/// Javafx functional 
package com.wit5;

public class Pawn extends Piece {
    private boolean firstMove = true;

    public Pawn(int x, int y, boolean white) {
        super("Pawn", x, y, white);
    }

    @Override
    public void movePiece(int newX, int newY) {
        if (isValidMove(newX, newY)) {
            this.x = newX;
            this.y = newY;
            firstMove = false; 
            // ChessBoard.updatePiecePosition(this, newX, newY);
        }
    }

    @Override
    public boolean isValidMove(int newX, int newY) {
        int direction = white ? -1 : 1; 

        if (newX == this.x && newY == this.y + direction) { 
            return true;
        } else if (firstMove && newX == this.x && newY == this.y + 2 * direction) { 
            return true;
        }
        return false;
    }

    //En Passant 
    private boolean canEnPassant(int newX, int newY, Piece[][] board, int lastPawnMoveX, int lastPawnMoveY) {
        if (Math.abs(newX - this.x) != 1 || newY - this.y != (white ? -1 : 1)) return false;
    
        Piece adjacentPawn = board[newX][this.y];
        if (adjacentPawn instanceof Pawn && adjacentPawn.white != this.white) {
            return lastPawnMoveX == newX && lastPawnMoveY == this.y;
        }
        return false;
    }
    
    //Promotion
    private void checkPromotion(Piece[][] board) {
        if ((white && y == 0) || (!white && y == 7)) {
            System.out.println("Pawn Promotion! Choose: Q, R, B, N");
    
         
            board[x][y] = new Queen(x, y, white);
        }
    }
}

