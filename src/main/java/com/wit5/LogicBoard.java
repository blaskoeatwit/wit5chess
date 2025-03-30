package com.wit5;
import com.wit5.Pieces.*;
import com.wit5.BoardManager.Cell;

public class LogicBoard {
    // The 2D array representing the board, board[x][y] is the piece at (x, y)
    public Piece[][] board;
    private boolean isWhiteTurn = true;
    public Cell lastMove = null;
    
    public LogicBoard() {
        board = new Piece[8][8];

        // Set up pawns
        for (int i = 0; i < 8; i++) {
            board[i][1] = new Pawn(new Cell(i, 1), false);
            board[i][6] = new Pawn(new Cell(i, 6), true);
        }
        
        // Set up rooks
        board[0][0] = new Rook(new Cell(0, 0), false);
        board[7][0] = new Rook(new Cell(7, 0), false);
        board[0][7] = new Rook(new Cell(0, 7), true);
        board[7][7] = new Rook(new Cell(7, 7), true);
        
        // Set up knights
        board[1][0] = new Knight(new Cell(1, 0), false);
        board[6][0] = new Knight(new Cell(6, 0), false);
        board[1][7] = new Knight(new Cell(1, 7), true);
        board[6][7] = new Knight(new Cell(6, 7), true);
        
        // Set up bishops
        board[2][0] = new Bishop(new Cell(2, 0), false);
        board[5][0] = new Bishop(new Cell(5, 0), false);
        board[2][7] = new Bishop(new Cell(2, 7), true);
        board[5][7] = new Bishop(new Cell(5, 7), true);
        
        // Set up queens
        board[3][0] = new Queen(new Cell(3, 0), false);
        board[3][7] = new Queen(new Cell(3, 7), true);
        
        // Set up kings
        board[4][0] = new King(new Cell(4, 0), false);
        board[4][7] = new King(new Cell(4, 7), true);
    }

    public void nextTurn() { isWhiteTurn = !isWhiteTurn; }
    public boolean isWhiteTurn() { return isWhiteTurn; }
    public Cell lastMove() { return lastMove; }
    
    public Piece getCell(Cell cell) throws IndexOutOfBoundsException {
        return board[cell.x()][cell.y()];
    }
    public Piece setCell(Cell cell, Piece piece) throws IndexOutOfBoundsException {
        Piece oldPiece = board[cell.x()][cell.y()];
        board[cell.x()][cell.y()] = piece;
        return oldPiece;
    }


    // Returns false if move failed, true otherwise
    public boolean attemptMove(Cell curCell, Cell newCell) throws IndexOutOfBoundsException {
        Piece piece = getCell(curCell);
        if (piece == null) return false;
        if (piece.isWhite() != isWhiteTurn) return false;
        if (piece.move(this, newCell)) {
            lastMove = newCell;
            nextTurn();
            return true;
        }
        return false;
    }

    public boolean isValidMove(Cell curCell, Cell newCell) {
        Piece piece = getCell(curCell);
        if (piece == null) return false;
        if (piece.isWhite() != isWhiteTurn()) return false;
        return piece.legalMove(this, newCell);
    }

    // Assumes the path forms a horizontal, vertical, or slope=+/-1 diagonal line
    // (Basically don't call this function when moving the knight)
    public boolean isPathClear(Cell curCell, Cell newCell) {
        int signX = Integer.signum(newCell.x() - curCell.x());
        int signY = Integer.signum(newCell.y() - curCell.y());
        int steps = Math.max(Math.abs(newCell.x() - curCell.x()), Math.abs(newCell.y() - curCell.y()));
        
        for (int i = 1; i < steps; i++) {
            try {
                Cell intermediateCell = new Cell( 
                    curCell.x() + i * signX,
                    curCell.y() + i * signY
                );
                if (getCell(intermediateCell) != null) return false;
            } catch (IndexOutOfBoundsException e) { return false; }
        }
        
        return true;
    }

}
