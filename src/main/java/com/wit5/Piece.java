package com.wit5;
//Java FX 
public abstract class Piece {
    protected int x, y; 
    protected boolean white;
    protected String name;
    protected boolean hasMoved = false;

    public Piece(String name, int x, int y, boolean white) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.white = white;
    }

    public abstract void movePiece(int newX, int newY); 
    public abstract boolean isValidMove(int newX, int newY); 

    public int getX() { return x; }
    public int getY() { return y; }
    public String getName() { return name; }
    
}

// Move to board
//Sanity Check 
// public boolean isValidMove(int newX, int newY, Piece[][] board) {
 
//     if (newX < 0 || newX > 7 || newY < 0 || newY > 7) return false;
    
  
//     if (board[newX][newY] != null && board[newX][newY].white == this.white) return false;
    
//     return true; 
// }
