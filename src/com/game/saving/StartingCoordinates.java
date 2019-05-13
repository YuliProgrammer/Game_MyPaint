package com.game.saving;

public class StartingCoordinates {

    private static double[] coordinates = new double[2];

    public StartingCoordinates(double x, double y) {
        coordinates[0] = x;
        coordinates[1] = y;
    }

    public StartingCoordinates() {
    }

    public static double getX() {
        return coordinates[0];
    }

    public static double getY() {
        return coordinates[1];
    }

}
