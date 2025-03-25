
package com.wit5;
// JavaFX Functional
public class Rook extends Piece {
    public Rook(int x, int y, boolean white) {
        super("Rook", x, y, white);
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
        return newX == this.x || newY == this.y; 
    }
}
