/// Javafx functional 
package com.wit5;

import java.util.Scanner;

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
        }
    }

    @Override
    public boolean isValidMove(int newX, int newY) {
        int direction = isWhite ? -1 : 1; 

        if (newX == this.x && newY == this.y + direction) { 
            return true;
        } else if (firstMove && newX == this.x && newY == this.y + 2 * direction) { 
            return true;
        }
        return false;
    }

    //En Passant 
    private boolean canEnPassant(int newX, int newY, Piece[][] board, int lastPawnMoveX, int lastPawnMoveY) {
        if (Math.abs(newX - this.x) != 1 || newY - this.y != (isWhite ? -1 : 1)) return false;
    
        Piece adjacentPawn = board[newX][this.y];
        // This isn't technically correct, en passant can only happen when the adjacent pawn moved twice on it's first move.
        // Consider replacing firstMove with a move counter?
        if (adjacentPawn instanceof Pawn && adjacentPawn.isWhite != isWhite) {
            return lastPawnMoveX == newX && lastPawnMoveY == this.y;
        }
        return false;
    }
    
    //Promotion
    private void checkPromotion(Piece[][] board) {
        Scanner input = new Scanner(System.in);
        if ((isWhite && y == 0) || (!isWhite && y == 7)) {
            char c;
        	do {
        		System.out.print("Pawn Promotion! Choose: Q, R, B, K: ");
        		c = Character.toUpperCase(input.next().charAt(0));
        	} while (!(c=='Q'||c=='R'||c=='B'||c=='K'));
        	switch (c) {
            case 'Q': board[x][y] = new Queen(x, y, isWhite);
            case 'R': board[x][y] = new Rook(x, y, isWhite);
            case 'B': board[x][y] = new Bishop(x, y, isWhite);
            case 'K': board[x][y] = new Knight(x, y, isWhite);
            
            
            }
    
         
            
        }
    }

}

