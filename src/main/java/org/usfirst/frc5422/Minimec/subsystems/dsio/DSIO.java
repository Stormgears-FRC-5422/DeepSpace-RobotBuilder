package org.usfirst.frc5422.Minimec.subsystems.dsio;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DSIO {

    private Joystick joystick;
    //ButtonBoard is seen as 2 different JoySticks
    private Joystick buttonBoard;
    private Joystick buttonBoard2;

    //TODO: RENAME BUTTONS BASED ON COLOR
    private JoystickButton elevatorSwitch, cargoIntake, cargoRelease, backJackLevel3,backJackLevel2,
            wristSwitch, intakeOn, hatchIntake, hatchRelease, elevatorRest, elevatorGround, elevatorLevelOne, elevatorLevelTwo, elevatorLevelThree;

    public DSIO(){
        this(0,1);
    }
    public DSIO(int joystickChannel, int buttonBoardChannel){
        joystick = new Joystick(joystickChannel);
        buttonBoard = new Joystick(buttonBoardChannel);
        buttonBoard2 = new Joystick(buttonBoardChannel+1);

        elevatorSwitch = new JoystickButton(buttonBoard, ButtonIds.ELEVATOR_FAILSAFE_BUTTON_ID);

        backJackLevel2 = new JoystickButton(buttonBoard, ButtonIds.BACKJACK_LEVEL_2);
        backJackLevel3 = new JoystickButton(buttonBoard, ButtonIds.BACKJACK_LEVEL_3);

        wristSwitch = new JoystickButton(buttonBoard, ButtonIds.WRIST_SWITCH_ID);

        intakeOn = new JoystickButton(buttonBoard, ButtonIds.INTAKE_SWITCH_ID);

        hatchIntake = new JoystickButton(buttonBoard, ButtonIds.HATCH_STATUS_INTAKE_BUTTON_ID);

        hatchRelease = new JoystickButton(buttonBoard, ButtonIds.HATCH_STATUS_RELEASE_BUTTON_ID);

        elevatorRest = new JoystickButton(buttonBoard, ButtonIds.ELEVATOR_REST_BUTTON_ID);
        elevatorGround = new JoystickButton(buttonBoard, ButtonIds.ELEVATOR_LEVEL_GROUND_BUTTON_ID);
        elevatorLevelOne = new JoystickButton(buttonBoard, ButtonIds.ELEVATOR_LEVEL_1_BUTTON_ID);
        elevatorLevelTwo = new JoystickButton(buttonBoard, ButtonIds.ELEVATOR_LEVEL_2_BUTTON_ID);
        elevatorLevelThree = new JoystickButton(buttonBoard, ButtonIds.ELEVATOR_LEVEL_3_BUTTON_ID);

        cargoIntake = new JoystickButton(buttonBoard2, ButtonIds.CARGO_STATUS_INTAKE_BUTTON_ID);
        cargoRelease = new JoystickButton(buttonBoard2, ButtonIds.CARGO_STATUS_RELEASE_BUTTON_ID);

    }
    //Outputs whats Buttons are being pressed on the ButtonBoards
    public void smartDashboard() {
        for (int i = 0; i <= buttonBoard.getButtonCount(); i++) {
            SmartDashboard.putBoolean("ButtonBoard Button " + i, buttonBoard.getRawButton(i));
        }
        for (int i = 0; i <= buttonBoard.getButtonCount(); i++) {
            SmartDashboard.putBoolean("ButtonBoard 2 Button " + i, buttonBoard.getRawButton(i));
        }
    }

    public void checkSwitches(){

        //TODO: ADD SUBSYSTEMS AND COMMANDS


        //if (buttonBoard.getRawButton(ButtonIds.BACKJACK_SWITCH_ID)){
            //TODO: Backjack class
        //}

    }
    public Joystick getJoystick(){
        return joystick;
    }


}
