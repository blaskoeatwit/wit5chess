package com.wit5;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;


public class App extends Application {
    private record Cell(int x, int y) {}
    private static final int BOARD_SIZE = 8; // 8x8 chess board
    
    // Store these as instance variables so they can be accessed by methods
    private Pane chessBoard;
    private double boardSize;
    private double squareSize;
    private double startX;
    private double startY;
    private Cell selectedCell;
    private Rectangle selectedSquareHighlight;
    
    @Override
    public void start(Stage stage) {
        Pane root = new Pane();
        Scene scene = new Scene(root, 640, 480);

        selectedSquareHighlight = new Rectangle();
        selectedSquareHighlight.setWidth(squareSize);
        selectedSquareHighlight.setHeight(squareSize);
        selectedSquareHighlight.setVisible(false);
        selectedSquareHighlight.setFill(Color.RED.deriveColor(0, 1, 1, 0.5)); // Semi-transparent red
        
        stage.setTitle("Chess Board");
        stage.setScene(scene);
        stage.show();

        // Create the chess board
        chessBoard = new Pane();
        root.getChildren().add(chessBoard);
        
        // Draw the initial chess board
        drawChessBoard(scene.getWidth(), scene.getHeight());
        
        // Update the chess board when the window is resized
        scene.widthProperty().addListener((obs, oldVal, newVal) -> 
            drawChessBoard(newVal.doubleValue(), scene.getHeight()));
        scene.heightProperty().addListener((obs, oldVal, newVal) -> 
            drawChessBoard(scene.getWidth(), newVal.doubleValue()));

        scene.setOnMouseClicked(event -> {
            Cell cell = getCell(event.getSceneX(), event.getSceneY());
            if (cell != null) { 
                selectedCell = cell;
                selectedSquareHighlight.setVisible(true);
                selectedSquareHighlight.setX(startX + cell.x() * squareSize);
                selectedSquareHighlight.setY(startY + cell.y() * squareSize);
            } else {
                selectedSquareHighlight.setVisible(false);
            }
        });

    }
    
    private void drawChessBoard(double sceneWidth, double sceneHeight) {
        chessBoard.getChildren().clear();
        boardSize = Math.min(sceneWidth, sceneHeight) * 0.8; // Use 80% of the smaller dimension
        squareSize = boardSize / BOARD_SIZE;
        startX = (sceneWidth - boardSize) / 2;
        startY = (sceneHeight - boardSize) / 2;
        
        // Draw the chess board (Temporary, eventually load/resize image)
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                Rectangle square = new Rectangle(
                    startX + col * squareSize, 
                    startY + row * squareSize, 
                    squareSize, 
                    squareSize
                );
                
                if ((row + col) % 2 == 0) {
                    square.setFill(Color.BEIGE);
                } else {
                    square.setFill(Color.SADDLEBROWN);
                }
                
                square.setStroke(Color.BLACK);
                chessBoard.getChildren().add(square);
            }
        }
        
        // Update the selectedSquareHighlight's size
        selectedSquareHighlight.setWidth(squareSize);
        selectedSquareHighlight.setHeight(squareSize);
        
        // Update the selectedSquareHighlight's position if there's a selected cell
        if (selectedCell != null) {
            selectedSquareHighlight.setX(startX + selectedCell.x() * squareSize);
            selectedSquareHighlight.setY(startY + selectedCell.y() * squareSize);
            selectedSquareHighlight.setVisible(true);
        }
        
        // Add the selectedSquareHighlight back to the chessBoard after all other squares
        // This ensures it will be on top of the board
        chessBoard.getChildren().add(selectedSquareHighlight);
    }
    
    private Cell getCell(double sceneX, double sceneY) {
        if (sceneX < startX || sceneX > startX + boardSize || 
            sceneY < startY || sceneY > startY + boardSize) {
            return null;
        }
        int x = (int)((sceneX - startX) / squareSize);
        int y = (int)((sceneY - startY) / squareSize);
        if (x < 0 || x >= BOARD_SIZE || y < 0 || y >= BOARD_SIZE) {
            return null;
        }
        return new Cell(x, y);
    }
    
    public static void main(String[] args) {
        // API call to create the window, results in the start() method being called.
        launch();
    }

}