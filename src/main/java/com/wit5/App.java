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

        // Create the chess board
        VisualBoard drawnBoard = new VisualBoard(scene);
        root.getChildren().add(drawnBoard);
        
        // Update the visual board's selected cell when the user clicks
        scene.setOnMouseClicked(event -> {
            // drawnBoard.selectCell(event.getSceneX(), event.getSceneY());
        });

    }
    
    public static void main(String[] args) {
        // API call to create the window, results in the start() method being called.
        launch();
    }

}