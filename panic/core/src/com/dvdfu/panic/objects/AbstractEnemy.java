package com.dvdfu.panic.objects;

import com.badlogic.gdx.scenes.scene2d.Group;

public abstract class AbstractEnemy extends GameObject {
	protected Group solids;
	protected Player player;
	protected enum State {
		ACTIVE, STUNNED, GRABBED, THROWN
	}
	protected State state;
	protected float dx;
	protected float dy;

	public AbstractEnemy() {
		state = State.ACTIVE;
	}
	
	public void act(float delta) {
		x += dx;
		y += dy;
		super.act(delta);
	}

	public void setSolids(Group solids) {
		this.solids = solids;
	}

	public void setPlayer(Player player) {
		this.player = player;
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