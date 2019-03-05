package org.usfirst.frc5422.Minimec;

import java.awt.*;
import java.util.ArrayList;
//import org.usfirst.frc5422.Minimec.PixyObject;
import java.util.List;
import java.util.stream.*;  // Import the Collections class

/*
   A collection of PixyObjects with methods to filter and sort according to need
*/
public class PixyObjectCollection {
    private ArrayList<PixyObject> m_list;
    private final double FRAME_SIZE = 320;

    public PixyObjectCollection(ArrayList<PixyObject> list) {
        m_list = list;
    }
    public PixyObjectCollection(String type[],double x[],double y[],double height[],double width[]) {
        int i;
        m_list = new ArrayList<PixyObject>();
        for (i=0; i<x.length; i++) {
            PixyObject item = new PixyObject(type[i],x[i],y[i],height[i],width[i]);
            if (item != null) {
                m_list.add(item);
            }
        }
    }

    // Find the largest object and return it
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
                double a_x_dist = Math.abs(a.getX());
                double b_x_dist = Math.abs(b.getX());
                return(Double.compare(a_x_dist,b_x_dist));
            })
            .collect(Collectors.toList());

        return(return_list);
    }

    public List<PixyObject> rightToLeft() {
        List<PixyObject> return_list = new ArrayList<PixyObject>();
        //Sort by furthest left pair
        return_list = m_list.stream()
                .sorted((a,b) -> {
                    double a_x_dist = Math.abs(a.getX());
                    double b_x_dist = Math.abs(b.getX());
                    return(Double.compare(b_x_dist,a_x_dist));
                })
                .collect(Collectors.toList());

        return(return_list);
    }

    public int size() {
        return m_list.size();
    }
}