package com.wit5;
import javafx.scene.layout.Pane;
import com.wit5.Pieces.Piece;
import com.wit5.BoardManager.Cell;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.text.Font;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class VisualBoard extends Pane {
    private Pane pieceImages = new Pane();
    private Pane highlighted = new Pane();
    private Pane promotionOptions = new Pane();
    private Pane gameOverScreen = new Pane();
    private ImageView boardView;

    public VisualBoard(Scene scene) {
        resizeBoard(scene.getWidth(), scene.getHeight());

        boardView = new ImageView(new Image("file:src/main/java/Resources/Board.png"));
        boardView.fitWidthProperty().bind(widthProperty());
        boardView.fitHeightProperty().bind(heightProperty());
        
        getChildren().add(boardView);
        getChildren().add(highlighted);
        getChildren().add(pieceImages);
        getChildren().add(promotionOptions);
        getChildren().add(gameOverScreen);

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

    public void removeHighlights() { highlighted.getChildren().clear(); }
    
    public void highlightCell(Cell cell, Color color) {
        if (cell == null) { return; }
        final int x = cell.x();
        final int y = cell.y();
        Rectangle rect = new Rectangle();
        rect.widthProperty().bind(widthProperty().divide(8));
        rect.heightProperty().bind(heightProperty().divide(8));
        rect.layoutXProperty().bind(widthProperty().divide(8).multiply(x));
        rect.layoutYProperty().bind(heightProperty().divide(8).multiply(y));
        rect.setFill(color.deriveColor(0, 1, 1, 0.5));
        highlighted.getChildren().add(rect);
    }

    public void removePromotionOptions() { promotionOptions.getChildren().clear(); }
    
    public void showPromotionOption(Cell cell, String pieceName, boolean isWhite) {      
        // Create a background highlight for the promotion option
        Rectangle background = new Rectangle();
        background.widthProperty().bind(widthProperty().divide(8));
        background.heightProperty().bind(heightProperty().divide(8));
        background.layoutXProperty().bind(widthProperty().divide(8).multiply(cell.x()));
        background.layoutYProperty().bind(heightProperty().divide(8).multiply(cell.y()));
        background.setFill(Color.LIGHTGRAY.deriveColor(0, 1, 1, 1.));
        promotionOptions.getChildren().add(background);
        
        // Create the piece image for the promotion option
        String colorPrefix = isWhite ? "White" : "Black";
        String imagePath = "file:src/main/java/Resources/" + colorPrefix + pieceName + ".png";
        ImageView pieceView = new ImageView(new Image(imagePath));
        pieceView.preserveRatioProperty().set(true);
        pieceView.fitHeightProperty().bind(heightProperty().divide(8));
        
        // Bind piece to cell center
        pieceView.layoutXProperty().bind(widthProperty().divide(8).multiply(cell.x()));
        pieceView.layoutYProperty().bind(heightProperty().divide(8).multiply(cell.y()));
        
        promotionOptions.getChildren().add(pieceView);
    }

    public void updatePieceDraws(LogicBoard board) {
        pieceImages.getChildren().clear();
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                Piece piece = board.getCell(new Cell(x, y));
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
        if (sceneX <= startX || sceneX >= startX + boardSize || 
            sceneY <= startY || sceneY >= startY + boardSize) {
            return null;
        }
        Cell cell = cellUnbounded(sceneX, sceneY);
        return cell;
    }

    public Cell cellUnbounded(double sceneX, double sceneY) {
        double startX = getLayoutX();
        double startY = getLayoutY();
        double boardSize = getPrefWidth();
        double squareSize = boardSize / 8;
        int x = (int) Math.floor((sceneX - startX) / squareSize);
        int y = (int) Math.floor((sceneY - startY) / squareSize);
        return new Cell(x, y);
    }
    
    public void showGameOverScreen(boolean isWhiteWinner, boolean isCheckmate) {
        gameOverScreen.getChildren().clear();
        
        // Overlay
        Rectangle overlay = new Rectangle();
        overlay.widthProperty().bind(widthProperty());
        overlay.heightProperty().bind(heightProperty());
        overlay.setFill(Color.BLACK.deriveColor(0, 1, 1, 0.7));
        
        // Aligned word box
        VBox messageBox = new VBox(15);
        messageBox.setAlignment(Pos.CENTER);
        messageBox.layoutXProperty().bind(widthProperty().divide(2).subtract(150));
        messageBox.layoutYProperty().bind(heightProperty().divide(2).subtract(100));
        messageBox.setPrefWidth(300);
        messageBox.setPrefHeight(200);
        
        Label gameOverLabel = new Label("GAME OVER");
        gameOverLabel.setFont(new Font("Arial", 30));
        gameOverLabel.setTextFill(Color.WHITE);
        
        String gameEndText;
        if (isCheckmate) {
            String winner = isWhiteWinner ? "White" : "Black";
            gameEndText = winner + " wins!";
        } else gameEndText = "Stalemate!";
        Label checkmateLabel = new Label(gameEndText);
        checkmateLabel.setFont(new Font("Arial", 24));
        checkmateLabel.setTextFill(isCheckmate ? Color.WHITE : Color.GRAY);
        
        Button newGameButton = new Button("New Game");
        newGameButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 10 20 10 20;");
        newGameButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // Send message to board manager
                if (onNewGameRequest != null) onNewGameRequest.run();
            }
        });
        
        messageBox.getChildren().addAll(gameOverLabel, checkmateLabel,  newGameButton);
        
        gameOverScreen.getChildren().addAll(overlay, messageBox);
    }
    
    private Runnable onNewGameRequest;
    public void setOnNewGameRequest(Runnable callback) { this.onNewGameRequest = callback; }
    public void removeGameOverScreen() { gameOverScreen.getChildren().clear(); }
    
}