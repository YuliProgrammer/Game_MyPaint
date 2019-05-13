package com.game.model;

import com.game.Board;
import com.game.MovingShape;
import com.game.saving.StartingCoordinates;
import com.google.gson.annotations.Expose;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class Triangle extends MovingShape {

    private GraphicsContext gc;
    private boolean isColor;

    @Expose
    private final String type = "Triangle";
    @Expose
    private int index;
    @Expose
    private double x;
    @Expose
    private double y;
    @Expose
    private double diameter;
    @Expose
    private List<KeyCode> shapeWay = new ArrayList<>();
    @Expose
    private double[] startingCoordinates = {StartingCoordinates.getX(), StartingCoordinates.getY()};

    public Triangle(GraphicsContext gc, List<Shape> shapes,
                    int index, double x, double y, double DIAMETER,
                    double[] startingCoordinates,
                    List<KeyCode> shapeWay) {
        super(gc, shapes);

        this.gc = gc;
        this.index = index;
        this.x = x;
        this.y = y;
        this.diameter = DIAMETER;
        this.startingCoordinates = startingCoordinates;

        this.shapeWay = shapeWay;
    }
    public Triangle(GraphicsContext gc, List<Shape> shapes, double DIAMETER, double x, double y, int index) {
        super(gc, shapes);

        this.gc = gc;
        this.index = index;
        this.x = x;
        this.y = y;
        this.diameter = DIAMETER;
    }

    public Triangle() {
    }

    @Override
    public void setColor(boolean color) {
        this.isColor = color;
    }

    @Override
    public int getIndex() {
        return index;
    }

    @Override
    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public double getX() {
        return x;
    }

    @Override
    public void setX(double xC) {
        this.x = xC;
    }

    @Override
    public double getY() {
        return y;
    }

    @Override
    public void setY(double yC) {
        this.y = yC;
    }

    @Override
    public double getDiameter() {
        return diameter;
    }

    @Override
    public void setDiameter(double diameter) {
        this.diameter = diameter;
    }

    @Override
    public List<Shape> getMargeShapes() {
        return null;
    }

    @Override
    public void setMargeShapes(List<Shape> margeShapes) {
    }

    @Override
    public List<KeyCode> getTrajectory() {
        return shapeWay;
    }

    @Override
    public void setTrajectory(List<KeyCode> shapeWay) {
        this.shapeWay = shapeWay;
    }

    @Override
    public double[] getStartingCoordinates() {
        return startingCoordinates;
    }

    @Override
    public void setStartingCoordinates(double x, double y) {
        this.startingCoordinates[0] = x;
        this.startingCoordinates[1] = y;
    }

    @Override
    public void draw() {

        double[] xPoints = {this.x, this.x - (this.diameter - 10), this.x + (this.diameter - 10)};
        double[] yPoints = {this.y, this.y + this.diameter, this.y + this.diameter};

        Color color;

        if (!isColor) {
            if (index == Board.glShapeCurrentIndex) {
                color = Color.FORESTGREEN;
            } else {
                color = Color.WHITE;
            }
        } else {
            color = Color.YELLOW;
        }

        gc.setFill(color);
        gc.fillPolygon(xPoints, yPoints, 3);
        gc.setStroke(Color.FORESTGREEN);
        gc.setLineWidth(3);

        gc.strokePolygon(xPoints, yPoints, 3);

    }

}
