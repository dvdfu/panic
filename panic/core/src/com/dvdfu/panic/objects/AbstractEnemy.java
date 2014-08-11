package com.dvdfu.panic.objects;

public abstract class AbstractEnemy extends GameObject {
	public enum State {
		ACTIVE, STUNNED, GRABBED, THROWN, DEAD, REMOVE
	}

	protected State state;

	public AbstractEnemy() {
		state = State.ACTIVE;
	}

	public void act(float delta) {
		super.act(delta);
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public void launch(float dx, float dy) {
		this.dx = dx;
		this.dy = dy;
	}
}