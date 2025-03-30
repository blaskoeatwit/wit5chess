package com.wit5;

import com.wit5.Pieces.*;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

// Handles all interactions between the javafx visuals and the backend board classes
public class BoardManager {
    // Base colors for highlights
    public static final Color selectionColor = Color.BLACK;
    public static final Color lastMoveColor = Color.BLUE;
    public static final Color validMoveColor = Color.GREEN;
    public static final Color captureColor = Color.RED;
    
    public record Cell(int x, int y) {}
    
    private LogicBoard logicBoard;
    private VisualBoard visualBoard;

    private Cell selectedCell;
    private boolean awaitingPromotion = false;
    private Cell promotionCell = null;
    
    public BoardManager(Pane root, Scene scene) {
        logicBoard = new LogicBoard();
        visualBoard = new VisualBoard(scene);
        visualBoard.updatePieceDraws(logicBoard);
        root.getChildren().add(visualBoard);

        scene.setOnMouseClicked(event -> {
            // If awaiting promotion, check if a promotion piece was clicked
            if (awaitingPromotion) {
                handlePromotionSelection(event.getSceneX(), event.getSceneY());
                return;
            }
            
            // Otherwise handle normal cell selection
            if (selectCell(event.getSceneX(), event.getSceneY())) {
                if (logicBoard.lastMove().y() == 0 || logicBoard.lastMove().y() == 7) {
                    if (logicBoard.getCell(logicBoard.lastMove()) instanceof Pawn) {
                        promotionCell = logicBoard.lastMove();
                        awaitingPromotion = true;
                        showPromotionOptions(promotionCell);
                        return;
                    }
                }
                visualBoard.updatePieceDraws(logicBoard);
            }
        });
    }

    // Returns true if a move was made, false otherwise
    public boolean selectCell(double sceneX, double sceneY) {
        // If awaiting promotion, don't allow any other moves
        if (awaitingPromotion) {
            return false;
        }
        
        visualBoard.removeHighlights();
        Cell cell = visualBoard.cellAt(sceneX, sceneY);

        // If we click the same cell or an invalid cell, deselect
        if (cell == null || cell.equals(selectedCell)) {
            selectedCell = null;
            highlightLast();
            return false;
        } 
        
        // If we click a valid cell and a piece is selected, attempt to move
        if (selectedCell != null && logicBoard.attemptMove(selectedCell, cell)) {
            selectedCell = null;
            highlightLast();
            return true;
        } else { selectedCell = cell; } // Otherwise, select the clicked cell
        
        highlightSelected();
        return false;
    }

    private void highlightLast() {
        if (logicBoard.lastMove != null) {
            visualBoard.highlightCell(logicBoard.lastMove, lastMoveColor);
        }
    }

    private void highlightSelected() {
        boolean highlightLast = true;
        if (selectedCell != null) {
            visualBoard.highlightCell(selectedCell, selectionColor);
            for (int x = 0; x < 8; x++) {
                for (int y = 0; y < 8; y++) {
                    Cell newCell = new Cell(x, y);
                    if (newCell.equals(selectedCell)) continue;
                    if (logicBoard.isValidMove(selectedCell, newCell)) {
                        if (newCell.equals(logicBoard.lastMove)) { highlightLast = false; }
                        if (logicBoard.getCell(newCell) != null) {
                            visualBoard.highlightCell(newCell, captureColor);
                        } else {
                            visualBoard.highlightCell(newCell, validMoveColor);
                        }
                    }
                }
            }
        }
        if (highlightLast) highlightLast();
    }
    
    // Show promotion options above the cell
    private void showPromotionOptions(Cell cell) {
        visualBoard.removeHighlights();
        
        // Get the y position for promotion options (above the cell)
        int promotionY = cell.y() - (logicBoard.getCell(cell).isWhite() ? 1 : -1);
        
        // Show promotion options centered around the pawn's position
        // Two pieces on each side of the center of the promotion square
        visualBoard.showPromotionOption(new Cell(cell.x() - 1, promotionY), "Queen", logicBoard.getCell(cell).isWhite());
        visualBoard.showPromotionOption(new Cell(cell.x(), promotionY), "Rook", logicBoard.getCell(cell).isWhite());
        visualBoard.showPromotionOption(new Cell(cell.x() + 1, promotionY), "Bishop", logicBoard.getCell(cell).isWhite());
        visualBoard.showPromotionOption(new Cell(cell.x() + 2, promotionY), "Knight", logicBoard.getCell(cell).isWhite());
        
        // Highlight the cell being promoted
        visualBoard.highlightCell(cell, selectionColor);
    }
    
    // Handle promotion selection
    private void handlePromotionSelection(double sceneX, double sceneY) {
        Cell clickedCell = visualBoard.cellUnbounded(sceneX, sceneY);
        
        // Get the y position for promotion options (above the cell)
        int promotionY = promotionCell.y() - (logicBoard.getCell(promotionCell).isWhite() ? 1 : -1);
        
        // Check if one of the promotion options was clicked
        if (clickedCell.y() == promotionY) {
            boolean isWhite = logicBoard.getCell(promotionCell).isWhite();
            Piece newPiece = null;
            
            // Determine which piece was selected based on x-coordinate
            if (clickedCell.x() == promotionCell.x() - 1) {
                newPiece = new Queen(promotionCell, isWhite);
            } else if (clickedCell.x() == promotionCell.x()) {
                newPiece = new Rook(promotionCell, isWhite);
            } else if (clickedCell.x() == promotionCell.x() + 1) {
                newPiece = new Bishop(promotionCell, isWhite);
            } else if (clickedCell.x() == promotionCell.x() + 2) {
                newPiece = new Knight(promotionCell, isWhite);
            }
            
            // If a valid piece was selected, replace the pawn
            if (newPiece != null) {
                logicBoard.setCell(promotionCell, newPiece);
                visualBoard.removePromotionOptions();
                visualBoard.updatePieceDraws(logicBoard);
                visualBoard.removeHighlights();
                highlightLast();
                awaitingPromotion = false;
                promotionCell = null;
            }
        }
    }

}
