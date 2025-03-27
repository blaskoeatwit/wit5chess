package com.wit5;

public class Board {
    /// The 2D array representing the board, board[x][y] is the piece at (x, y)
    private Piece[][] board;
    private boolean isWhiteTurn = true;
    
    public Board() {
        board = new Piece[8][8];
    }

    /// Initializes the board with pieces (NEED TO DO)
    private void initializeBoard() {
        // TODO: Initialize the board with pieces
    }

    /// Returns true if it is white's turn, false otherwise
    public boolean isWhiteTurn() { return isWhiteTurn; }

    /// Returns the current piece stored in the cell
    /// Assumes valid cell coordinates
    public Piece getCell(int x, int y) throws IndexOutOfBoundsException {
        return board[x][y];
    }

    /// Returns the old piece stored in the cell
    /// Assumes valid cell coordinates
    private Piece setCell(int x, int y, Piece piece) throws IndexOutOfBoundsException {
        Piece oldPiece = board[x][y];
        board[x][y] = piece;
        return oldPiece;
    }


    // Consider replacing the boolean return with a custom error
    /// Returns false if the move failed, true otherwise
    public boolean attemptMove(int x1, int y1, int x2, int y2) {
        if (!isValidMove(x1, y1, x2, y2)) return false;
        makeMove(x1, y1, x2, y2);
        return true;
    }

    /// Returns false if the move is invalid, true otherwise 
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

            // Bound validation technically also occurs here, but if neither getCell throws this won't either
            if (!isPathClear(x1, y1, x2, y2)) return false;
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
        // Storing the piece being captured in case we want to do something with that data in the future
        Piece target = getCell(x2, y2);
        setCell(x1, y1, null);
        setCell(x2, y2, piece);
        // Update turn
        isWhiteTurn = !isWhiteTurn;
    }

}
