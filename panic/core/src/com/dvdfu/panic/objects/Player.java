package com.dvdfu.panic.objects;

import com.badlogic.gdx.math.Rectangle;
import com.dvdfu.panic.handlers.Consts;
import com.dvdfu.panic.handlers.Enums.EnemyState;
import com.dvdfu.panic.handlers.Enums.ItemType;
import com.dvdfu.panic.handlers.Input;
import com.dvdfu.panic.visuals.Sprites;

public class Player extends GameObject {
	private AbstractEnemy held;
	private ItemType[] items = { null, null, null };
	private float walkSpeed;
	private float throwSpeed;
	private int jumpTimer;
	private int throwTimer;
	private boolean grounded;
	private boolean facingRight;

	public Player() {
		super();
		setSize(28, 18);
		xSprOffset = -2;
		setSprite(Sprites.player);
		throwSpeed = 12;
		walkSpeed = 6;
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
				dy = 7;
			} else {
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
			jumpTimer = 16;
			dy = 7;
		}
		grounded = false;
	}

	public void act(float delta) {
		move();
		if (throwTimer > 0) {
			throwTimer--;
		}
		if (held != null) {
			if (held.getState() != EnemyState.GRABBED) {
				held = null;
			} else if (!Input.KeyDown(Input.C)) {
				float dyt = dy + 3;
				float dxt = 0;
				if (Input.KeyDown(Input.ARROW_DOWN)) dyt = -throwSpeed;
				if (Input.KeyDown(Input.ARROW_UP)) dyt = throwSpeed;
				if (Input.KeyDown(Input.ARROW_LEFT)) dxt = -throwSpeed;
				if (Input.KeyDown(Input.ARROW_RIGHT)) dxt = throwSpeed;
				if (dyt == dy + 3 && dxt == 0) {
					held.setState(EnemyState.STUNNED);
				} else {
					held.setState(EnemyState.THROWN);
				}
				held.launch(dxt, dyt);
				held = null;
				throwTimer = 10;
			}
		}
		if (getTop() < 0) {
			reset();
		}
		if (getX() > Consts.ScreenWidth) {
			x = 1 - getWidth();
		}
		if (getRight() < 0) {
			x = Consts.ScreenWidth - 1;
		}
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
		Rectangle myRect = bounds.setPosition(x, y + dy);
		if (myRect.overlaps(enemy.bounds)) {
			if (enemy.state == EnemyState.ACTIVE || enemy.state == EnemyState.STUNNED) {
				if (getTop() + dy > enemy.getTop() && bounds.y < enemy.getTop()) {
					enemy.setState(EnemyState.STUNNED);
					enemy.jumpOn();
					if (!Input.KeyDown(Input.C)) {
						dy = 7;
						jumpTimer = 16;
					}
				}
			}
		}
		if (bounds.overlaps(enemy.bounds)) {
			if ((enemy.state == EnemyState.STUNNED || (enemy.state == EnemyState.THROWN && throwTimer == 0)) && Input.KeyDown(Input.C) && held == null) {
				held = enemy;
				enemy.setState(EnemyState.GRABBED);
			} else if (enemy.state == EnemyState.ACTIVE) {
				// PLAYER DIES
				reset();
			}
		}
	}

	public void getItem(ItemType item) {
		if (item == null) {
			return;
		}
		switch (hasItem(item)) {
		case -1:
		case 2:
			items[2] = items[1];
			items[1] = items[0];
			items[0] = item;
			break;
		case 1:
			items[1] = items[0];
			items[0] = item;
			break;
		default:
			break;
		}
	}

	private int hasItem(ItemType item) {
		for (int i = 0; i < 3; i++) {
			if (items[i] == item) return i;
		}
		return -1;
	}

	/*
	 * public void draw(Batch batch, float parentAlpha) { for (int i = 0; i < 3;
	 * i++) { if (items[i] != null) { Label p = new Label(items[i].toString());
	 * p.drawC(batch, x + getWidth() / 2, y + 80 - i * 16); } }
	 * super.draw(batch, parentAlpha); }
	 */

	public void reset() {
		x = (Consts.ScreenWidth - getWidth()) / 2;
		y = Consts.ScreenHeight;
		dy = 0;
		dx = 0;
		facingRight = true;
	}
}