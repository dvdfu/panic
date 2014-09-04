package com.dvdfu.panic.objects;

import com.badlogic.gdx.math.MathUtils;
import com.dvdfu.panic.handlers.Enums.EnemyState;
import com.dvdfu.panic.handlers.GameStage;
import com.dvdfu.panic.visuals.Sprites;

public class EnemyJump extends AbstractEnemy {
	private int jumpTimer, jumpTimerMax;
	private int jumpSpeed;

	public EnemyJump(GameStage stage) {
		super(stage);
		jumpTimerMax = 100;
		jumpSpeed = 5;
		moveSpeed = 1.5f;
		setSize(18, 20);
		this.xSprOffset = -3;
		setSprite(Sprites.enemyWalk);
		reset();
	}

	public void update() {
		if (state == EnemyState.ACTIVE) {
			if (jumpTimer > 0) {
				jumpTimer--;
				xSpeed = 0;
			} else if (grounded) {
				ySpeed = jumpSpeed;
				xSpeed = movingRight ? moveSpeed : -moveSpeed;
			}
		}
		contain();
		super.update();
	}

	protected void justLanded() {
		jumpTimer = jumpTimerMax;
		super.justLanded();
	}

	public void setState(EnemyState state) {
		// STATE ENTER
		switch (state) {
		case STUNNED:
			stunnedTimer = 180;
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

	public void reset() {
		jumpTimer = jumpTimerMax;
		setState(EnemyState.ACTIVE);
		movingRight = MathUtils.randomBoolean();
		if (movingRight) {
			xSpeed = moveSpeed;
		} else {
			xSpeed = -moveSpeed;
		}
		ySpeed = 0;
	}
}