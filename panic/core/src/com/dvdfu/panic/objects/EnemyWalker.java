package com.dvdfu.panic.objects;

import com.badlogic.gdx.math.MathUtils;
import com.dvdfu.panic.handlers.Enums.EnemyState;
import com.dvdfu.panic.visuals.Sprites;

public class EnemyWalker extends AbstractEnemy {

	public EnemyWalker() {
		super();
		collidesOthers = true;
		moveSpeed = 0.5f;
		setSize(28, 26);
		xSprOffset = -2;
		setSprite(Sprites.enemyWalk2);
		reset();
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
			xSpeed = movingRight ? moveSpeed : -moveSpeed;
			setSprite(Sprites.enemyWalk2);
			break;
		case STUNNED:
			xSpeed = 0;
			stunnedTimer = 300;
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
		state = EnemyState.ACTIVE;
		movingRight = MathUtils.randomBoolean();
		if (movingRight) {
			xSpeed = moveSpeed;
		} else {
			xSpeed = -moveSpeed;
		}
		ySpeed = 0;
	}
}