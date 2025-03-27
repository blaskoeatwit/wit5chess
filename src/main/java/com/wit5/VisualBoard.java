package com.wit5;

import javafx.scene.layout.Pane;
import com.wit5.Pieces.Piece;
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
    private Pane pieceImages;
    private Rectangle selectedSquareHighlight;
    private Cell selectedCell;
    private ImageView boardView;


    public VisualBoard(Scene scene) {
        boardView = new ImageView(new Image("file:src/main/java/Resources/tempTestBoard.png"));
        
        selectedSquareHighlight = new Rectangle();
        selectedSquareHighlight.setVisible(false);
        // Translucent red
        selectedSquareHighlight.setFill(Color.RED.deriveColor(0, 1, 1, 0.5));
        
        pieceImages = new Pane();
        
        // Add to the scene
        getChildren().add(boardView);
        getChildren().add(selectedSquareHighlight);
        getChildren().add(pieceImages);

        // Set initial position
        updateBoard(scene.getWidth(), scene.getHeight());
        
        // Set up resize listeners
        scene.widthProperty().addListener((obs, oldVal, newVal) -> {
            updateBoard(newVal.doubleValue(), scene.getHeight());
        });
        scene.heightProperty().addListener((obs, oldVal, newVal) -> {
            updateBoard(scene.getWidth(), newVal.doubleValue());
        });
    }

    public void updateBoard(double width, double height) {
        boardSize = Math.min(width, height) * 0.8;
        squareSize = boardSize / 8;
        startX = (width - boardSize) / 2;
        startY = (height - boardSize) / 2;
        
        // Update the board image size and position
        boardView.setFitWidth(boardSize);
        boardView.setFitHeight(boardSize);
        boardView.setX(startX);
        boardView.setY(startY);

        // Update piece renders
        updatePieceRenders(new LogicBoard());
        
        // Update highlight size
        selectedSquareHighlight.setWidth(squareSize);
        selectedSquareHighlight.setHeight(squareSize);
        // Only update the highlight position if a cell is selected (otherwise no point)
        if (selectedCell != null) {
            selectedSquareHighlight.setLayoutX(startX + selectedCell.x() * squareSize);
            selectedSquareHighlight.setLayoutY(startY + selectedCell.y() * squareSize);
        }
    }

    public void updatePieceRenders(LogicBoard board) {
        // Remove all pieces from the board
        pieceImages.getChildren().clear();

        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                Piece piece = board.getCell(x, y);
                if (piece != null) {
                    // Create ImageView for the piece
                    ImageView pieceView = new ImageView(piece.getImage());
                    pieceView.preserveRatioProperty().set(true);
                    
                    // Set the size of the piece image
                    pieceView.setFitHeight(squareSize);
                    
                    // Need to add to scene graph temporarily to calculate bounds
                    pieceImages.getChildren().add(pieceView);
                    
                    // Get the actual width after setting the height (preserving ratio)
                    double pieceWidth = pieceView.getBoundsInLocal().getWidth();
                    
                    // Position the piece on the board (centered in the square)
                    // Center horizontally: add half of the remaining space in the square
                    pieceView.setLayoutX(startX + x * squareSize + (squareSize - pieceWidth) / 2);
                    // Center vertically: add half of the remaining space in the square
                    pieceView.setLayoutY(startY + y * squareSize);
                }
            }
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
        if (cell == null || cell.equals(selectedCell)) {
            selectedSquareHighlight.setVisible(false);
            selectedCell = null;
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