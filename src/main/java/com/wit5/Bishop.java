package com.wit5;
// JavaFX functional 
public class Bishop extends Piece {
    public Bishop(int x, int y, boolean white) {
        super("Bishop", x, y, white);
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
        return Math.abs(newX - this.x) == Math.abs(newY - this.y);
    }
}
