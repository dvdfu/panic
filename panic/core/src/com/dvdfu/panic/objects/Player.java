package com.dvdfu.panic.objects;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.dvdfu.panic.handlers.Consts;
import com.dvdfu.panic.handlers.Enums.EnemyState;
import com.dvdfu.panic.handlers.Enums.ItemType;
import com.dvdfu.panic.handlers.GameStage;
import com.dvdfu.panic.handlers.Input;
import com.dvdfu.panic.visuals.Label;
import com.dvdfu.panic.visuals.Sprites;

public class Player extends GameObject {
	private AbstractEnemy held;
	private ItemType[] items = { null, null, null };
	private Label[] itemLabels = { new Label(), new Label(), new Label() };
	private boolean grounded, groundedBuffer;
	private boolean facingRight;

	private float walkSpeed;
	private float walkAcceleration;
	private float jumpSpeed;
	private int jumpTimer, jumpTimerMax;
	private int jumps, jumpsMax;
	private float throwSpeed;
	private int throwTimer;
	private int hurtTimer;
	private int health, healthMax;

	public Player(GameStage stage) {
		super(stage);
		xSprOffset = -2;
		setSize(12, 16);
		setSprite(Sprites.player);
		reset();
	}

	private void applyPowerups() {
		// walkSpeed = hasItem(Enums.ItemType.DASH) >= 0 ? 4.5f : 3;
		// walkAcceleration = hasItem(Enums.ItemType.DASH) >= 0 ? 1 : 0.3f;
		// throwSpeed = hasItem(Enums.ItemType.THROW) >= 0 ? 9 : 6;
		// jumpSpeed = hasItem(Enums.ItemType.HIGH_JUMP) >= 0 ? 6 : 4;
	}

	private void holdItem() {
		if (held == null) { return; }
		if (facingRight) {
			held.setX(getRight() - held.getWidth() / 2);
			held.xSpeed = xSpeed;
		} else {
			held.setX(getX() - held.getWidth() / 2);
			held.xSpeed = xSpeed;
		}
		held.setY(getY() + getHeight() / 2);
		if (held.getState() != EnemyState.GRABBED) {
			held = null;
		} else if (!Input.KeyDown(Input.CTRL)) {
			float dyt = ySpeed;
			float dxt = xSpeed;
			if (Input.KeyDown(Input.ARROW_DOWN)) dyt = -throwSpeed;
			if (Input.KeyDown(Input.ARROW_UP)) dyt = throwSpeed;
			if (Input.KeyDown(Input.ARROW_LEFT)) dxt = -throwSpeed;
			if (Input.KeyDown(Input.ARROW_RIGHT)) dxt = throwSpeed;
			held.setState(EnemyState.THROWN);
			held.setVelocity(dxt, dyt);
			held = null;
			throwTimer = 10;
		}
	}

	private void tryJump() {
		ySpeed -= Consts.Gravity;
		if (Input.KeyPressed(Input.Z)) {
			if (grounded) {
				jumpTimer = jumpTimerMax;
				ySpeed = jumpSpeed;

			} else if (jumps > 0) {
				jumpTimer = jumpTimerMax;
				ySpeed = jumpSpeed;
				jumps--;
			}
		} else if (jumpTimer > 0) {
			jumpTimer--;
			if (Input.KeyDown(Input.Z)) {
				ySpeed = jumpSpeed;
			} else {
				jumpTimer = 0;
			}
		}
		groundedBuffer = false;
	}

	private void tryWalk() {
		if (Input.KeyDown(Input.ARROW_RIGHT)) {
			if (xSpeed < walkSpeed) {
				xSpeed += walkAcceleration;
				facingRight = true;
			} else xSpeed = walkSpeed;
		} else if (xSpeed > 0) {
			xSpeed -= walkAcceleration;
		}
		if (Input.KeyDown(Input.ARROW_LEFT)) {
			if (xSpeed > -walkSpeed) {
				xSpeed -= walkAcceleration;
				facingRight = false;
			} else xSpeed = -walkSpeed;
		} else if (xSpeed < 0) {
			xSpeed += walkAcceleration;
		}
		if (xSpeed > -walkAcceleration && xSpeed < walkAcceleration) {
			xSpeed = 0;
		}
	}

	private void contain() {
		if (getRight() > Consts.BoundsR) {
			setX(Consts.BoundsR - getWidth());
			xSpeed = 0;
		}
		if (getX() < Consts.BoundsL) {
			setX(Consts.BoundsL);
			xSpeed = 0;
		}
	}

	/* must have functions called in this order: move -must change dx/dy -must change grounded to false collide -must correct x/y, dx/dy -must correct grounded act -must add dx/dy to x/y -must finalize position */

	public void update() {
		applyPowerups();
		holdItem();
		tryJump();
		tryWalk();
		contain();
	}

	public void act(float delta) {
		grounded = groundedBuffer;
		if (throwTimer > 0) {
			throwTimer--;
		}
		if (hurtTimer > 0) {
			hurtTimer--;
		}
		super.act(delta);
		handleSprite();
	}

	public boolean collideSolid(Floor block) {
		bounds.setPosition(getX() + xSpeed, getY() + ySpeed);
		if (bounds.overlaps(block.bounds)) {
			if (getY() >= block.getTop()) {
				setY(block.getTop());
				groundedBuffer = true;
				jumps = jumpsMax;
				jumpTimer = 0;
				ySpeed = 0;
			}
			if (block.getSolid()) {
				if (getTop() <= block.getY()) {
					setY(block.getY() - getHeight());
					jumpTimer = 0;
					ySpeed = 0;
				}
				if (getX() >= block.getRight()) {
					setX(block.getRight());
					xSpeed = 0;
				} else if (getRight() <= block.getX()) {
					setX(block.getX() - getWidth());
					xSpeed = 0;
				}
			}
			setBounds();
			return true;
		}
		setBounds();
		return false;
	}

	public boolean collideEnemy(AbstractEnemy enemy) {
		if (hurtTimer > 0) { return false; }
		bounds.setPosition(getX() + xSpeed, getY() + ySpeed);
		if (bounds.overlaps(enemy.bounds)) {
			if (getY() > enemy.getY()) {
				if (enemy.state == EnemyState.ACTIVE) {
					enemy.setState(EnemyState.STUNNED);
					setY(enemy.getTop());
					ySpeed = jumpSpeed;
					jumpTimer = jumpTimerMax;
					return false;
				} else if (enemy.state == EnemyState.STUNNED) {
					if (!Input.KeyDown(Input.CTRL) || held != null) {
						enemy.setState(EnemyState.DEAD);
						enemy.setVelocity(xSpeed / 2, -ySpeed * 2 / 3);
						setY(enemy.getTop());
						ySpeed = jumpSpeed;
						jumpTimer = jumpTimerMax;
						return false;
					}
				}
			} else if (Input.KeyDown(Input.CTRL) && held == null && (enemy.state == EnemyState.STUNNED || (enemy.state == EnemyState.THROWN && throwTimer == 0))) {
				held = enemy;
				enemy.setState(EnemyState.GRABBED);
				return false;
			} else if (enemy.state == EnemyState.ACTIVE) { return true; }
		}
		setBounds();
		return false;

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
				itemLabels[i].setText(items[i].toString());
				itemLabels[i].drawC(batch, getX() + getWidth() / 2, getY() + 80 - i * 16);
			}
		}
		if (hurtTimer > 0) {
			batch.setColor(0, 0, 1, 1);
		}
		super.draw(batch, parentAlpha);
		if (hurtTimer > 0) {
			batch.setColor(1, 1, 1, 1);
		}
	}

	public void reset() {
		setX(Consts.ScreenWidth / 2 - getWidth() / 2);
		setY(Consts.F1Height + getHeight());
		ySpeed = 0;
		xSpeed = 0;
		facingRight = true;

		walkSpeed = 2.5f;
		walkAcceleration = 0.3f;
		throwSpeed = 6;
		jumpSpeed = 4;
		jumpsMax = 0;
		jumpTimerMax = 12;
		healthMax = 3;
		health = healthMax;
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

	public boolean isGrounded() {
		return grounded;
	}

	public int getHealth() {
		return health;
	}
}