package com.wit5.Pieces;
import com.wit5.BoardManager.Cell;
import com.wit5.LogicBoard;

public class Queen extends Piece {
    public Queen(Cell curCell, boolean white) { super("Queen", curCell, white); }

    @Override
    public Piece copy() { return new Queen(this.curCell, this.isWhite); }

    @Override
    public boolean legalMove(LogicBoard board, Cell newCell) throws IndexOutOfBoundsException {
        Piece target = board.getCell(newCell);
        if (target != null && target.isWhite() == this.isWhite()) return false;
        if (!board.isPathClear(curCell, newCell)) return false;

        return Math.abs(newCell.x() - curCell.x()) == Math.abs(newCell.y() - curCell.y())
            || newCell.x() == curCell.x() ^ newCell.y() == curCell.y();
    }
}
