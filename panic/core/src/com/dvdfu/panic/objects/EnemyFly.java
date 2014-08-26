package com.dvdfu.panic.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
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
		sprScale = 2;
		setSize(19 * sprScale, 15 * sprScale);
		xSprOffset = -2;
		setSprite(Sprites.enemyThrow);
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
			setSprite(Sprites.enemyThrow);
			break;
		case STUNNED:
			xSpeed = 0;
			stunnedTimer = 600;
			setSprite(Sprites.enemyThrow);
			break;
		case GRABBED:
			xSpeed = 0;
			ySpeed = 0;
		case THROWN:
			stunnedTimer = 0;
			setSprite(Sprites.enemyThrow);
			break;
		case DEAD:
			setSprite(Sprites.enemyThrow);
			break;
		default:
			setSprite(Sprites.enemyThrow);
			break;
		}
		super.setState(state);
	}
	
	public void setGoal(float x, float y) {
		xGoal = x;
		yGoal = y;
	}

	public void draw(Batch batch, float alpha) {
		batch.setColor(new Color(1, 0, 1, 1));
		super.draw(batch, alpha);
		batch.setColor(new Color(1, 1, 1, 1));
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