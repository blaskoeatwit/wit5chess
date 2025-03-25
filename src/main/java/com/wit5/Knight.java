
// JavaFX functional 
package com.wit5;

public class Knight extends Piece {
    public Knight(int x, int y, boolean white) {
        super("Knight", x, y, white);
    }

    @Override
    public void movePiece(int newX, int newY) {
        if (isValidMove(newX, newY)) {
            this.x = newX;
            this.y = newY;
            // ChessBoard.updatePiecePosition(this, newX, newY);
        }
    }

    @Override
    public boolean isValidMove(int newX, int newY) {
        int dx = Math.abs(newX - this.x);
        int dy = Math.abs(newY - this.y);
        return (dx == 2 && dy == 1) || (dx == 1 && dy == 2); 
    }
}
