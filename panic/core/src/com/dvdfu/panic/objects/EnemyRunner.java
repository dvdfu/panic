package com.dvdfu.panic.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.dvdfu.panic.handlers.Consts;
import com.dvdfu.panic.handlers.Enums.EnemyState;
import com.dvdfu.panic.visuals.Sprites;

public class EnemyRunner extends AbstractEnemy {

	public EnemyRunner() {
		super();
		moveSpeed = 4.5f;
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
			super.setState(state);
			break;
		case STUNNED:
			super.setState(EnemyState.DEAD);
		break;
		case DEAD:
			setSprite(Sprites.enemyThrow);
			super.setState(state);
			break;
		default:
			setSprite(Sprites.enemyThrow);
			super.setState(state);
			break;
		}
	}

	public void collidePlayer(Player other) {
		if (state == EnemyState.DEAD) {
			return;
		}
		bounds.setPosition(getX() + xSpeed, getY() + ySpeed);
		if (bounds.overlaps(other.getBounds())){
			setState(EnemyState.REMOVE);
		}
		updateBounds();
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
			xSpeed = moveSpeed;
		} else {
			setX(Consts.ScreenWidth - MathUtils.random(160) - 1);
			xSpeed = -moveSpeed;
		}
		setY(Consts.ScreenHeight);
		ySpeed = 0;
	}
}