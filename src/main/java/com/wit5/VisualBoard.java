package com.wit5;

import javafx.scene.layout.Pane;
import com.wit5.Pieces.Piece;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class VisualBoard extends Pane {
    record Cell(int x, int y) {}

    private double boardSize;
    private double squareSize;
    private double startX;
    private double startY;
    private Pane pieceImages;
    private Pane highlighted;
    private ImageView boardView;


    public VisualBoard(Scene scene) {
        boardView = new ImageView(new Image("file:src/main/java/Resources/tempTestBoard.png"));
        
        pieceImages = new Pane();
        highlighted = new Pane();
        
        // Add to the scene
        getChildren().add(boardView);
        getChildren().add(highlighted);
        getChildren().add(pieceImages);

        // Set initial position
        resizeBoard(scene.getWidth(), scene.getHeight());
        
        // Set up resize listeners
        scene.widthProperty().addListener((obs, oldVal, newVal) -> {
            resizeBoard(newVal.doubleValue(), scene.getHeight());
        });
        scene.heightProperty().addListener((obs, oldVal, newVal) -> {
            resizeBoard(scene.getWidth(), newVal.doubleValue());
        });
    }

    public void resizeBoard(double width, double height) {
        boardSize = Math.min(width, height) * 0.8;
        squareSize = boardSize / 8;
        startX = (width - boardSize) / 2;
        startY = (height - boardSize) / 2;
        
        // Update the board image size and position
        boardView.setFitWidth(boardSize);
        boardView.setFitHeight(boardSize);
        boardView.setX(startX);
        boardView.setY(startY);

        // Update piece sizes
        // for (Node child : pieceImages.getChildren()) {
        //     child.setLayoutX(startX + (int) child.getLayoutX() * squareSize);
        //     child.setLayoutY(startY + (int) child.getLayoutY() * squareSize);
        //     child.setFitHeight(squareSize);
        // }
        placePieceRenders(new LogicBoard());

        // Update piece renders
        // updatePieceRenders(board);
        
    }

    public void placePieceRenders(LogicBoard board) {
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
                    
                    // Need to add early to get bounds
                    pieceImages.getChildren().add(pieceView);
                    double pieceWidth = pieceView.getBoundsInLocal().getWidth();
                    
                    // Position the piece on the board (centered in the square)
                    pieceView.setLayoutX(startX + x * squareSize + (squareSize - pieceWidth) / 2);
                    pieceView.setLayoutY(startY + y * squareSize);
                }
            }
        }
    }

    // The double position check exists because I was occasionally getting (rounding) errors which led to cells outside the board being selected
    public Cell cellAt(double sceneX, double sceneY) {
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

}