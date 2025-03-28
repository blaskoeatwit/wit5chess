package com.wit5;

import javafx.scene.layout.Pane;
import com.wit5.Pieces.Piece;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class VisualBoard extends Pane {
    record Cell(int x, int y) {}

    private Pane pieceImages;
    private Pane highlighted;
    private ImageView boardView;

    public VisualBoard(Scene scene, LogicBoard board) {
        // Initialize board image
        boardView = new ImageView(new Image("file:src/main/java/Resources/tempTestBoard.png"));
        boardView.fitWidthProperty().bind(widthProperty());
        boardView.fitHeightProperty().bind(heightProperty());
        
        pieceImages = new Pane();
        highlighted = new Pane();
        
        getChildren().add(boardView);
        getChildren().add(highlighted);
        getChildren().add(pieceImages);

        // Set initial board size and set piece draws
        resizeBoard(scene.getWidth(), scene.getHeight());
        updatePieceDraws(board);
        
        // Set up resize listeners
        scene.widthProperty().addListener((obs, oldVal, newVal) -> {
            resizeBoard(newVal.doubleValue(), scene.getHeight());
        });
        scene.heightProperty().addListener((obs, oldVal, newVal) -> {
            resizeBoard(scene.getWidth(), newVal.doubleValue());
        });
    }

    private void resizeBoard(double width, double height) {
        double boardSize = Math.min(width, height) * 0.8;
        setPrefSize(boardSize, boardSize);
        // Center board on screen
        setLayoutX(width / 2 - boardSize / 2);
        setLayoutY(height / 2 - boardSize / 2);
    }

    public void updatePieceDraws(LogicBoard board) {
        pieceImages.getChildren().clear();
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                Piece piece = board.getCell(x, y);
                if (piece != null) {
                    ImageView pieceView = new ImageView(piece.getImage());
                    pieceView.preserveRatioProperty().set(true);
                    pieceView.fitHeightProperty().bind(heightProperty().divide(8));
                    pieceImages.getChildren().add(pieceView);
                    
                    // Bind piece to cell center
                    final int i = x; final int j = y;
                    pieceView.layoutXProperty().bind(widthProperty().divide(8).multiply(i));
                    pieceView.layoutYProperty().bind(heightProperty().divide(8).multiply(j));
                }
            }
        }
    }

    public Cell cellAt(double sceneX, double sceneY) {
        double startX = getLayoutX();
        double startY = getLayoutY();
        double boardSize = getPrefWidth();
        if (sceneX < startX || sceneX > startX + boardSize || 
            sceneY < startY || sceneY > startY + boardSize) {
            return null;
        }
        double squareSize = boardSize / 8;
        int x = (int)((sceneX - startX) / squareSize);
        int y = (int)((sceneY - startY) / squareSize);
        if (x < 0 || x >= 8 || y < 0 || y >= 8) {
            return null;
        }
        return new Cell(x, y);
    }

}