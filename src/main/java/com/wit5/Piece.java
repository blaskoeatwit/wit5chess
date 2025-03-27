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

    public int getX() { return x; }
    public int getY() { return y; }
    public String getName() { return name; }
    public boolean isWhite() { return isWhite; }
    
    public javafx.scene.image.Image getImage() {
        String colorPrefix = isWhite ? "White" : "Black";
        String imagePath = "file:src/main/java/Resources/" + colorPrefix + name + ".png.png";
        return new javafx.scene.image.Image(imagePath);
    }
}
