package com.wit5.Pieces;
import com.wit5.BoardManager.Cell;
import com.wit5.LogicBoard;

public abstract class Piece {
    protected boolean isWhite;
    protected int moveCount = 0;
    protected String name;
    protected Cell curCell;

    public Piece(String name, Cell curCell, boolean isWhite) {
        this.name = name;
        this.curCell = curCell;
        this.isWhite = isWhite;
    }
    public String getName() { return name; }
    public boolean isWhite() { return isWhite; }
    public boolean hasMoved() { return moveCount > 0; }

    public abstract boolean legalMove(LogicBoard board, Cell newCell) throws IndexOutOfBoundsException;

    public boolean move(LogicBoard board, Cell newCell) throws IndexOutOfBoundsException {
        if (!legalMove(board, newCell)) return false;
        board.setCell(newCell, this);
        board.setCell(curCell, null);
        curCell = newCell;
        moveCount++;
        return true;
    }

    
    public javafx.scene.image.Image getImage() {
        String colorPrefix = isWhite ? "White" : "Black";
        String imagePath = "file:src/main/java/Resources/" + colorPrefix + name + ".png";
        return new javafx.scene.image.Image(imagePath);
    }
}
