package com.game.model;

import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;

import java.util.List;

public interface Shape {

    void move(KeyCode event);
    void move(MouseEvent event, double mouseX, double mouseY, List<Shape> margeF, Figure figure, Shape s);

    void draw();
    void setColor(boolean isColor);

    int getIndex();
    void setIndex(int index);

    double getX();
    void setX(double xC);

    double getY();
    void setY(double yC);

    double getDiameter();
    void setDiameter(double diameter);

    List<Shape> getMargeShapes();
    void setMargeShapes(List<Shape> margeShapes);

    List<KeyCode> getTrajectory();
    void setTrajectory(List<KeyCode> shapeWay);

    double[] getStartingCoordinates() ;
    void setStartingCoordinates(double x, double y);
}
