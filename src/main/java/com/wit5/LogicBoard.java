package com.wit5;
import com.wit5.Pieces.*;
import com.wit5.BoardManager.Cell;

public class LogicBoard {
    // The 2D array representing the board, board[x][y] is the piece at (x, y)
    private Piece[][] board;
    private boolean isWhiteTurn = true;
    private Cell lastMove = null;
    
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

    // Deep copy the board
    public LogicBoard(LogicBoard reference) {
        Piece[][] newBoard = new Piece[8][8];
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                Piece piece = reference.getCell(new Cell(x, y));
                newBoard[x][y] = piece == null ? null : piece.copy();
            }
        }
        this.board = newBoard;
        this.isWhiteTurn = reference.isWhiteTurn();
        this.lastMove = reference.lastMove();
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

    // Alias for the constructor which deep copies the board
    public LogicBoard deepCopy() { return new LogicBoard(this); }

    // Returns false if move failed, true otherwise
    public boolean attemptMove(Cell curCell, Cell newCell) throws IndexOutOfBoundsException {
        if (curCell.equals(newCell)) return false;
        Piece piece = getCell(curCell);
        if (piece == null) return false;
        if (piece.isWhite() != isWhiteTurn()) return false;
        if (moveSelfChecks(curCell, newCell)) return false;
        if (!piece.move(this, newCell)) return false;
        lastMove = newCell;
        nextTurn();
        return true;
    }

    public boolean isValidMove(Cell curCell, Cell newCell) {
        if (curCell.equals(newCell)) return false;
        Piece piece = getCell(curCell);
        if (piece == null) return false;
        if (piece.isWhite() != isWhiteTurn()) return false;
        if (moveSelfChecks(curCell, newCell)) return false;
        return piece.legalMove(this, newCell);
    }

    // Tests whether a path between two cells has pieces in the way
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
    
    private Cell findCurrentKing() {
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                Piece piece = getCell(new Cell(x, y));
                if (piece instanceof King && piece.isWhite() == isWhiteTurn()) {
                    return new Cell(x, y);
                }
            }
        }
        return null;
    }

    // Runtime crash if king is not on board
    // Returns true if the current player is in check
    private boolean inCheck() {
        Cell kingSquare = findCurrentKing();
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                Piece piece = getCell(new Cell(x, y));
                if (piece != null && piece.legalMove(this, kingSquare)) return true;
            }
        }
        return false;
    }

    // Returns true if the current player is in check after moving from curCell to newCell
    public boolean moveSelfChecks(Cell curCell, Cell newCell) {
        LogicBoard future = this.deepCopy();
        Piece piece = future.getCell(curCell);
        if (piece == null) return false;
        piece.move(future, newCell);
        return future.inCheck();
    }

    public boolean isCheckmate() {
        // For each piece on the board
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                Cell curCell = new Cell(x, y);
                Piece piece = getCell(curCell);
                if (piece == null) continue;
                // Which you can control
                if (piece.isWhite() != isWhiteTurn()) continue;
                // Check if there is any move
                for (int xAttempt = 0; xAttempt < 8; xAttempt++) {
                    for (int yAttempt = 0; yAttempt < 8; yAttempt++) {
                        Cell newCell = new Cell(xAttempt, yAttempt);
                        // Which is legal and ends the check
                        if (piece.legalMove(this, newCell) && !moveSelfChecks(curCell, newCell)) return false;
                    }
                }
            }
        }
        return true;
    }

    private int countValidMoves() {
        int count = 0;
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                Cell curCell = new Cell(x, y);
                Piece piece = getCell(curCell);
                if (piece == null) continue;
                if (piece.isWhite() != isWhiteTurn()) continue;
                for (int xAttempt = 0; xAttempt < 8; xAttempt++) {
                    for (int yAttempt = 0; yAttempt < 8; yAttempt++) {
                        Cell newCell = new Cell(xAttempt, yAttempt);
                        if (piece.legalMove(this, newCell) && !moveSelfChecks(curCell, newCell)) count++;
                    }
                }
            }
        }
        return count;
    }

    // Verified by copying stalemate here
    // https://www.chess.com/blog/JCH2021/the-fastest-stalemate-possible-in-chess
    public boolean isStalemate() { return countValidMoves() == 0; }
}
