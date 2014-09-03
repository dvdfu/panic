package com.dvdfu.panic.objects;

import com.badlogic.gdx.math.MathUtils;
import com.dvdfu.panic.handlers.Bound;
import com.dvdfu.panic.handlers.Enums.EnemyState;
import com.dvdfu.panic.handlers.GameStage;
import com.dvdfu.panic.visuals.Sprites;

public class EnemyFly extends AbstractEnemy {
	private float xGoal;
	private float yGoal;

	public EnemyFly(GameStage stage) {
		super(stage);
		setSize(18, 20);
		this.xSprOffset = -3;
		setSprite(Sprites.enemyWalk);
		reset();
	}

	public void update() {
		if (state == EnemyState.ACTIVE) {
			if (xSpeed < MathUtils.clamp((xGoal - getX()) / 60, -1, 1)) {
				xSpeed += 0.1f;
			} else {
				xSpeed -= 0.1f;
			}
			if (ySpeed < MathUtils.clamp((yGoal - getY()) / 60, -1, 1)) {
				ySpeed += 0.1f;
			} else {
				ySpeed -= 0.1f;
			}
		}
		if ((state == EnemyState.THROWN || state == EnemyState.STUNNED) && grounded) {
			brake(0, 0.25f);
			if (state == EnemyState.THROWN && xSpeed == 0) {
				setState(EnemyState.STUNNED);
			}
		}
	}

	public void collideSolid(Floor other) {
		if (state == EnemyState.GRABBED) { return; }
		Bound otherBounds = other.bounds;
		bounds.setPosition(getX(), getY() + ySpeed);
		if (bounds.overlaps(otherBounds)) {
			if (bounds.bottomOf(otherBounds)) {
				setY(other.getY() - getHeight());
				bump(-xSpeed, -ySpeed);
			} else if (bounds.topOf(otherBounds)) {
				setY(other.getTop());
				bump(-xSpeed, -ySpeed);
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
		}
		setBounds();
	}

	public void setState(EnemyState state) {
		// STATE EXIT
		switch (this.state) {
		case ACTIVE:
			xSpeed = 0;
			ySpeed = 0;
		default:
			break;
		}
		// STATE ENTER
		switch (state) {
		case STUNNED:
			stunnedTimer = 300;
		case GRABBED:
		case THROWN:
		case DEAD:
			setSprite(Sprites.enemyRock);
			break;
		default:
			setSprite(Sprites.enemyWalk);
			break;
		}
		super.setState(state);
	}

	public void setGoal(float x, float y) {
		xGoal = x;
		yGoal = y;
	}
	
	private void bump(float x, float y) {
		xSpeed += x;
		ySpeed += y;
	}

	public void reset() {
		setState(EnemyState.ACTIVE);
	}
}