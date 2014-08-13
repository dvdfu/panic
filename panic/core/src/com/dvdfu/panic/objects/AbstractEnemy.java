package com.dvdfu.panic.objects;

import com.dvdfu.panic.handlers.Enums.EnemyState;

public abstract class AbstractEnemy extends GameObject {

	protected EnemyState state;
	protected boolean grounded;
	protected int stunnedTimer;
	protected int damagedTimer;

	public AbstractEnemy() {
		state = EnemyState.ACTIVE;
		stunnedTimer = 0;
		damagedTimer = 0;
	}

	public void act(float delta) {
		super.act(delta);
	}

	public EnemyState getState() {
		return state;
	}

	public void setState(EnemyState state) {
		this.state = state;
	}

	public void launch(float dx, float dy) {
		this.dx = dx;
		this.dy = dy;
	}
	
	public void jumpOn() {
		stunnedTimer = 500;
	}
}