package com.dvdfu.panic.objects;

import com.badlogic.gdx.math.Rectangle;
import com.dvdfu.panic.handlers.Input;
import com.dvdfu.panic.visuals.Sprites;

public class Player extends GameObject {
	private AbstractEnemy held;
	private float walkSpeed;
	private int jumpTimer;
	private int throwTimer;
	private boolean grounded;
	private boolean facingRight;
	private boolean disturbed;

	public Player() {
		super();
		setSize(28, 18);
		xSprOffset = -2;
		setSprite(Sprites.atlas.createSprite("player"), 32, 32);
		walkSpeed = 5;
		reset();
	}
	
	/*
	 * must have functions called in this order:
	 * -move
	 *     -must change dx/dy
	 *     -must change grounded to false
	 * -collide
	 *     -must correct x/y, dx/dy
	 *     -must correct grounded
	 * -act
	 *     -must add dx/dy to x/y, must finalize position
	 */

	public void move() {
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

		if (held != null) {
			if (throwTimer > 0) {
				throwTimer--;
			}
			if (facingRight) {
				held.x = x + 16;
				held.y = y;
			} else {
				held.x = x - 16;
				held.y = y;
			}
			if (throwTimer == 0 && !Input.KeyDown(Input.CTRL)) {
				float dxt = 0;
				float dyt = 0;
				if (Input.KeyDown(Input.ARROW_DOWN)) dyt = -9;
				if (Input.KeyDown(Input.ARROW_UP)) dyt = 12;
				if (Input.KeyDown(Input.ARROW_LEFT)) dxt = -9;
				if (Input.KeyDown(Input.ARROW_RIGHT)) dxt = 9;
				held.launch(dxt, dyt);
				held = null;
			}
		}
		grounded = false;
	}

	public void act(float delta) {
		disturbed = dx != 0 || dy != 0;
		super.act(delta);
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
		if (!disturbed) return;
		if (bounds.overlaps(enemy.bounds)) {
			if (enemy.getState() == AbstractEnemy.State.ACTIVE) {
				if (getTop() + dy > enemy.getTop() && bounds.y < enemy.getTop()) {
					enemy.setState(AbstractEnemy.State.STUNNED);
					dy = 8;
				}
			} else if (enemy.getState() == AbstractEnemy.State.STUNNED) {
				if (Input.KeyDown(Input.CTRL) && held == null) {
					held = enemy;
					enemy.setState(AbstractEnemy.State.GRABBED);
				}
			}
		}
	}

	public void reset() {
		x = 100;
		y = 300;
		facingRight = true;
	}
}