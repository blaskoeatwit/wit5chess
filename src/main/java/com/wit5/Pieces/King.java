package com.wit5.Pieces;
import com.wit5.BoardManager.Cell;
import com.wit5.LogicBoard;

public class King extends Piece {

    public King(Cell curCell, boolean white) { super("King", curCell, white); }

    // Returns the cell of the rook used during the castle, or null if castle is invalid
    private Cell castlingRook(LogicBoard board, Cell newCell) { 
        if (!this.hasMoved() && newCell.y() == curCell.y()) {
            int deltaX = newCell.x() - curCell.x();
            Cell rookCell;
            Piece rook;
            if (deltaX == 2) { // Kingside
                rookCell = new Cell(7, curCell.y());
                rook = board.getCell(rookCell);
            } else if (deltaX == -2) { // Queenside
                rookCell = new Cell(0, curCell.y());
                rook = board.getCell(rookCell);
            } else return null;

            if (rook instanceof Rook && !rook.hasMoved() && board.isPathClear(curCell, rookCell)) {
                return rookCell;
            }
        }
        return null;
    }

    @Override
    public boolean legalMove(LogicBoard board, Cell newCell) throws IndexOutOfBoundsException {
        Piece target = board.getCell(newCell);
        if (target != null && target.isWhite() == this.isWhite()) return false;
        if (!board.isPathClear(curCell, newCell)) return false;

        // Castling check
        if (castlingRook(board, newCell) != null) return true;

        // Normal movement
        return (Math.abs(newCell.x() - curCell.x()) <= 1 && Math.abs(newCell.y() - curCell.y()) <= 1);
    }

    @Override
    public boolean move(LogicBoard board, Cell newCell) throws IndexOutOfBoundsException {
        if (!legalMove(board, newCell)) return false;
        Cell rookCell = castlingRook(board, newCell);
        board.setCell(newCell, this);
        board.setCell(curCell, null);
        if (rookCell != null) {
            Cell rookNewCell = new Cell(newCell.x() - Integer.signum(newCell.x() - curCell.x()), newCell.y());
            Piece rook = board.getCell(rookCell);
            board.setCell(rookNewCell, rook);
            rook.moveCount += 1;
            rook.curCell = rookNewCell;
            board.setCell(rookCell, null);
        }
        curCell = newCell;
        moveCount += 1;
        return true;
    }

}

