#!/usr/bin/env bash
# I'm assuming people on windows are using GitBash!

#uncomment this line if you want to see the whole script run
#set -x

# assume this is running in the ./deploy directory
# must run on the development machine, not the roborio - the java sources don't make it to the rio!

echo "***** Running configuration file checker *****"

# First make sure that there are no temp files lying around from last time
# the -f forces the rm and doesn't complain if the file is missing
rm -f ./config.shell ./src.properties.found

# We need to use the find command, but windows has its own, completely different, find. So be explicit
# OS specific support (must be 'true' or 'false').
case "`uname`" in
  MINGW* )
    find=/usr/bin/find
    ;;
  MSYS* )
    find=/usr/bin/find
    ;;
    * )
    # we can just get it off the PATH like everything else
    find=find
    ;;
esac

# read config.properties and put its values into a new .shell file
# This takes
# foo = bar
# and adds a line like
# CONFIG[foo]=foo
# to the config.shell file
echo "declare -A CONFIG" > config.shell
cat ./config.properties | grep -v "#" | grep "=" | sed "s/ //g" | while read line ; do
	config=${line%%=*} # Take everything before the = sign
	echo "CONFIG[$config]=$config" >> config.shell
done

# Add this new array to the current environment by "dotting" it in (reads it as a set of commands)
. ./config.shell

# create the list of java references in java or kotlin files
# Try to intercept multiple properties on a single line
# This will fail for very dynamic properties (if you do StormProp.getInt(StormProp.getString(..)) )
# Lets just say please don't do that. If you want that behavior, use two steps.
# Note that some sed's don't support \n as newline, so this script has a real newline in it
$find .. -name "*.java" -exec grep "StormProp.get" {} \; | sed "s/ //g" | sed 's/StormProp/\
    StormProp/g' | grep StormProp  > src.properties.found
$find .. -name "*.kt"   -exec grep "StormProp.get" {} \; | sed "s/ //g" | sed 's/StormProp/\
    StormProp/g' | grep StormProp >> src.properties.found

cat src.properties.found | while read line ; do
	# start with e.g. REST_POSITION=StormProp.getInt("elevatorRestPosition",0);
	key="${line#*StormProp.}"
	# now have getInt("elevatorRestPosition",0);
	key="${key#*\(}"
	# now have "elevatorRestPosition",0);
	key="${key%%,*}"
	# now have "elevatorRestPosition" in quotes
	key=$(echo $key | sed 's/\"//g')
	# now have elevatorRestPosition without quotes

	# Does the key appear in the associative array? The value of the array doesn't matter, but the element won't exist if it wasn't in the config file
	if [[ "$key" == "${CONFIG[$key]}" ]] ; then
		: # nothing to do
	else
		echo "I found the key \"$key\" in source code and not in the config.properties file"
		echo "The line looks something like this:"
		echo "$line"
		exit 1 # fail
	fi
done

# bash apparently gets us here if the loop above calls exit...
res=$?

if [[ $res == 1 ]] ; then
    echo "********** FAILING **********"
else
    echo "***** Success *****"
fi

#It is safe to comment the rm line if you want to look at the files
rm -rf ./config.shell ./src.properties.found
exit $res
