package com.dvdfu.panic.objects;

import com.badlogic.gdx.math.MathUtils;
import com.dvdfu.panic.handlers.Consts;
import com.dvdfu.panic.handlers.Enums.EnemyState;
import com.dvdfu.panic.visuals.Sprites;

public class EnemyJump extends AbstractEnemy {
	private boolean jumping;
	private int jumpTimer;
	private int jumpSpeed;

	public EnemyJump() {
		super();
		jumpSpeed = 7;
		moveSpeed = 2;
		setSize(28, 26);
		xSprOffset = -2;
		setSprite(Sprites.enemyWalk2);
		reset();
	}

	public void move() {
		if (state == EnemyState.ACTIVE) {
			if (jumpTimer > 0) {
				jumpTimer--;
				jumping = false;
				xSpeed = 0;
			} else if (grounded) {
				ySpeed = jumpSpeed;
				xSpeed = movingRight ? moveSpeed : -moveSpeed;
				jumping = true;
			}
		}
		if (state != EnemyState.GRABBED) {
			ySpeed -= Consts.Gravity;
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

	protected void justLanded() {
		if (jumping) {
			jumpTimer = 60;
			jumping = false;
		}
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
			setSprite(Sprites.enemyWalk2);
			break;
		case STUNNED:
			ySpeed = 0;
			stunnedTimer = 120;
			setSprite(Sprites.enemyWalk2);
			break;
		case GRABBED:
			xSpeed = 0;
			ySpeed = 0;
		case THROWN:
			stunnedTimer = 0;
			setSprite(Sprites.enemyWalk2);
			break;
		case DEAD:
			setSprite(Sprites.enemyWalk2);
			break;
		default:
			setSprite(Sprites.enemyWalk2);
			break;
		}
		super.setState(state);
	}
	
	public void reset() {
		jumping = true;
		state = EnemyState.ACTIVE;
		movingRight = MathUtils.randomBoolean();
		if (movingRight) {
			setX(1 - getWidth() + MathUtils.random(160));
			xSpeed = moveSpeed;
		} else {
			setX(Consts.ScreenWidth - MathUtils.random(160) - 1);
			xSpeed = -moveSpeed;
		}
		setY(Consts.ScreenHeight);
		ySpeed = 0;
	}
}