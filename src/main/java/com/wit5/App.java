package com.wit5;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;


public class App extends Application {
    
    @Override
    public void start(Stage stage) {
        Pane root = new Pane();
        Scene scene = new Scene(root, 640, 480);
        stage.setTitle("Chess Board");
        stage.setScene(scene);
        stage.show();
        BoardManager boardManager = new BoardManager(root, scene);
    }
    
    public static void main(String[] args) {
        // API call to create the window, results in the start() method being called.
        launch();
    }

}