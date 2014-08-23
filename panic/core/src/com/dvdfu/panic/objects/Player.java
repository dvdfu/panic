package com.dvdfu.panic.objects;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.dvdfu.panic.handlers.Consts;
import com.dvdfu.panic.handlers.Enums.EnemyState;
import com.dvdfu.panic.handlers.Enums.ItemType;
import com.dvdfu.panic.handlers.Input;
import com.dvdfu.panic.visuals.Label;
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
		sprScale = 2;
		xSprOffset = -1 * sprScale;
		setSize(12 * sprScale, 16 * sprScale);
		setSprite(Sprites.player);
		throwSpeed = 12;
		walkSpeed = 6;
		reset();
	}

	/* must have functions called in this order: move -must change dx/dy -must change grounded to false collide -must correct x/y, dx/dy -must correct grounded act -must add dx/dy to x/y -must finalize position */

	public void move() {
		if (held != null) {
			if (facingRight) {
				held.setX(getX() + 16);
				held.xSpeed = xSpeed;
			} else {
				held.setX(getX() - 16);
				held.xSpeed = xSpeed;
			}
			held.setY(getY() + 26);
		}
		if (throwTimer == 0) {
			ySpeed -= 0.3f;
		}
		if (jumpTimer > 0) {
			jumpTimer--;
			if (Input.KeyDown(Input.Z)) {
				ySpeed = 7;
			} else {
				jumpTimer = 0;
			}
		}

		if (Input.KeyDown(Input.ARROW_RIGHT)) {
			if (xSpeed < walkSpeed) {
				xSpeed += 0.5f;
				facingRight = true;
			} else xSpeed = walkSpeed;
		} else if (xSpeed > 0) {
			xSpeed -= 0.5f;
		}
		if (Input.KeyDown(Input.ARROW_LEFT)) {
			if (xSpeed > -walkSpeed) {
				xSpeed -= 0.5f;
				facingRight = false;
			} else xSpeed = -walkSpeed;
		} else if (xSpeed < 0) {
			xSpeed += 0.5f;
		}
		if (xSpeed > -0.5f && xSpeed < 0.5f) {
			xSpeed = 0;
		}
		if (grounded && Input.KeyPressed(Input.Z)) {
			jumpTimer = 16;
			ySpeed = 7;
		}
		if (getTop() < 0) {
			reset();
		}
		grounded = false;
	}

	public void act(float delta) {
		if (throwTimer > 0) {
			throwTimer--;
		}
		if (held != null) {
			if (held.getState() != EnemyState.GRABBED) {
				held = null;
			} else if (!Input.KeyDown(Input.C)) {
				if (held instanceof Flower) {
					if (grounded) {
						held.setY(getY());
						held = null;
						throwTimer = 10;
					}
				} else {
					float dyt = ySpeed + 2;
					float dxt = xSpeed;
					if (Input.KeyDown(Input.ARROW_DOWN)) dyt = -throwSpeed;
					if (Input.KeyDown(Input.ARROW_UP)) dyt = throwSpeed;
					if (Input.KeyDown(Input.ARROW_LEFT)) dxt = -throwSpeed;
					if (Input.KeyDown(Input.ARROW_RIGHT)) dxt = throwSpeed;
					held.setState(EnemyState.THROWN);
					held.launch(dxt, dyt);
					held = null;
					throwTimer = 10;
				}
			}
		}
		super.act(delta);
		handleSprite();
	}

	public boolean collideSolid(Solid block) {
		boolean collided = false;
		bounds.setPosition(getX(), getY() + ySpeed);
		if (bounds.overlaps(block.bounds)) {
			collided = true;
			if (getTop() + ySpeed > block.getY() && bounds.y < block.getY()) {
				setY(block.getY() - getHeight());
			}
			if (getTop() + ySpeed > block.getTop() && bounds.y < block.getTop()) {
				setY(block.getTop());
				grounded = true;
			}
			ySpeed = 0;
			jumpTimer = 0;
		}
		bounds = bounds.setPosition(getX() + xSpeed, getY());
		if (bounds.overlaps(block.bounds)) {
			collided = true;
			if (getRight() + xSpeed > block.getX() && bounds.x < block.getX()) {
				setX(block.getX() - getWidth());
			}
			if (getRight() + xSpeed > block.getRight() && bounds.x < block.getRight()) {
				setX(block.getRight());
			}
			xSpeed = 0;
		}
		updateBounds();
		return collided;
	}

	public void collideEnemy(AbstractEnemy enemy) {
		bounds.setPosition(getX(), getY() + ySpeed);
		if (ySpeed < 0 && bounds.overlaps(enemy.bounds)) {
			if (getTop() + ySpeed > enemy.getTop()) {
				if (enemy.state == EnemyState.ACTIVE) {
					enemy.setState(EnemyState.STUNNED);
					ySpeed = 7;
					jumpTimer = 16;
				} else if (enemy.state == EnemyState.STUNNED) {
					if (!Input.KeyDown(Input.C) || held != null) {
						enemy.setState(EnemyState.DEAD);
						enemy.launch(xSpeed, -ySpeed);
						ySpeed = 7;
						jumpTimer = 16;
					}
				}
			}
		}
		bounds.setPosition(getX() + xSpeed, getY() + ySpeed);
		if (bounds.overlaps(enemy.bounds)) {
			if ((enemy.state == EnemyState.STUNNED || (enemy.state == EnemyState.THROWN && throwTimer == 0))
				&& Input.KeyDown(Input.C) && held == null) {
				held = enemy;
				enemy.setState(EnemyState.GRABBED);
			} else if (enemy.state == EnemyState.ACTIVE) {
				// PLAYER DIES
				reset();
			}
		}
		updateBounds();
	}

	public void collideFlower(Flower flower) {
		if (bounds.overlaps(flower.bounds)) {
			if (Input.KeyDown(Input.C) && held == null) {
				held = flower;
				flower.setState(EnemyState.GRABBED);
			}
		}
	}

	public void getItem(ItemType item) {
		if (item == null) { return; }
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

	public void draw(Batch batch, float parentAlpha) {
		for (int i = 0; i < 3; i++) {
			if (items[i] != null) {
				Label p = new Label(items[i].toString());
				p.drawC(batch, getX() + getWidth() / 2, getY() + 80 - i * 16);
			}
		}
		super.draw(batch, parentAlpha);
	}

	public void reset() {
		setX(Consts.ScreenWidth / 2 - getWidth() / 2);
		setY(Consts.ScreenHeight);
		ySpeed = 0;
		xSpeed = 0;
		facingRight = true;
	}

	private void handleSprite() {
		if (!grounded) {
			setSprite(facingRight ? Sprites.playerJumpR : Sprites.playerJumpL);
		} else if (xSpeed > 0) {
			setSprite(Sprites.playerRunR);
		} else if (xSpeed < 0) {
			setSprite(Sprites.playerRunL);
		} else {
			setSprite(facingRight ? Sprites.playerIdleR : Sprites.playerIdleL);
		}
	}
}