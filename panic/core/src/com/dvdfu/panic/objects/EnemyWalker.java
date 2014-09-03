package com.dvdfu.panic.objects;

import com.badlogic.gdx.math.MathUtils;
import com.dvdfu.panic.handlers.Enums.EnemyState;
import com.dvdfu.panic.handlers.GameStage;
import com.dvdfu.panic.visuals.Sprites;

public class EnemyWalker extends AbstractEnemy {

	public EnemyWalker(GameStage stage) {
		super(stage);
		collidesOthers = true;
		moveSpeed = 0.5f;
		setSize(24, 22);
		setSprite(Sprites.enemyWalk);
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
		case STUNNED:
			stunnedTimer = 300;
		case GRABBED:
		case THROWN:
		case DEAD:
			setSprite(Sprites.enemyRock);
			break;
		case ACTIVE:
			xSpeed = movingRight ? moveSpeed : -moveSpeed;
		default:
			setSprite(Sprites.enemyWalk);
			break;
		}
		super.setState(state);
	}

	public void reset() {
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