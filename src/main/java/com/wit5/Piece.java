package com.wit5;

public abstract class Piece {
    protected int x, y; 
    protected boolean isWhite;
    protected String name;

    public Piece(String name, int x, int y, boolean isWhite) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.isWhite = isWhite;
    }

    public abstract void movePiece(int newX, int newY); 
    public abstract boolean isValidMove(int newX, int newY);

    // Ezra thinks it would be a better idea if this method was implemented in the Board class
    // If someone disagrees, lmk and we can figure out what's what
    // public abstract boolean isPathClear(Board board, int newX, int newY);

    public int getX() { return x; }
    public int getY() { return y; }
    public String getName() { return name; }
    public boolean isWhite() { return isWhite; }
    
}

