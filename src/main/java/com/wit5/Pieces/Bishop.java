package com.wit5.Pieces;
import com.wit5.BoardManager.Cell;
import com.wit5.LogicBoard;

public class Bishop extends Piece {
    public Bishop(Cell curCell, boolean white) { super("Bishop", curCell, white); }

    @Override
    public boolean legalMove(LogicBoard board, Cell newCell) throws IndexOutOfBoundsException {
        Piece target = board.getCell(newCell);
        if (target != null && target.isWhite() == this.isWhite()) return false;
        if (!board.isPathClear(curCell, newCell)) return false;

        return Math.abs(newCell.x() - curCell.x()) == Math.abs(newCell.y() - curCell.y());
    }
}
