

// JavaFX Functional 
package com.wit5.Pieces;

public class Queen extends Piece {
    public Queen(int x, int y, boolean white) {
        super("Queen", x, y, white);
    }

    @Override
    public void movePiece(int newX, int newY) {
        if (isValidMove(newX, newY)) {
            this.x = newX;
            this.y = newY;
        }
    }

    @Override
    public boolean isValidMove(int newX, int newY) {
        return newX == this.x || newY == this.y || // Moves like Rook
               Math.abs(newX - this.x) == Math.abs(newY - this.y); // Moves like Bishop
    }
}
