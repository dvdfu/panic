package com.dvdfu.panic.objects;

import com.dvdfu.panic.visuals.Sprites;

public class Flower extends AbstractEnemy {

	public Flower() {
		super();
		reset();
		setSize(32, 32);
		setSprite(Sprites.enemyWalk);
	}

	public void reset() {}

	public void move() {}

	@Override
	public void collideSolid(Solid solid) {
		// TODO Auto-generated method stub
		
	}

}
