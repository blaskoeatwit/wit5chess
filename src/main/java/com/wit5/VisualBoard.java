package com.wit5;

import javafx.scene.layout.Pane;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;



public class VisualBoard extends Pane {
    record Cell(int x, int y) {}

    private double boardSize;
    private double squareSize;
    private double startX;
    private double startY;
    private Rectangle selectedSquareHighlight;
    private Cell selectedCell;
    private ImageView boardView;


    public VisualBoard(Scene scene) {
        boardView = new ImageView(new Image("file:src/main/java/Resources/tempTestBoard.png"));
        
        selectedSquareHighlight = new Rectangle();
        selectedSquareHighlight.setVisible(false);
        // Translucent red
        selectedSquareHighlight.setFill(Color.RED.deriveColor(0, 1, 1, 0.5));
        
        // Add to the scene
        getChildren().add(boardView);
        getChildren().add(selectedSquareHighlight);
        // Set initial position
        scaleBoard(scene.getWidth(), scene.getHeight());
        
        // Set up resize listeners
        scene.widthProperty().addListener((obs, oldVal, newVal) -> {
            scaleBoard(newVal.doubleValue(), scene.getHeight());
        });
        scene.heightProperty().addListener((obs, oldVal, newVal) -> {
            scaleBoard(scene.getWidth(), newVal.doubleValue());
        });
    }

    public void scaleBoard(double width, double height) {
        boardSize = Math.min(width, height) * 0.8;
        squareSize = boardSize / 8;
        startX = (width - boardSize) / 2;
        startY = (height - boardSize) / 2;
        
        // Update the board image size and position
        boardView.setFitWidth(boardSize);
        boardView.setFitHeight(boardSize);
        boardView.setX(startX);
        boardView.setY(startY);
        
        // Update highlight size
        selectedSquareHighlight.setWidth(squareSize);
        selectedSquareHighlight.setHeight(squareSize);
        // Only update the highlight position if a cell is selected (otherwise no point)
        if (selectedCell != null) {
            selectedSquareHighlight.setLayoutX(startX + selectedCell.x() * squareSize);
            selectedSquareHighlight.setLayoutY(startY + selectedCell.y() * squareSize);
        }
    }

    // The double position check exists because I was occasionally getting (rounding) errors which led to cells outside the board being selected
    private Cell cellAt(double sceneX, double sceneY) {
        if (sceneX < startX || sceneX > startX + boardSize || 
            sceneY < startY || sceneY > startY + boardSize) {
            return null;
        }
        int x = (int)((sceneX - startX) / squareSize);
        int y = (int)((sceneY - startY) / squareSize);
        if (x < 0 || x >= 8 || y < 0 || y >= 8) {
            return null;
        }
        return new Cell(x, y);
    }

    public void selectCell(double x, double y) {
        Cell cell = cellAt(x, y);
        System.out.println("Selected cell: " + cell);
        if (cell == null) {
            selectedSquareHighlight.setVisible(false);
            return;
        }
        selectedCell = cell;
        selectedSquareHighlight.setVisible(true);
        selectedSquareHighlight.setLayoutX(startX + cell.x() * squareSize);
        selectedSquareHighlight.setLayoutY(startY + cell.y() * squareSize);
    }

    public Cell getSelectedCell() {
        return selectedCell;
    }
}