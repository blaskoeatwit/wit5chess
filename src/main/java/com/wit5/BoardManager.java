package com.wit5;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

// Handles all interactions between the javafx visuals and the backend board classes
public class BoardManager {
    record Cell(int x, int y) {}
    
    private LogicBoard logicBoard;
    private VisualBoard visualBoard;

    private Cell selectedCell;
    
    public BoardManager(Pane root, Scene scene) {
        logicBoard = new LogicBoard();
        visualBoard = new VisualBoard(scene);
        visualBoard.updatePieceDraws(logicBoard);
        root.getChildren().add(visualBoard);

        scene.setOnMouseClicked(event -> {
            selectCell(event.getSceneX(), event.getSceneY());
        });
    }

    public void selectCell(double sceneX, double sceneY) {
        visualBoard.removeHighlights();
        Cell cell = visualBoard.cellAt(sceneX, sceneY);

        // If we click the same cell or an invalid cell, deselect
        if (cell == null || cell.equals(selectedCell)) {
            selectedCell = null;
            return;
        } 
        
        // If we click a valid cell and a piece is selected, attempt to move
        if (selectedCell != null && logicBoard.attemptMove(selectedCell.x(), selectedCell.y(), cell.x(), cell.y())) {
            visualBoard.updatePieceDraws(logicBoard);
            selectedCell = null;
        } else { selectedCell = cell; } // Otherwise, select the clicked cell
        
        // Highlight selection
        if (selectedCell != null) {
            visualBoard.highlightCell(selectedCell, Color.RED);
            for (int x = 0; x < 8; x++) {
                for (int y = 0; y < 8; y++) {
                    if (logicBoard.isValidMove(cell.x(), cell.y(), x, y)) {
                        visualBoard.highlightCell(new Cell(x, y), Color.BLUE);
                    }
                }
            }
        }
    }
}
