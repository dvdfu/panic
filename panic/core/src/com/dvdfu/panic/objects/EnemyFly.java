package com.dvdfu.panic.objects;

import com.badlogic.gdx.math.MathUtils;
import com.dvdfu.panic.handlers.Consts;
import com.dvdfu.panic.handlers.Enums.EnemyState;
import com.dvdfu.panic.visuals.Sprites;

public class EnemyFly extends AbstractEnemy {
	private float xGoal;
	private float yGoal;

	public EnemyFly() {
		super();
		stretched = false;
		setSize(28 * sprScale, 26 * sprScale);
		xSprOffset = -2;
		setSprite(Sprites.enemyWalk);
		reset();
	}

	public void move() {
		if (state != EnemyState.GRABBED && state != EnemyState.ACTIVE) {
			ySpeed -= 0.3f;
		}
		if (state == EnemyState.ACTIVE) {
			xSpeed = MathUtils.clamp((xGoal - getX()) / 60, -2.5f, 2.5f);
			ySpeed = MathUtils.clamp((yGoal - getY()) / 60, -2.5f, 2.5f);
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

	public void collideSolid(Solid other) {
		if (state == EnemyState.ACTIVE) { return; }
		super.collideSolid(other);
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
		case ACTIVE:
			setSprite(Sprites.enemyWalk);
			break;
		case STUNNED:
			xSpeed = 0;
			stunnedTimer = 600;
			setSprite(Sprites.enemyWalk);
			break;
		case GRABBED:
			xSpeed = 0;
			ySpeed = 0;
		case THROWN:
			stunnedTimer = 0;
			setSprite(Sprites.enemyWalk);
			break;
		case DEAD:
			setSprite(Sprites.enemyWalk);
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
		state = EnemyState.ACTIVE;
		movingRight = MathUtils.randomBoolean();
		if (movingRight) {
			setX(1 - getWidth() + MathUtils.random(160));
		} else {
			setX(Consts.ScreenWidth - MathUtils.random(160) - 1);
		}
		setY(Consts.ScreenHeight);
	}
}