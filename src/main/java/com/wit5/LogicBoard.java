package com.wit5;
import com.wit5.Pieces.*;

public class LogicBoard {
    /// The 2D array representing the board, board[x][y] is the piece at (x, y)
    private Piece[][] board;
    private boolean isWhiteTurn = true;
    
    public LogicBoard() {
        board = new Piece[8][8];

        // Set up pawns
        for (int i = 0; i < 8; i++) {
            board[i][1] = new Pawn(i, 1, false); // Black pawns
            board[i][6] = new Pawn(i, 6, true);  // White pawns
        }
        
        // Set up rooks
        board[0][0] = new Rook(0, 0, false);
        board[7][0] = new Rook(7, 0, false);
        board[0][7] = new Rook(0, 7, true);
        board[7][7] = new Rook(7, 7, true);
        
        // Set up knights
        board[1][0] = new Knight(1, 0, false);
        board[6][0] = new Knight(6, 0, false);
        board[1][7] = new Knight(1, 7, true);
        board[6][7] = new Knight(6, 7, true);
        
        // Set up bishops
        board[2][0] = new Bishop(2, 0, false);
        board[5][0] = new Bishop(5, 0, false);
        board[2][7] = new Bishop(2, 7, true);
        board[5][7] = new Bishop(5, 7, true);
        
        // Set up queens
        board[3][0] = new Queen(3, 0, false);
        board[3][7] = new Queen(3, 7, true);
        
        // Set up kings
        board[4][0] = new King(4, 0, false);
        board[4][7] = new King(4, 7, true);
    }

    public boolean isWhiteTurn() { return isWhiteTurn; }

    public Piece getCell(int x, int y) throws IndexOutOfBoundsException {
        return board[x][y];
    }
    private Piece setCell(int x, int y, Piece piece) throws IndexOutOfBoundsException {
        Piece oldPiece = board[x][y];
        board[x][y] = piece;
        return oldPiece;
    }

    /// Returns false if move failed, true otherwise
    public boolean attemptMove(int x1, int y1, int x2, int y2) {
        if (!isValidMove(x1, y1, x2, y2)) return false;
        makeMove(x1, y1, x2, y2);
        return true;
    }

    public boolean isValidMove(int x1, int y1, int x2, int y2) {
        try {
            // Bound validation occurs through the thrown error
            Piece piece = getCell(x1, y1);
            if (piece == null) return false;
            if (piece.isWhite() != isWhiteTurn) return false;
            if (!piece.isValidMove(x2, y2)) return false;

            // Bound validation occurs through the thrown error
            Piece target_square = getCell(x2, y2);
            if (target_square != null && target_square.isWhite() == piece.isWhite()) return false;

            if (!(piece instanceof Knight) && !isPathClear(x1, y1, x2, y2)) return false;
        } catch (IndexOutOfBoundsException e) { return false; }

        return true;
    }

    /// Assumes the path forms a horizontal, vertical, or slope=+/-1 diagonal line
    /// (Basically don't call this function when moving the knight)
    private boolean isPathClear(int x1, int y1, int x2, int y2) throws IndexOutOfBoundsException {
        int steps = Math.max(Math.abs(x2 - x1), Math.abs(y2 - y1));
        for (int i = 1; i < steps; i++) {
            try {
                if (getCell(x1 + i * (x2 - x1) / steps, y1 + i * (y2 - y1) / steps) != null) {
                    return false;
                }
            } catch (IndexOutOfBoundsException e) { return false; }
        }
        return true;
    }

    /// THE FOLLOWING LOGIC DOES NOT WORK FOR EN PASSANT OR CASTLING!!!
    /// THIS FUNCTION MUST BE UPDATED
    /// Assumes move was already validated.
    private void makeMove(int x1, int y1, int x2, int y2) {
        Piece piece = getCell(x1, y1);
        piece.movePiece(x2, y2);
        // Storing the piece being captured in case we want to do something with that data in the future
        Piece target = getCell(x2, y2);
        setCell(x1, y1, null);
        setCell(x2, y2, piece);
        // Update turn
        isWhiteTurn = !isWhiteTurn;
    }

}
