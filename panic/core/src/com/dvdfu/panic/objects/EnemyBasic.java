package com.dvdfu.panic.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.dvdfu.panic.visuals.Sprites;

public class EnemyBasic extends AbstractEnemy {

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
		super.act(delta);
	}

	public void collideSolid(Solid block) {
		if (state == State.GRABBED || state == State.DEAD) {
			return;
		}
		Rectangle myRect = bounds.setPosition(x, y + dy);
		if (myRect.overlaps(block.bounds)) {
			if (getTop() + dy > block.getY() && myRect.y < block.getY()) {
				y = block.getY() - getHeight();
				if (state == State.THROWN && dx == 0) {
					setState(State.DEAD);
				} else {
					dy = 0;
				}
			}
			if (getTop() + dy > block.getTop() && myRect.y < block.getTop()) {
				y = block.getTop();
				dy = 0;
				if (state == State.THROWN && dx == 0) {
					setState(State.DEAD);
				}
			}
		}
		myRect.setPosition(x + dx, y);
		if (myRect.overlaps(block.bounds)) {
			if (getRight() + dx > block.getX() && myRect.x < block.getX()) {
				x = block.getX() - getWidth();
				if (state == State.THROWN) {
					dx = -dx;
				} else {
					dx = 0;
					dy = 0;
				}
			}
			if (getRight() + dx > block.getRight() && myRect.x < block.getRight()) {
				x = block.getRight();
				if (state == State.THROWN) {
					dx = -dx;
				} else {
					dx = 0;
					dy = 0;
				}
			}
		}
	}

	public void collideEnemy(AbstractEnemy enemy) {
		if (state != State.ACTIVE && state != State.GRABBED) {
			return;
		}
		if (bounds.overlaps(enemy.bounds)) {
			if (enemy.state == State.THROWN || enemy.state == State.GRABBED) {
				setState(State.DEAD);
			}
			if (enemy.state == State.ACTIVE && state == State.GRABBED) {
				enemy.setState(State.DEAD);
				setState(State.DEAD);
			}
			if (enemy.state == State.STUNNED && state == State.ACTIVE) {
				if (dx > 0) {
					x = enemy.getX() - getWidth();
				}
				if (dx < 0) {
					x = enemy.getRight();
				}
				dx = -dx;
			}
		}
	}

	public void setState(State state) {
		switch (state) {
		case STUNNED:
			dx = 0;
			break;
		case DEAD:
			if (dy == 0) dy = 6;
			else dy = -dy;
			dx = -dx;
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
		case DEAD:
			batch.setColor(new Color(1, 0, 1, 1));
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