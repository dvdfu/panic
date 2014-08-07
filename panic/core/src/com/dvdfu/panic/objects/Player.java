package com.dvdfu.panic.objects;

import com.badlogic.gdx.math.MathUtils;
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
	private int throwTimer;
	private boolean grounded;
	private boolean facingRight;

	public Player() {
		super();
		setSize(32, 32);
		setSprite(Sprites.atlas.createSprite("player"), 32, 32);
		walkSpeed = 5;
		x = 100;
		y = 300;
		dy = 0;
		dx = 0;
		jumpTimer = 0;
		throwTimer = 0;
		facingRight = true;
	}

	public void setBlocks(Group solids) {
		this.solids = solids;
	}

	public void setEnemies(Group enemies) {
		this.enemies = enemies;
	}

	public void act(float delta) {
		dy -= 0.3f;
		grounded = false;
		if (jumpTimer > 0) {
			jumpTimer--;
			if (Input.KeyDown(Input.Z)) {
				dy = 8;
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
				float dxt = dx * 1.5f;
				float dyt = dy * 1.5f;
				if (Input.KeyDown(Input.ARROW_DOWN)) dyt = -9;
				if (Input.KeyDown(Input.ARROW_UP)) dyt = 9;
				if (Input.KeyDown(Input.ARROW_LEFT)) dxt = -9;
				if (Input.KeyDown(Input.ARROW_RIGHT)) dxt = 9;
				held.toss(dxt, dyt);
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
					throwTimer = 20;
				}
				if (getX() > enemy.getX()
						&& enemy.getState() == AbstractEnemy.State.STUNNED
						&& Input.KeyDown(Input.CTRL)) {
					enemy.setState(AbstractEnemy.State.GRABBED);
					held = enemy;
					throwTimer = 20;
				}
			}
			if (ry.overlaps(ro) && getY() > enemy.getY()
					&& enemy.state == AbstractEnemy.State.ACTIVE) {
				y = enemy.getTop();
				dy = 6;
				jumpTimer = 6;
				enemy.setState(AbstractEnemy.State.STUNNED);
			}
		}
	}
}
