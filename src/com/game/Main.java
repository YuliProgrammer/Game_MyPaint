package com.game;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;


public class Main extends Application {

    private static final int BOARD_WIDTH = 800;
    private static final int BOARD_HEIGHT = 500;

    private static Board board;
    private static Scene scene;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Paint");
        stage.getIcons().add(new Image("file:Palette.png"));

        Canvas canvas = new Canvas();
        canvas.setHeight(BOARD_HEIGHT);
        canvas.setWidth(BOARD_WIDTH);

        BorderPane group = new BorderPane(canvas);
        scene = new Scene(group);
        stage.setScene(scene);
        stage.show();

        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.fillText(" Move: \n" +
                "   tab - select, \n" +
                "   page up/down - change size, S - save, C - copy, Delete - delete figure; \n" +
                "   N - create new merge figure, Esc - sever the merge figure; \n" +
                "   W - record the trajectory of movement, R - reproduce the trajectory.\n" +
                " Mouse click: Ctrl-merge", 150.0D, 50.0D);

        board = new Board(gc);
        board.draw();
        startNewThread();
    }

    private void startNewThread() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mousePressed();
                keyPressed();
            }
        }).start();
    }

    private static void mousePressed() {
        scene.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getButton().equals(MouseButton.PRIMARY)) {
                    if (event.isControlDown()) {
                        board.move(event, event.getSceneX(), event.getSceneY());
                    }
                }

            }
        });
    }

    private static void keyPressed() {
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode() != KeyCode.CONTROL) {
                    board.move(keyEvent);
                }
            }
        });
    }
}
