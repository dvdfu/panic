package com.dvdfu.panic.objects;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.dvdfu.panic.handlers.Consts;
import com.dvdfu.panic.handlers.Enums.EnemyState;
import com.dvdfu.panic.visuals.Label;

public abstract class AbstractEnemy extends GameObject {

	protected EnemyState state;
	protected boolean grounded;
	protected boolean movingRight;
	protected int stunnedTimer;
	protected int damagedTimer;
	protected float moveSpeed;
	protected Label stunBar;

	public AbstractEnemy() {
		state = EnemyState.ACTIVE;
		stunBar = new Label("" + stunnedTimer);
	}

	public void move() {
		if (state != EnemyState.GRABBED) {
			ySpeed -= 0.3f;
		}
		if ((state == EnemyState.THROWN || state == EnemyState.STUNNED) && grounded) {
			brake(0, 0.25f);
			if (state == EnemyState.THROWN && xSpeed == 0) {
				setState(EnemyState.STUNNED);
			}
		}
		contain();
		grounded = false;
	}

	protected void brake(float vf, float a) {
		if (xSpeed > vf + a) {
			xSpeed -= a;
		} else if (xSpeed > vf) {
			xSpeed = vf;
		} else if (xSpeed < -vf - a) {
			xSpeed += a;
		} else if (xSpeed < -vf) {
			xSpeed = -vf;
		}
	}

	protected void contain() {
		if (getRight() > Consts.BoundsR) {
			setX(Consts.BoundsR - getWidth());
			xSpeed = -xSpeed;
			movingRight ^= true;
		}
		if (getX() < Consts.BoundsL) {
			setX(Consts.BoundsL);
			xSpeed = -xSpeed;
			movingRight ^= true;
		}
	}

	public void act(float delta) {
		if (state == EnemyState.STUNNED) {
			if (stunnedTimer > 0) {
				stunnedTimer--;
			} else {
				setState(EnemyState.ACTIVE);
			}
		}
		super.act(delta);
	}

	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		if (state == EnemyState.STUNNED) {
			stunBar.setText("" + (stunnedTimer / 60 + 1));
			stunBar.drawC(batch, getX() + getWidth() / 2, getY() + 40);
		}
	}

	public EnemyState getState() {
		return state;
	}

	public void setState(EnemyState state) {
		this.state = state;
	}

	protected void justLanded() {

	}

	public void collideSolid(Solid other) {
		if (state == EnemyState.GRABBED) { return; }
		bounds.setPosition(getX(), getY() + ySpeed);
		if (bounds.overlaps(other.bounds)) {
			if (getTop() + ySpeed > other.getY() && bounds.y < other.getY()) {
				setY(other.getY() - getHeight());
			}
			if (getTop() + ySpeed > other.getTop() && bounds.y < other.getTop()) {
				setY(other.getTop());
				grounded = true;
				justLanded();
			}
			ySpeed = 0;
			if (state == EnemyState.DEAD) {
				setState(EnemyState.REMOVE);
			}
		}
		bounds.setPosition(getX() + xSpeed, getY());
		if (bounds.overlaps(other.bounds)) {
			if (getRight() + xSpeed > other.getX() && bounds.x < other.getX()) {
				setX(other.getX() - getWidth());
			}
			if (getRight() + xSpeed > other.getRight() && bounds.x < other.getRight()) {
				setX(other.getRight());
			}
			xSpeed = -xSpeed / 2;
			movingRight ^= true;
		}
		updateBounds();
	}

	public void collideEnemy(AbstractEnemy other) {
		if (other.state == EnemyState.DEAD || other.state == EnemyState.REMOVE || state == EnemyState.REMOVE) { return; }
		bounds.setPosition(getX() + xSpeed, getY() + ySpeed);
		if (bounds.overlaps(other.bounds)) {
			if (state == EnemyState.THROWN) {
				other.setState(EnemyState.DEAD);
				other.launch(xSpeed / 2, 6);
			} else if (state == EnemyState.GRABBED) {
				other.setState(EnemyState.STUNNED);
				if (xSpeed == 0) {
					if (getX() > other.getX()) {
						other.launch(-1, 6);
					} else {
						other.launch(1, 6);
					}
				} else {
					other.launch(xSpeed / 2, 6);
				}
			}
		}
		bounds.setPosition(getX() + xSpeed, getY());
		if (bounds.overlaps(other.bounds)) {
			if (state == EnemyState.ACTIVE && other.state == EnemyState.STUNNED && grounded) {
				if (getRight() + xSpeed > other.getX() && bounds.x < other.getX()) {
					setX(other.getX() - getWidth());
				}
				if (getRight() + xSpeed > other.getRight() && bounds.x < other.getRight()) {
					setX(other.getRight());
				}
				xSpeed = -xSpeed;
				movingRight ^= true;
			}
		}
		updateBounds();
	}

	public void collidePlayer(Player other) {
	}

	public void launch(float dx, float dy) {
		this.xSpeed = dx;
		this.ySpeed = dy;
	}
}