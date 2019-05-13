package com.game;

import com.game.model.*;
import com.game.saving.Save;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.List;

public abstract class MovingShape implements Shape {

    private GraphicsContext gc;
    private List<Shape> shapes = new ArrayList<>();

    private double DIAMETER;
    private double x;
    private double y;
    private int index;

    private Shape currentShape;
    private boolean isTheSameCurrentShape = false;
    private boolean isRecordWay = false;
    private List<KeyCode> shapeWay = new ArrayList<>();

    public MovingShape(GraphicsContext gc, List<Shape> shapes) {
        this.gc = gc;
        this.shapes = shapes;
    }

    public MovingShape() {
    }

    @Override
    public void move(KeyCode eventCode) {

        if (eventCode == KeyCode.TAB) {
            isTheSameCurrentShape = false;
            currentShape = shapes.get(Board.glShapeCurrentIndex);
            currentShape.setColor(false);
            if (Board.glShapeCurrentIndex < shapes.size() - 1) {
                Board.glShapeCurrentIndex++;
            } else {
                Board.glShapeCurrentIndex = 0;
            }
        } else {
            isTheSameCurrentShape = true;
        }

        currentShape = shapes.get(Board.glShapeCurrentIndex);
        drawingCurrentShape();

        if (eventCode == KeyCode.W) {  // записать траекторию движения
            if (!isRecordWay) {
                if (currentShape instanceof Figure) {
                    for (Shape shape : currentShape.getMargeShapes()) {
                        shape.setStartingCoordinates(shape.getX(), shape.getY());
                    }
                } else {
                    currentShape.setStartingCoordinates(currentShape.getX(), currentShape.getY());
                }
                isRecordWay = true;
            } else {
                isRecordWay = false;
            }
        }

        if (isRecordWay) {
            if (isTheSameCurrentShape) {
                if (eventCode != KeyCode.W) {
                    shapeWay.add(eventCode);
                }
            } else {
                currentShape.setTrajectory(shapeWay);
                shapeWay = new ArrayList<>();
            }
        } else if (!isRecordWay && !shapeWay.isEmpty()) {
            currentShape.setTrajectory(shapeWay);
            shapeWay = new ArrayList<>();
        }

        if (currentShape instanceof Figure) {

            if (eventCode == KeyCode.C || eventCode == KeyCode.DELETE ||
                    eventCode == KeyCode.ESCAPE || eventCode == KeyCode.S) {
                moveAll(eventCode);
            } else {
                List<Shape> margeF = currentShape.getMargeShapes();
                boolean canMove = false;

                for (Shape shape : margeF) {
                    if (eventCode == KeyCode.TAB) {
                        canMove = true;
                        break;
                    }

                    if ((eventCode == KeyCode.PAGE_DOWN && shape.getDiameter() > 2) ||
                            (eventCode == KeyCode.PAGE_UP && shape.getDiameter() < gc.getCanvas().getHeight())) {
                        canMove = true;
                    } else if ((eventCode == KeyCode.LEFT && shape.getX() > 5) ||
                            (eventCode == KeyCode.RIGHT && shape.getX() < gc.getCanvas().getWidth() - shape.getDiameter()) ||
                            (eventCode == KeyCode.DOWN && shape.getY() < gc.getCanvas().getHeight() - shape.getDiameter()) ||
                            (eventCode == KeyCode.UP && shape.getY() > 5)) {
                        canMove = true;
                    } else {
                        canMove = false;
                        break;
                    }
                }

                if (canMove) {
                    for (Shape shape : margeF) {
                        x = shape.getX();
                        y = shape.getY();
                        DIAMETER = shape.getDiameter();

                        moveAll(eventCode);

                        shape.setX(x);
                        shape.setY(y);
                        shape.setDiameter(DIAMETER);
                    }
                }
            }

        } else {
            x = currentShape.getX();
            y = currentShape.getY();
            index = currentShape.getIndex();
            DIAMETER = currentShape.getDiameter();

            moveAll(eventCode);

            currentShape.setX(x);
            currentShape.setY(y);
            currentShape.setDiameter(DIAMETER);
        }

        if (eventCode != KeyCode.DELETE && eventCode != KeyCode.TAB &&
                eventCode != KeyCode.W && eventCode != KeyCode.S) {
            indexRebuild();

        }

        x = 0;
        y = 0;
        DIAMETER = 0;
    }

    @Override
    public void move(MouseEvent event, double mouseX, double mouseY, List<Shape> margeF, Figure figure, Shape shape) {

        if (getDistance(shape.getX(), shape.getY(), mouseX, mouseY) <= shape.getDiameter()) {
            Board.glShapeCurrentIndex = shape.getIndex();

            margeF.add(shape);
            figure.setMargeShapes(margeF);
            figure.setIndex(Board.glShapeCurrentIndex);
            shapes.remove(shape);

            if (shapes.contains(figure)) {
                shapes.remove(figure);
                shapes.add(figure);
            } else {
                shapes.add(figure);
            }

            for (int j = 0; j < shapes.size(); j++) {
                Shape sh = shapes.get(j);
                sh.setIndex(j);
                if (sh instanceof Figure) {
                    Board.glShapeCurrentIndex = j;
                    List<Shape> list = shapes.get(j).getMargeShapes();
                    for (Shape s : list) {
                        s.setIndex(j);
                    }
                }
            }
        }
    }

    private void moveAll(KeyCode eventCode) {

        if (eventCode == KeyCode.LEFT) {
            if (x > 5) {
                x = x - 5;
            }
        } else if (eventCode == KeyCode.RIGHT) {
            if (x < gc.getCanvas().getWidth() - DIAMETER) {
                x = x + 5;
            }
        } else if (eventCode == KeyCode.DOWN) {
            if (y < gc.getCanvas().getHeight() - DIAMETER) {
                y = y + 5;
            }
        } else if (eventCode == KeyCode.UP) {
            if (y > 5) {
                y = y - 5;
            }
        }

        if (eventCode == KeyCode.PAGE_DOWN) {
            if (DIAMETER > 2) {
                DIAMETER--;
            }
        } else if (eventCode == KeyCode.PAGE_UP) {
            if (DIAMETER < gc.getCanvas().getHeight()) {
                DIAMETER++;
            }
        }

        if (eventCode == KeyCode.DELETE) {
            if (shapes.size() > 1) {
                if (currentShape instanceof Figure) {
                    List<Shape> margeF = currentShape.getMargeShapes();
                    margeF.clear();
                }
                shapes.remove(Board.glShapeCurrentIndex);
                indexRebuild();

                if (Board.glShapeCurrentIndex > 0) {
                    Board.glShapeCurrentIndex--;
                } else {
                    Board.glShapeCurrentIndex = 0;
                }
            }
        } else if (eventCode == KeyCode.C) {

            if (currentShape instanceof Figure) {
                List<Shape> newList = new ArrayList<>();
                List<Shape> list = currentShape.getMargeShapes();

                for (Shape s : list) {
                    copyShape(newList, s);
                }

                Figure figure = new Figure(gc, shapes, DIAMETER, x, y, shapes.size() - 1);
                figure.setMargeShapes(newList);
                shapes.add(figure);

            } else {
                copyShape(shapes, currentShape);
            }

        } else if (eventCode == KeyCode.ESCAPE) { // для разъединения фигур
            if (currentShape instanceof Figure) {
                List<Shape> list = currentShape.getMargeShapes();
                for (Shape s : list) {
                    shapes.add(s);
                }

                shapes.remove(currentShape);
                Board.glShapeCurrentIndex = 0;
            }
        }

        if (eventCode == KeyCode.S) {  // save
            Save.SaveAll(shapes);
        }

    }

    private void drawingCurrentShape() {
        boolean isColoring = false;

        for (Shape s : shapes) {
            if (currentShape != s) {
                if (currentShape instanceof Figure) {
                    if (s instanceof Figure) {
                        for (Shape shape1 : s.getMargeShapes()) {
                            for (Shape shape2 : currentShape.getMargeShapes()) {
                                if (getDistance(shape1.getX(), shape1.getY(), shape2.getX(), shape2.getY()) < shape2.getDiameter()) {
                                    isColoring = true;
                                    break;
                                }
                            }
                        }
                    } else {
                        for (Shape shape : currentShape.getMargeShapes()) {
                            if (getDistance(shape.getX(), shape.getY(), s.getX(), s.getY()) < shape.getDiameter()) {
                                isColoring = true;
                                break;
                            }
                        }
                    }

                    if (isColoring == true) {
                        for (Shape sh : currentShape.getMargeShapes()) {
                            sh.setColor(true);
                        }
                    } else {
                        isColoring = false;
                    }

                } else {
                    if (s instanceof Figure) {
                        for (Shape sh : s.getMargeShapes()) {
                            if (getDistance(currentShape.getX(), currentShape.getY(), sh.getX(), sh.getY()) < currentShape.getDiameter()) {
                                currentShape.setColor(true);
                                isColoring = true;
                                break;
                            } else {
                                isColoring = false;
                            }
                        }
                    } else {
                        if (getDistance(currentShape.getX(), currentShape.getY(), s.getX(), s.getY()) < currentShape.getDiameter()) {
                            currentShape.setColor(true);
                            isColoring = true;
                            break;
                        } else {
                            isColoring = false;
                        }
                    }
                }
            }
        }

        if (isColoring == false) {
            if (currentShape instanceof Figure) {
                List<Shape> figure = currentShape.getMargeShapes();
                for (Shape sh : figure) {
                    sh.setColor(false);
                }
            } else {
                currentShape.setColor(false);
            }
        }
    }

    private void copyShape(List<Shape> list, Shape shape) {
        if (shape instanceof Circle) {
            list.add(new Circle(gc, shapes, shape.getDiameter(), shape.getX(), shape.getY(), shapes.size() - 1));
        } else if (shape instanceof Triangle) {
            list.add(new Triangle(gc, shapes, shape.getDiameter(), shape.getX(), shape.getY(), shapes.size() - 1));
        } else if (shape instanceof Square) {
            list.add(new Square(gc, shapes, shape.getDiameter(), shape.getX(), shape.getY(), shapes.size() - 1));
        }
    }

    private double getDistance(double x, double y, double x2, double y2) {
        return Math.sqrt(Math.pow(x - x2, 2) + Math.pow(y - y2, 2));
    }

    private void indexRebuild() {
        for (int i = 0; i < shapes.size(); i++) {
            Shape s = shapes.get(i);
            s.setIndex(i);
            if (s instanceof Figure) {
                List<Shape> list = s.getMargeShapes();
                for (Shape sh : list) {
                    sh.setIndex(i);
                }
            }
        }
    }

    private void clear() {
        gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
    }

    @Override
    public abstract void draw();
}