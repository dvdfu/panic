package com.dvdfu.panic.objects;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.dvdfu.panic.handlers.Input;
import com.dvdfu.panic.visuals.Sprites;

public class Player extends GameObject {
	private Group solids;
	private Group enemies;
	private AbstractEnemy held;
	private float walkSpeed;
	private float dy;
	private float dx;
	private int jumpTimer;
	private boolean grounded;
	private boolean facingRight;

	public Player() {
		super();
		setSize(64, 64);
		setSprite(Sprites.atlas.createSprite("player"), 32, 32);
		walkSpeed = 2;
		x = 100;
		y = 100;
		dy = 0;
		dx = 0;
		jumpTimer = 0;
		facingRight = true;
	}

	public void setBlocks(Group solids) {
		this.solids = solids;
	}

	public void setEnemies(Group enemies) {
		this.enemies = enemies;
	}

	public void act(float delta) {
		if (y + dy > 0) {
			dy -= 0.3f;
			grounded = false;
		} else {
			dy = 0;
			y = 0;
			grounded = true;
		}
		if (jumpTimer > 0) {
			jumpTimer--;
			if (Input.KeyDown(Input.Z)) {
				dy = 6;
			}
			if (Input.KeyReleased(Input.Z)) {
				jumpTimer = 0;
			}
		} else {
			jumpTimer = 0;
		}
		run();
		collide();
		jump();

		y += dy;
		x += dx;
		setPosition(x, y);

		if (held != null) {
			if (facingRight) {
				held.x = x + 16;
				held.y = y;
			} else {
				held.x = x - 16;
				held.y = y;
			}
			if (!Input.KeyDown(Input.CTRL)) {
				held.toss(dx * 1.5f, 6);
				held = null;
			}
		}

		super.act(delta);
	}

	private void run() {
		if (Input.KeyDown(Input.ARROW_RIGHT)) {
			if (dx < walkSpeed) {
				dx += 0.5f;
				facingRight = true;
			} else
				dx = walkSpeed;
		} else if (dx > 0) {
			dx -= 0.5f;
		}
		if (Input.KeyDown(Input.ARROW_LEFT)) {
			if (dx > -walkSpeed) {
				dx -= 0.5f;
				facingRight = false;
			} else
				dx = -walkSpeed;
		} else if (dx < 0) {
			dx += 0.5f;
		}
		if (dx > -0.5f && dx < 0.5f) {
			dx = 0;
		}
	}

	private void jump() {
		if (grounded && Input.KeyPressed(Input.Z)) {
			jumpTimer = 12;
		}
	}

	private void collide() {
		Rectangle rx = new Rectangle(x + dx, y, getWidth(), getHeight());
		Rectangle ry = new Rectangle(x, y + dy, getWidth(), getHeight());
		for (Actor block : solids.getChildren()) {
			Rectangle ro = new Rectangle(block.getX(), block.getY(),
					block.getWidth(), block.getHeight());
			if (rx.overlaps(ro)) {
				if (getRight() < block.getRight()) {
					x = block.getX() - getWidth();
					dx = 0;
				}
				if (getX() > block.getX()) {
					x = block.getRight();
					dx = 0;
				}
			}
			if (ry.overlaps(ro)) {
				if (getTop() < block.getTop()) {
					y = block.getY() - getHeight();
					dy = 0;
					jumpTimer = 0;
				}
				if (getY() > block.getY()) {
					y = block.getTop();
					dy = 0;
					grounded = true;
				}
			}
		}
		for (Actor actor : enemies.getChildren()) {
			AbstractEnemy enemy = (AbstractEnemy) actor;
			Rectangle ro = new Rectangle(enemy.getX(), enemy.getY(),
					enemy.getWidth(), enemy.getHeight());
			if (rx.overlaps(ro)) {
				if (getRight() < enemy.getRight()
						&& enemy.getState() == AbstractEnemy.State.STUNNED
						&& Input.KeyDown(Input.CTRL)) {
					enemy.setState(AbstractEnemy.State.GRABBED);
					held = enemy;
				}
				if (getX() > enemy.getX()
						&& enemy.getState() == AbstractEnemy.State.STUNNED
						&& Input.KeyDown(Input.CTRL)) {
					enemy.setState(AbstractEnemy.State.GRABBED);
					held = enemy;
				}
			}
			if (ry.overlaps(ro)) {
				if (getY() > enemy.getY()) {
					y = enemy.getTop();
					dy = 6;
					jumpTimer = 6;
					enemy.setState(AbstractEnemy.State.STUNNED);
				}
			}
		}
	}
}
