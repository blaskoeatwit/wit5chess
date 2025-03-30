package com.wit5.Pieces;
import com.wit5.BoardManager.Cell;
import com.wit5.LogicBoard;

// import java.util.Scanner;

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
        if (validEnPassant(board, newCell)) board.setCell(board.lastMove, null);
        board.setCell(newCell, this);
        board.setCell(curCell, null);
        curCell = newCell;
        moveCount++;
        return true;
    }

    private boolean validEnPassant(LogicBoard board, Cell newCell) {
        if (board.lastMove == null) return false;
        Cell behindLast = new Cell(board.lastMove.x(), board.lastMove.y() + forward);
        if (!validCapture(behindLast)) return false;
        if (!newCell.equals(behindLast)) return false;
        Piece adjacentPawn = board.getCell(board.lastMove);
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
    
    // //Promotion
    // private void checkPromotion(Piece[][] board) {
    //     Scanner input = new Scanner(System.in);
    //     if ((isWhite && y == 0) || (!isWhite && y == 7)) {
    //         char c;
    //     	do {
    //     		System.out.print("Pawn Promotion! Choose: Q, R, B, K: ");
    //     		c = Character.toUpperCase(input.next().charAt(0));
    //     	} while (!(c=='Q'||c=='R'||c=='B'||c=='K'));
    //     	switch (c) {
    //             case 'Q': board[x][y] = new Queen(x, y, isWhite);
    //             case 'R': board[x][y] = new Rook(x, y, isWhite);
    //             case 'B': board[x][y] = new Bishop(x, y, isWhite);
    //             case 'K': board[x][y] = new Knight(x, y, isWhite);
    //         }
    //     }
    // }

}

