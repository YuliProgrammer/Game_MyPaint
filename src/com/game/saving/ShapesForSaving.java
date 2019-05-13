package com.game.saving;

import javafx.scene.input.KeyCode;

import java.util.ArrayList;
import java.util.List;

public class ShapesForSaving {

    private String type;
    private int index;
    private double x;
    private double y;
    private double diameter;
    private double[] startingCoordinates = new double[2];
    private List<ShapesForSaving> margeShapes = new ArrayList<>();
    private List<KeyCode> shapeWay = new ArrayList<>();

    public ShapesForSaving() {

    }

    public String getType() {
        return type;
    }

    public int getIndex() {
        return index;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getDiameter() {
        return diameter;
    }

    public double[] getStartingCoordinates() {
        return startingCoordinates;
    }

    public List<ShapesForSaving> getMargeShapes() {
        return margeShapes;
    }

    public List<KeyCode> getShapeWay() {
        return shapeWay;
    }

}
