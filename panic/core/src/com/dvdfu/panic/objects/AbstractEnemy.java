package com.dvdfu.panic.objects;

public abstract class AbstractEnemy extends GameObject {
	public enum State {
		ACTIVE, STUNNED, GRABBED, THROWN
	}
	protected State state;
	protected float dx;
	protected float dy;
	protected boolean collides;

	public AbstractEnemy() {
		state = State.ACTIVE;
		collides = true;
	}
	
	public void act(float delta) {
		x += dx;
		y += dy;
		super.act(delta);
	}

	public State getState() {
		return state;
	}
	
	public void setState(State state) {
		this.state = state;
	}

	public void toss(float dx, float dy) {
		state = State.THROWN;
		this.dx = dx;
		this.dy = dy;
	}
}