package org.usfirst.frc5422.utils;

import java.awt.*;
import java.util.ArrayList;
import org.usfirst.frc5422.Minimec.Robot;
import java.util.List;
import java.util.stream.*;  // Import the Collections class

/*
   A collection of PixyObjects with methods to filter and sort according to need
*/
public class PixyObjectCollection {
    private ArrayList<PixyObject> m_list;
    public PixyObjectCollection(ArrayList<PixyObject> list) {
        m_list = list;
    }
    
    public PixyObjectCollection(String type[],double x[],double y[],double width[],double height[]) {
        int i;
        m_list = new ArrayList<PixyObject>();
        if (type.length != x.length || type.length != y.length || type.length != height.length || type.length != width.length) {
            System.out.println("Detected inconsistent object list");
        }
        else {
            for (i=0; i<x.length; i++) {
                PixyObject item = new PixyObject(type[i],x[i],y[i],width[i],height[i]);
                if (item != null) {
                    // Ignore items not meeting min height requirements (too far away)
                    if (height[i] >= item.getMinHeight()) {
                        m_list.add(item);
                    }
                }
            }
        }
    }

    // Filter out dock objects that are either don't match the height of the closer ones or are far away
    // relative to the inter dock distance
    public void filterDocksByProximity() {
        double min_height_factor = .7;
        double min_span_factor = 3;
        double min_height;  // Any object smaller than this rejected 
        double min_span; // Any object farther than this from the main ones are ignored
        double closest_height= 0; // the height of the closest (largest)
        if (m_list.size() > 0) {
            List<PixyObject> sorted_objects = getLargest();
            closest_height = sorted_objects.get(0).getHeight();
            min_height = min_height_factor * closest_height;
            min_span = closest_height * min_span_factor;

            // remove objects that are too small
            m_list.removeIf( obj -> { 
                    if (obj.getHeight() < min_height) { 
                        if (Robot.debug) System.out.println("Dropping object for distance x=" + obj.getX() + " h=" + obj.getHeight());
                        
                        return(true); 
                    }
                    else { return(false);} 
            });
        }
    }

    // Filter out by y pos high or low (for rocket)
    // relative to the inter dock distance
    public void filterDocksForRocket(boolean request_high) {
        double ref_height; // height of referenc target (high or low)
        double min_diff; // min difference to consider one higher than another
        double factor = .5;  
        List<PixyObject> sorted_objects;
        PixyObject highest_object;
        PixyObject lowest_object;

        if (m_list.size() > 1) {
            sorted_objects = m_list.stream().sorted((a,b) -> {
                return(Double.compare(a.getY(),b.getY()));
            })
            .collect(Collectors.toList());
            highest_object = sorted_objects.get(sorted_objects.size() - 1);
            lowest_object = sorted_objects.get(0);

            if (request_high) {
                ref_height = highest_object.getY();
            }
            else {
                ref_height = lowest_object.getY();
            }

            // remove objects that are too low or too high
            m_list.removeIf( obj -> { 
                    if (request_high && obj.getY() < (highest_object.getY() - ref_height * factor)) {
                        if (Robot.debug) System.out.println("Dropping low object for height");
                        return(true); 
                    } else if (!request_high && obj.getY() > (lowest_object.getY() + ref_height * factor)) {
                        if (Robot.debug) System.out.println("Dropping high object for height");
                        return(true);
                    } else {
                        return(false);
                    }
            });
        }
    }


    // List sorted by largest first
    public List<PixyObject> getLargest() {
        List<PixyObject> ret_list = new ArrayList<PixyObject>();
        // Sort by closest to center
        ret_list = m_list.stream()
            .sorted((a, b) -> {
                double area_a = a.getX() * a.getY();
                double area_b = b.getX() * b.getY();
                return(Double.compare(area_b,area_a));
            })
            .collect(Collectors.toList());   

        return(ret_list);
    }

    // Find the objects closest to the center of view
    public List<PixyObject> getCenterX() {
        
        return(getClosest(PixyObject.frame_center_x));
    }

    // Useful for tracking the same object (x position only)
    public List<PixyObject> getClosest(double x_pos) {
        return(getClosest(x_pos,-1));
    }

    // Pick the pair of objects adjacent (left and right) to given X coord (dock pair tracking)
    public List<PixyObject> getAdjacentPair(double x_pos) {
        List<PixyObject> ret_list = new ArrayList<PixyObject>();
        PixyObject left = null;
        PixyObject right = null;
        // Sort by closest to center

        for (PixyObject obj : m_list) {
            if (obj.getX() < x_pos) {
                if (left == null) { left = obj; }
                else if (obj.getX() > left.getX()) { left = obj; }
            } else {
                if (right == null) { right = obj; }
                else if (obj.getX() < right.getX()) { right = obj; }
            }
        }
        if (left != null && right != null) { 
            ret_list.add(left);  ret_list.add(right); 
        }

        return(ret_list);
    }
 
    // Useful for tracking the same object
    public List<PixyObject> getClosest(double x_pos, double y_pos) {
        List<PixyObject> ret_list = new ArrayList<PixyObject>();
        // Sort by closest to center
        ret_list = m_list.stream()
            .sorted((a, b) -> {
                if (y_pos < 0) { //  Only interested in X axis
                    double a_x_dist = Math.abs(a.getX() - x_pos);
                    double b_x_dist = Math.abs(b.getX() - x_pos);
                    return(Double.compare(a_x_dist,b_x_dist));
                }
                else {
                    double dist_a = Math.pow(a.getX() - x_pos,2) + Math.pow(a.getY() - y_pos,2);
                    double dist_b = Math.pow(b.getX() - x_pos,2) + Math.pow(b.getY() - y_pos,2);
                    return(Double.compare(dist_a,dist_b));

                }
            })
            .collect(Collectors.toList());   

        return(ret_list);
    }


    public List<PixyObject> leftToRight() {
        List<PixyObject> return_list = new ArrayList<PixyObject>();
        //Sort by furthest left pair
        return_list = m_list.stream()
            .sorted((a, b) -> {
                return(Double.compare(a.getX(),b.getX()));
            })
            .collect(Collectors.toList());

        return(return_list);
    }

    public List<PixyObject> rightToLeft() {
        List<PixyObject> return_list = new ArrayList<PixyObject>();
        //Sort by furthest left pair
        return_list = m_list.stream()
                .sorted((a,b) -> {
                    return(Double.compare(b.getX(),a.getX()));
                })
                .collect(Collectors.toList());

        return(return_list);
    }

    public int size() {
        return m_list.size();
    }
}
