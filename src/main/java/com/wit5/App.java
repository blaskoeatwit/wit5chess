package com.wit5;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import com.wit5.VisualBoard.Cell;


public class App extends Application {
    
    @Override
    public void start(Stage stage) {
        Pane root = new Pane();
        Scene scene = new Scene(root, 640, 480);
        stage.setTitle("Chess Board");
        stage.setScene(scene);
        stage.show();

        // Create the chess board
        BoardManager boardManager = new BoardManager(scene);
        root.getChildren().add(boardManager.visualBoard);
        
        // Update the visual board's selected cell when the user clicks
        scene.setOnMouseClicked(event -> {
            Cell cell = boardManager.visualBoard.cellAt(event.getSceneX(), event.getSceneY());
            if (cell == null) { return; }
            System.out.println(cell);
        });

    }
    
    public static void main(String[] args) {
        // API call to create the window, results in the start() method being called.
        launch();
    }

}