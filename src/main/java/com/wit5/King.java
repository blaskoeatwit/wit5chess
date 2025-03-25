

/////// JavaFx Functional
package com.wit5;

public class King extends Piece {
    public King(int x, int y, boolean white) {
        super("King", x, y, white);
    }

    @Override
    public void movePiece(int newX, int newY) {
        if (isValidMove(newX, newY)) {
            this.x = newX;
            this.y = newY;
            //ChessBoard.updatePiecePosition(this, newX, newY);
        }
    }

    @Override
    public boolean isValidMove(int newX, int newY) {
        return Math.abs(newX - this.x) <= 1 && Math.abs(newY - this.y) <= 1; 
    }
    
    // Castling Implementation 
    public boolean canCastle(Piece[][] board, int newX, int newY) {
        if (this.hasMoved || this.y != newY || (newX != 2 && newX != 6)) return false;
    
        int direction = (newX == 2) ? -1 : 1; 
        int rookX = (newX == 2) ? 0 : 7;
        
        Piece rook = board[rookX][y];
        if (!(rook instanceof Rook) || rook.hasMoved) return false;
        
        
        for (int i = this.x + direction; i != rookX; i += direction) {
            if (board[i][y] != null) return false;
        }
        
        return true;
    }
}

