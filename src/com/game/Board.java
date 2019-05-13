package com.game;

import com.game.model.*;

import com.game.saving.ShapesForSaving;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Board {

    public static int glShapeCurrentIndex = 0;
    private int index = 0;

    private GraphicsContext gc;
    private static Timer timer;

    private List<Shape> shapes = new ArrayList<>();
    private List<Shape> margeShape;
    private boolean isNewMargeShape = true;

    private Shape shape;
    private Figure figure;

    public Board(GraphicsContext gc) {
        this.gc = gc;

        File JSON = new File("shapeList.json");
        if (JSON != null && JSON.length() > 0) {
            readFromJson("shapeList.json");
        } else {
            shapes.add(new Square(gc, shapes, 30, 5, 40, index));
            index++;

            shapes.add(new Triangle(gc, shapes, 30, 70, 40, index));
            index++;

            shapes.add(new Circle(gc, shapes, 30, 30, 80, index));
            index++;
        }

        if (isNewMargeShape) {
            margeShape = new ArrayList<>();
            figure = new Figure(gc, shapes, 30, 10, 40, shapes.size() - 1);
            isNewMargeShape = false;
        }
    }

    public void draw() {
        for (Shape shape : shapes) {
            if (shape instanceof Figure) {
                for (Shape s : shape.getMargeShapes()) {
                    s.draw();
                }
            } else {
                shape.draw();
            }
        }
    }

    private int count = 0;

    public void move(KeyEvent event) {
        shape = shapes.get(glShapeCurrentIndex);
        timer = new Timer();

        if (event.getCode() == KeyCode.R) {// для движения по траэктории

            List<KeyCode> events = shape.getTrajectory();
            if (events.isEmpty() || events.size() == 0) {
                return;
            }

            if (shape instanceof Figure) {
                for (Shape s : shape.getMargeShapes()) {
                    s.setX(s.getStartingCoordinates()[0]);
                    s.setY(s.getStartingCoordinates()[1]);
                }
            } else {
                shape.setX(shape.getStartingCoordinates()[0]);
                shape.setY(shape.getStartingCoordinates()[1]);
            }


            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    if (count != events.size()) {
                        shape.move(events.get(count));
                        clear();
                        draw();
                    } else {
                        count = 0;
                        stopTimer();
                        return;
                    }
                    count++;
                }
            };

            timer.schedule(task, 0, 100);

        } else if (event.getCode() == KeyCode.N) {   // для создания новой объединенной фигуры
            if (!margeShape.isEmpty() && margeShape.size() != 0) {
                margeShape = new ArrayList<>();
                figure = new Figure(gc, shapes, 30, 10, 40, shapes.size() - 1);
            }
        } else {
            shape.move(event.getCode());
            clear();
            draw();
        }
    }

    public void move(MouseEvent event, double mouseX, double mouseY) {
        for (int i = shapes.size() - 1; i >= 0; i--) {
            shapes.get(i).move(event, mouseX, mouseY, margeShape, figure, shapes.get(i));
        }
        clear();
        draw();
    }

    public void clear() {
        gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
    }

    private static void stopTimer() {
        timer.cancel();
        timer.purge();
    }

    private void readFromJson(String fileName) {
        Gson gson = new Gson();

        try {
            String json = jsonToString(fileName);

            Type listType = new TypeToken<ArrayList<ShapesForSaving>>() {
            }.getType();
            List<ShapesForSaving> usersList = gson.fromJson(json, listType);

            shapes.clear();

            for (ShapesForSaving save : usersList) {
                if (save.getType().equals("Figure")) {
                    List<ShapesForSaving> margeShapeForSaving = save.getMargeShapes();
                    List<Shape> margeShape = new ArrayList<>();

                    for (ShapesForSaving margeSave : margeShapeForSaving) {
                        readTypes(margeSave, margeShape);
                    }

                    shapes.add(new Figure(gc, shapes, save.getIndex(), save.getX(), save.getY(), save.getDiameter(),
                            margeShape, save.getShapeWay()));
                } else {
                    readTypes(save, shapes);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String jsonToString(String fileName) {
        StringBuilder sb = new StringBuilder();

        try {
            File file = new File(fileName);

            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            InputStream inputStream = new FileInputStream(fileName);
            BufferedReader buf = new BufferedReader(new InputStreamReader(inputStream));

            String line = buf.readLine();
            sb = new StringBuilder();

            while (line != null) {
                sb.append(line).append("\n");
                line = buf.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    private void readTypes(ShapesForSaving saving, List<Shape> shapeList) {
        double[] startCoordinates = saving.getStartingCoordinates();

        if (saving.getType().equals("Square")) {
            shapeList.add(new Square(gc, shapes, saving.getIndex(), saving.getX(), saving.getY(),
                    saving.getDiameter(), startCoordinates, saving.getShapeWay()));
        } else if (saving.getType().equals("Triangle")) {
            shapeList.add(new Triangle(gc, shapes, saving.getIndex(), saving.getX(), saving.getY(),
                    saving.getDiameter(), startCoordinates, saving.getShapeWay()));
        } else if (saving.getType().equals("Circle")) {
            shapeList.add(new Circle(gc, shapes, saving.getIndex(), saving.getX(), saving.getY(),
                    saving.getDiameter(), startCoordinates, saving.getShapeWay()));
        }
    }

}
