package com.wit5;

public abstract class Piece {
	
	final private String name;
	private int x,y;
	final private boolean white;

	public Piece(String name, int x, int y, boolean white) {
		this.name=name;
		this.x=x;
		this.y=y;
		this.white=white;
		
	}
	
	public int getX() {
		return this.x;
	}
	
	public int getY() {
		return this.y;
	}
	
	final public boolean getTeam() {
		return this.white;
	}
	
	public void setX(int x) {
		this.x=x;
	}
	
	public void setY(int y) {
		this.y=y;
	}
	
	public void setPos(int x, int y) {
		setX(x);
		setY(y);
	}
	
	public abstract void movePiece();

}
