package com.dvdfu.panic.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.dvdfu.panic.visuals.Sprites;

public class EnemyBasic extends AbstractEnemy {
	private boolean disturbed;

	public EnemyBasic() {
		super();
		reset();
		setSize(32, 32);
		setSprite(Sprites.atlas.createSprite("plain"), 32, 32);
	}

	public void move() {
		if (state != State.GRABBED) {
			dy -= 0.3f;
		}
	}

	public void act(float delta) {
		disturbed = dx != 0 || dy != 0;
		super.act(delta);
	}

	public void collideSolid(Solid block) {
		if (state == State.GRABBED || !disturbed) { return; }
		Rectangle myRect = bounds.setPosition(x, y + dy);
		if (myRect.overlaps(block.bounds)) {
			if (getTop() + dy > block.getY() && myRect.y < block.getY()) {
				y = block.getY() - getHeight();
				dy = 0;
			}
			if (getTop() + dy > block.getTop() && myRect.y < block.getTop()) {
				y = block.getTop();
				dy = 0;
				if (state == State.THROWN) {
					state = State.STUNNED;
				}
			}
		}
		myRect.setPosition(x + dx, y);
		if (myRect.overlaps(block.bounds)) {
			if (getRight() + dx > block.getX() && myRect.x < block.getX()) {
				x = block.getX() - getWidth();
				dx = 0;
				dy = 0;
			}
			if (getRight() + dx > block.getRight() && myRect.x < block.getRight()) {
				x = block.getRight();
				dx = 0;
				dy = 0;
			}
		}
	}

	public void collideEnemy(AbstractEnemy enemy) {
		if (state == State.GRABBED || state == State.STUNNED || !disturbed) { return; }
		Rectangle myRect = bounds.setPosition(x, y + dy);
		if (myRect.overlaps(enemy.bounds)) {
			if (getTop() + dy > enemy.getY() && myRect.y < enemy.getY()) {
				y = enemy.getY() - getHeight();
				dy = 0;
			}
			if (getTop() + dy > enemy.getTop() && myRect.y < enemy.getTop()) {
				y = enemy.getTop();
				dy = 0;
				if (state == State.THROWN) {
					state = State.STUNNED;
				}
			}
		}
		myRect.setPosition(x + dx, y);
		if (myRect.overlaps(enemy.bounds)) {
			if (getRight() + dx > enemy.getX() && myRect.x < enemy.getX()) {
				x = enemy.getX() - getWidth();
				dx = -dx;
				dy = 0;
			}
			if (getRight() + dx > enemy.getRight() && myRect.x < enemy.getRight()) {
				x = enemy.getRight();
				dx = -dx;
				dy = 0;
			}
		}
	}
	
	public void setState(State state) {
		switch (state) {
		case STUNNED:
			dx = 0;
		break;
		}
		super.setState(state);
	}

	public void draw(Batch batch, float alpha) {
		switch (state) {
		case ACTIVE:
			batch.setColor(new Color(0, 1, 0, 1));
			break;
		case STUNNED:
			batch.setColor(new Color(1, 1, 0, 1));
			break;
		case GRABBED:
			batch.setColor(new Color(0, 0, 1, 1));
			break;
		case THROWN:
			batch.setColor(new Color(1, 0, 0, 1));
			break;
		}
		super.draw(batch, alpha);
		batch.setColor(1, 1, 1, 1);
	}

	public void reset() {
		state = State.ACTIVE;
		if (MathUtils.randomBoolean()) {
			x = -getWidth();
			dx = 1;
		} else {
			x = Gdx.graphics.getWidth();
			dx = -1;
		}
		y = 480 + MathUtils.random(160);
		dy = 0;
	}
}