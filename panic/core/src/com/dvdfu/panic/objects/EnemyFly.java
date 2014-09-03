package com.dvdfu.panic.objects;

import com.badlogic.gdx.math.MathUtils;
import com.dvdfu.panic.handlers.Bound;
import com.dvdfu.panic.handlers.Consts;
import com.dvdfu.panic.handlers.Enums.EnemyState;
import com.dvdfu.panic.handlers.GameStage;
import com.dvdfu.panic.visuals.Sprites;

public class EnemyFly extends AbstractEnemy {
	private float xGoal;
	private float yGoal;

	public EnemyFly(GameStage stage) {
		super(stage);
		setSize(24, 22);
		setSprite(Sprites.enemyWalk);
		reset();
	}

	public void update() {
		if (state != EnemyState.GRABBED && state != EnemyState.ACTIVE) {
			ySpeed -= Consts.Gravity;
		}
		if (state == EnemyState.ACTIVE) {
			xSpeed = MathUtils.clamp((xGoal - getX()) / 60, -1, 1);
			ySpeed = MathUtils.clamp((yGoal - getY()) / 60, -1, 1);
		}
		if ((state == EnemyState.THROWN || state == EnemyState.STUNNED) && grounded) {
			brake(0, 0.25f);
			if (state == EnemyState.THROWN && xSpeed == 0) {
				setState(EnemyState.STUNNED);
			}
		}
		if (state != EnemyState.ACTIVE) {
			contain();
		}
		grounded = false;
	}

	public void collideSolid(Floor other) {
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

	public void reset() {
		setState(EnemyState.ACTIVE);
	}
}