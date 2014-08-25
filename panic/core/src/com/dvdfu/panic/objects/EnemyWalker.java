package com.dvdfu.panic.objects;

import com.badlogic.gdx.math.MathUtils;
import com.dvdfu.panic.handlers.Consts;
import com.dvdfu.panic.handlers.Enums.EnemyState;
import com.dvdfu.panic.visuals.Sprites;

public class EnemyWalker extends AbstractEnemy {

	public EnemyWalker() {
		super();
		moveSpeed = 1;
		stretched = false;
		sprScale = 2;
		setSize(19 * sprScale, 15 * sprScale);
		xSprOffset = -2;
		setSprite(Sprites.enemyThrow);
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
			setSprite(Sprites.enemyThrow);
			break;
		case STUNNED:
			xSpeed = 0;
			stunnedTimer = 180;
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

	// public void draw(Batch batch, float alpha) {
	// switch (state) {
	// case ACTIVE:
	// batch.setColor(new Color(1, 0, 0, 1));
	// break;
	// case STUNNED:
	// batch.setColor(new Color(1, 1, 0, 1));
	// break;
	// case GRABBED:
	// batch.setColor(new Color(0, 0, 1, 1));
	// break;
	// case THROWN:
	// batch.setColor(new Color(0, 1, 0, 1));
	// break;
	// case DEAD:
	// batch.setColor(new Color(1, 0, 1, 1));
	// break;
	// default:
	// break;
	// }
	// super.draw(batch, alpha);
	// batch.setColor(new Color(1, 1, 1, 1));
	// }

	public void reset() {
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