package com.wit5.Pieces;
import com.wit5.LogicBoard;
import com.wit5.BoardManager.Cell;

public class Knight extends Piece {
    public Knight(Cell curCell, boolean white) { super("Knight", curCell, white); }

    @Override
    public boolean legalMove(LogicBoard board, Cell newCell) throws IndexOutOfBoundsException {
        Piece target = board.getCell(newCell);
        if (target != null && target.isWhite() == this.isWhite()) return false;

        int dx = Math.abs(newCell.x() - curCell.x());
        int dy = Math.abs(newCell.y() - curCell.y());
        return (dx == 2 && dy == 1) || (dx == 1 && dy == 2); 
    }

}
