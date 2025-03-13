package com.wit5;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;


public class App extends Application {

    /**
     * All initialization should be done in this method.
     */
    @Override
    public void start(Stage stage) {
        // Creates an empty StackPane for the new window. This can also be accomplished by loading an FXML file.
        StackPane root = new StackPane();
        Scene scene = new Scene(root, 640, 480);
        
        stage.setTitle("Basic Window");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Our main function, entry point of the application
     */
    public static void main(String[] args) {
        // API call to create the window, results in the start() method being called.
        launch();
    }

}