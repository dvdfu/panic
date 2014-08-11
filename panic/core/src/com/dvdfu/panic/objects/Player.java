package com.dvdfu.panic.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.dvdfu.panic.handlers.Input;
import com.dvdfu.panic.objects.AbstractEnemy.State;
import com.dvdfu.panic.visuals.Sprites;

public class Player extends GameObject {
	private AbstractEnemy held;
	private float walkSpeed;
	private int jumpTimer;
	private int throwTimer;
	private boolean grounded;
	private boolean facingRight;

	public Player() {
		super();
		setSize(28, 18);
		xSprOffset = -2;
		setSprite(Sprites.atlas.createSprite("player"), 32, 32);
		walkSpeed = 5;
		reset();
	}

	/*
	 * must have functions called in this order: -move -must change dx/dy -must
	 * change grounded to false -collide -must correct x/y, dx/dy -must correct
	 * grounded -act -must add dx/dy to x/y, must finalize position
	 */

	public void move() {
		if (held != null) {
			if (facingRight) {
				held.x = x + 16;
			} else {
				held.x = x - 16;
			}
			held.y = y + 8;
		}
		dy -= 0.3f;
		if (jumpTimer > 0) {
			jumpTimer--;
			if (Input.KeyDown(Input.Z)) {
				dy = 8;
			}
			if (Input.KeyReleased(Input.Z)) {
				jumpTimer = 0;
			}
		}

		if (Input.KeyDown(Input.ARROW_RIGHT)) {
			if (dx < walkSpeed) {
				dx += 0.5f;
				facingRight = true;
			} else dx = walkSpeed;
		} else if (dx > 0) {
			dx -= 0.5f;
		}
		if (Input.KeyDown(Input.ARROW_LEFT)) {
			if (dx > -walkSpeed) {
				dx -= 0.5f;
				facingRight = false;
			} else dx = -walkSpeed;
		} else if (dx < 0) {
			dx += 0.5f;
		}
		if (dx > -0.5f && dx < 0.5f) {
			dx = 0;
		}
		if (grounded && Input.KeyPressed(Input.Z)) {
			jumpTimer = 12;
			dy = 9;
		}
		grounded = false;
	}

	public void act(float delta) {
		if (held != null) {
			if (held.getState() != AbstractEnemy.State.GRABBED) {
				held = null;
			} else {
				if (throwTimer > 0) {
					throwTimer--;
				}
				if (throwTimer == 0 && !Input.KeyDown(Input.CTRL)) {
					float dyt = dy + 3;
					float dxt = 0;
					if (Input.KeyDown(Input.ARROW_DOWN)) dyt = -12;
					if (Input.KeyDown(Input.ARROW_UP)) dyt = 12;
					if (Input.KeyDown(Input.ARROW_LEFT)) dxt = -9;
					if (Input.KeyDown(Input.ARROW_RIGHT)) dxt = 9;
					if (dyt == dy + 3 && dxt == 0) {
						held.setState(AbstractEnemy.State.STUNNED);
					} else {
						held.setState(AbstractEnemy.State.THROWN);
					}
					held.launch(dxt, dyt);
					held = null;
				}
			}
		}
		super.act(delta);
		if (getTop() < 0) {
			reset();
		}
	}

	public void collideSolid(Solid block) {
		Rectangle myRect = bounds.setPosition(x, y + dy);
		if (myRect.overlaps(block.bounds)) {
			if (getTop() + dy > block.getY() && myRect.y < block.getY()) {
				y = block.getY() - getHeight();
				dy = 0;
				jumpTimer = 0;
			}
			if (getTop() + dy > block.getTop() && myRect.y < block.getTop()) {
				y = block.getTop();
				dy = 0;
				jumpTimer = 0;
				grounded = true;
			}
		}
		myRect.setPosition(x + dx, y);
		if (myRect.overlaps(block.bounds)) {
			if (getRight() + dx > block.getX() && myRect.x < block.getX()) {
				x = block.getX() - getWidth();
				dx = 0;
			}
			if (getRight() + dx > block.getRight() && myRect.x < block.getRight()) {
				x = block.getRight();
				dx = 0;
			}
		}
	}

	public void collideEnemy(AbstractEnemy enemy) {
		Rectangle myRect = bounds.setPosition(x, y + dy);
		if (myRect.overlaps(enemy.bounds)) {
			if (enemy.getState() == AbstractEnemy.State.ACTIVE) {
				if (getTop() + dy > enemy.getTop() && bounds.y < enemy.getTop()) {
					enemy.setState(AbstractEnemy.State.STUNNED);
					dy = 8;
				}
			}
		}
		myRect = bounds.setPosition(x + dx, y);
		if (myRect.overlaps(enemy.bounds)) {
			if (enemy.getState() == AbstractEnemy.State.STUNNED && Input.KeyDown(Input.CTRL) && held == null) {
				held = enemy;
				enemy.setState(AbstractEnemy.State.GRABBED);
			} else if (enemy.getState() == AbstractEnemy.State.ACTIVE) {
				reset();
			}
		}
	}

	public void reset() {
		x = 100;
		y = 300;
		facingRight = true;
	}
}