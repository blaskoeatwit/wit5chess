package com.wit5;

import javafx.scene.Scene;
import com.wit5.VisualBoard.Cell;
import javafx.scene.paint.Color;

// Will handle all interactions between the javafx visuals and the backend board classes
public class BoardManager {
    private LogicBoard logicBoard;
    public VisualBoard visualBoard;
    
    public BoardManager(Scene scene) {
        logicBoard = new LogicBoard();
        visualBoard = new VisualBoard(scene);
        visualBoard.updatePieceDraws(logicBoard);
    }

    public void selectCell(double sceneX, double sceneY) {
        Cell cell = visualBoard.cellAt(sceneX, sceneY);
        if (cell == null) { return; }
    }
}
