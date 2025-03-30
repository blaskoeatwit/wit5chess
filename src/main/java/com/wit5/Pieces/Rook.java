package com.wit5.Pieces;
import com.wit5.BoardManager.Cell;
import com.wit5.LogicBoard;

public class Rook extends Piece {
    protected boolean hasMoved = false;
    public Rook(Cell curCell, boolean white) { super("Rook", curCell, white); }

    @Override
    public Piece copy() { return new Rook(this.curCell, this.isWhite); }

    @Override
    public boolean legalMove(LogicBoard board, Cell newCell) throws IndexOutOfBoundsException {
        Piece target = board.getCell(newCell);
        if (target != null && target.isWhite() == this.isWhite()) return false;
        if (!board.isPathClear(curCell, newCell)) return false;

        return newCell.x() == curCell.x() ^ newCell.y() == curCell.y();
    }
}
