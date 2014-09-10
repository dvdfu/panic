package com.dvdfu.panic.objects;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.dvdfu.panic.handlers.Bound;
import com.dvdfu.panic.handlers.Consts;
import com.dvdfu.panic.handlers.Enums.EnemyState;
import com.dvdfu.panic.handlers.GameStage;
import com.dvdfu.panic.visuals.Sprites;

public abstract class AbstractEnemy extends GameObject {
	protected EnemyState state;
	protected boolean grounded, groundedBuffer;
	protected boolean movingRight;
	protected int stunnedTimer;
	protected int damagedTimer;
	protected float moveSpeed;

	public AbstractEnemy(GameStage stage) {
		super(stage);
		state = EnemyState.ACTIVE;
	}

	public void update() {
		if (state != EnemyState.GRABBED) {
			ySpeed -= Consts.Gravity;
		}
		if ((state == EnemyState.THROWN || state == EnemyState.STUNNED)
				&& grounded) {
			brake(0, 0.25f);
			if (state == EnemyState.THROWN && xSpeed == 0) {
				setState(EnemyState.STUNNED);
			}
		}
		groundedBuffer = false;
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

	final protected void contain() {
		if (getRight() > Consts.BoundsR) {
			setX(Consts.BoundsR - getWidth());
			if (state == EnemyState.THROWN) {
				xSpeed = -xSpeed / 2;
			} else {
				xSpeed = -xSpeed;
			}
			movingRight ^= true;
		}
		if (getX() < Consts.BoundsL) {
			setX(Consts.BoundsL);
			if (state == EnemyState.THROWN) {
				xSpeed = -xSpeed / 2;
			} else {
				xSpeed = -xSpeed;
			}
			movingRight ^= true;
		}
	}

	final public void act(float delta) {
		grounded = groundedBuffer;
		facingRight = xSpeed >= 0;
		switch (state) {
		case STUNNED:
			if (stunnedTimer > 0) {
				stunnedTimer--;
			} else {
				setState(EnemyState.ACTIVE);
			}
			break;
		case THROWN:
			setSpriteSpeed(5);
			break;
		default:
			break;
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
				batch.draw(Sprites.star.getFrame(frame), getCX() - 5 + 12
						* MathUtils.cos(theta),
						getCY() + 5 + 8 * MathUtils.sin(theta));
			}
		}
	}

	public EnemyState getState() {
		return state;
	}

	public void setState(EnemyState state) {
		// STATE EXIT
		switch (this.state) {
		default:
			break;
		}
		// STATE ENTER
		switch (state) {
		case ACTIVE:
			setSpriteSpeed(Consts.SpriteSpeed);
			break;
		case STUNNED:
			ySpeed = 0;
			setSpriteSpeed(0);
			break;
		case GRABBED:
			xSpeed = 0;
			ySpeed = 0;
			stunnedTimer = 0;
			setSpriteSpeed(0);
			break;
		case THROWN:
			stunnedTimer = 0;
			break;
		default:
			break;
		}
		this.state = state;
	}

	protected void justLanded() {
		if (state == EnemyState.THROWN) {
			stage.setCamShake(8);
		}
	}

	public void collideSolid(Floor other) {
		if (state == EnemyState.GRABBED) {
			return;
		}
		bounds.setPosition(getX() + xSpeed, getY() + ySpeed);
		if (bounds.overlaps(other.bounds)) {
			if (getY() >= other.getTop()) {
				setY(other.getTop());
				ySpeed = 0;
				groundedBuffer = true;
				if (!grounded) {
					justLanded();
				}
				if (state == EnemyState.DEAD) {
					setState(EnemyState.REMOVE);
				}
			}
			if (other.getSolid()) {
				if (getTop() <= other.getY()) {
					setY(other.getY() - getHeight());
					ySpeed = 0;
				}
				if (getX() >= other.getRight()) {
					setX(other.getRight());
					xSpeed = state == EnemyState.THROWN ? -xSpeed / 2 : -xSpeed;
					movingRight ^= true;
				} else if (getRight() <= other.getX()) {
					setX(other.getX() - getWidth());
					xSpeed = state == EnemyState.THROWN ? -xSpeed / 2 : -xSpeed;
					movingRight ^= true;
				}
			}
		}
		setBounds();
	}

	public void collideEnemy(AbstractEnemy other) {
		if (other.state == EnemyState.DEAD || other.state == EnemyState.REMOVE
				|| state == EnemyState.REMOVE) {
			return;
		}
		Bound otherBounds = other.bounds;
		bounds.setPosition(getX() + xSpeed, getY() + ySpeed);
		if (bounds.overlaps(otherBounds)) {
			if (state == EnemyState.THROWN) {
				other.setState(EnemyState.DEAD);
				other.setVelocity(xSpeed / 2, 3);
			} else if (state == EnemyState.GRABBED) {
				other.setState(EnemyState.STUNNED);
				if (xSpeed == 0) {
					if (getX() > other.getX()) {
						other.setVelocity(-1, 3);
					} else {
						other.setVelocity(1, 3);
					}
				} else {
					other.setVelocity(xSpeed / 2, 3);
				}
			}
		}
		if (state == EnemyState.ACTIVE
				&& (other.state == EnemyState.STUNNED || other.state == EnemyState.ACTIVE)) {
			bounds.setPosition(getX(), getY() + ySpeed);
			if (bounds.overlaps(otherBounds)) {
				if (bounds.bottomOf(otherBounds) && !grounded) {
					setY(other.getY() - getHeight());
					if (ySpeed > 0) {
						ySpeed = 0;
					}
				} else if (bounds.topOf(otherBounds)) {
					setY(other.getTop());
					ySpeed = 3;
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