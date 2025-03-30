package com.wit5.Pieces;
import com.wit5.BoardManager.Cell;
import com.wit5.LogicBoard;

public class Pawn extends Piece {
    private int forward;
    public Pawn(Cell cell, boolean white) { 
        super("Pawn", cell, white); 
        forward = isWhite ? -1 : 1;
    }

    @Override
    public boolean legalMove(LogicBoard board, Cell newCell) throws IndexOutOfBoundsException {
        Piece target = board.getCell(newCell);
        if (target == null) {
            if (validEnPassant(board, newCell)) return true;
            if (validWalk(newCell)) return true;
            if (moveCount == 0 && validJump(newCell)) return true;
        } else if (target.isWhite() != this.isWhite()) {
            if (validCapture(newCell)) return true;
        }
        return false;
    }

    @Override
    public boolean move(LogicBoard board, Cell newCell) throws IndexOutOfBoundsException {
        if (!legalMove(board, newCell)) return false;
        if (validEnPassant(board, newCell)) board.setCell(board.lastMove(), null);
        board.setCell(newCell, this);
        board.setCell(curCell, null);
        curCell = newCell;
        moveCount++;
        return true;
    }

    private boolean validEnPassant(LogicBoard board, Cell newCell) {
        if (board.lastMove() == null) return false;
        Cell behindLast = new Cell(board.lastMove().x(), board.lastMove().y() + forward);
        if (!validCapture(behindLast)) return false;
        if (!newCell.equals(behindLast)) return false;
        Piece adjacentPawn = board.getCell(board.lastMove());
        return adjacentPawn instanceof Pawn 
            && adjacentPawn.isWhite() != isWhite()
            && adjacentPawn.moveCount == 1
            && (curCell.y() == 3 || curCell.y() == 4);
    }

    // Checks whether the pawn is moving in a valid pattern 
    private boolean validWalk(Cell newCell) {
        return newCell.x() == curCell.x() && newCell.y() == curCell.y() + forward;
    }
    private boolean validJump(Cell newCell) {
        return newCell.x() == curCell.x() && newCell.y() == curCell.y() + 2 * forward;
    }
    private boolean validCapture(Cell newCell) {
        return Math.abs(newCell.x() - curCell.x()) == 1 && newCell.y() == curCell.y() + forward;
    }
    
}

