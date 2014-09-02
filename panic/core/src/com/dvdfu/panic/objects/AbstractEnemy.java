package com.dvdfu.panic.objects;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.dvdfu.panic.handlers.Bound;
import com.dvdfu.panic.handlers.Consts;
import com.dvdfu.panic.handlers.Enums.EnemyState;
import com.dvdfu.panic.visuals.Sprites;

public abstract class AbstractEnemy extends GameObject {

	protected EnemyState state;
	protected boolean collidesOthers;
	protected boolean grounded;
	protected boolean movingRight;
	protected int stunnedTimer;
	protected int damagedTimer;
	protected float moveSpeed;

	public AbstractEnemy() {
		state = EnemyState.ACTIVE;
	}

	public void update() {
		if (state != EnemyState.GRABBED) {
			ySpeed -= Consts.Gravity;
		}
		if ((state == EnemyState.THROWN || state == EnemyState.STUNNED) && grounded) {
			brake(0, 0.25f);
			if (state == EnemyState.THROWN && xSpeed == 0) {
				setState(EnemyState.STUNNED);
			}
		}
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
		facingRight = xSpeed >= 0;
		if (state == EnemyState.STUNNED) {
			if (stunnedTimer > 0) {
				stunnedTimer--;
			} else {
				setState(EnemyState.ACTIVE);
			}
		}
		contain();
		super.act(delta);
	}

	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		if (state == EnemyState.STUNNED) {
			int numStars = stunnedTimer / 60 + 1;
			for (int i = 0; i < numStars; i++) {
				float theta = i * MathUtils.PI2 / numStars + stunnedTimer / 16f;
				int frame = stunnedTimer / 12 + i;
				batch.draw(Sprites.star.getFrame(frame), getCX() - 4 + 12 * MathUtils.cos(theta),
					getCY() + 4 + 8 * MathUtils.sin(theta));
			}
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

	public void collideSolid(Floor other) {
		if (state == EnemyState.GRABBED) { return; }
		Bound otherBounds = other.bounds;
		bounds.setPosition(getX(), getY() + ySpeed);
		if (bounds.overlaps(otherBounds)) {
			if (bounds.bottomOf(otherBounds)) {
				setY(other.getY() - getHeight());
			} else if (bounds.topOf(otherBounds)) {
				setY(other.getTop());
				grounded = true;
				justLanded();
				if (state == EnemyState.DEAD) {
					setState(EnemyState.REMOVE);
				}
			}
			ySpeed = 0;
		}
		bounds.setPosition(getX() + xSpeed, getY());
		if (bounds.overlaps(otherBounds)) {
			if (bounds.leftOf(otherBounds)) {
				setX(other.getX() - getWidth());
			} else if (bounds.rightOf(otherBounds)) {
				setX(other.getRight());
			}
			xSpeed = -xSpeed;
			movingRight ^= true;
		}
		setBounds();
	}

	public void collideEnemy(AbstractEnemy other) {
		if (other.state == EnemyState.DEAD || other.state == EnemyState.REMOVE || state == EnemyState.REMOVE) { return; }
		Bound otherBounds = other.bounds;
		bounds.setPosition(getX() + xSpeed, getY() + ySpeed);
		if (bounds.overlaps(otherBounds)) {
			if (state == EnemyState.THROWN) {
				other.setState(EnemyState.DEAD);
				other.setVelocity(xSpeed / 2, 6);
			} else if (state == EnemyState.GRABBED) {
				other.setState(EnemyState.STUNNED);
				if (xSpeed == 0) {
					if (getX() > other.getX()) {
						other.setVelocity(-1, 6);
					} else {
						other.setVelocity(1, 6);
					}
				} else {
					other.setVelocity(xSpeed / 2, 6);
				}
			}
		}
		if (state == EnemyState.ACTIVE && collidesOthers && (other.collidesOthers || other.state == EnemyState.STUNNED)) {
			bounds.setPosition(getX(), getY() + ySpeed);
			if (bounds.overlaps(otherBounds)) {
				if (bounds.bottomOf(otherBounds) && !grounded) {
					setY(other.getY() - getHeight());
					if (ySpeed > 0) {
						ySpeed = 0;
					}
				} else if (bounds.topOf(otherBounds)) {
					setY(other.getTop());
					ySpeed = 4;
				}
			}
			bounds.setPosition(getX() + xSpeed, getY());
			if (bounds.overlaps(otherBounds)) {
				if (bounds.leftOf(otherBounds)) {
					setX(other.getX() - getWidth());
				} else if (bounds.rightOf(otherBounds)) {
					setX(other.getRight());
				}
				xSpeed = -xSpeed;
				movingRight ^= true;
			}
		}
		setBounds();
	}
}