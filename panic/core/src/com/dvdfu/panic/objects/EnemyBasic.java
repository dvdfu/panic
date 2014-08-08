package com.dvdfu.panic.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

public class EnemyBasic extends AbstractEnemy {

	public EnemyBasic() {
		super();
		x = MathUtils.random(100, 500);
		y = 300;
		setSize(32, 32);
	}
	
	public void move() {
		if (state != State.GRABBED) {
			dy -= 0.3f;
		}
		if (dx > 0.2f) dx -= 0.2f;
		else if (dx < -0.2f) dx += 0.2f;
		else dx = 0;
	}

	public void act(float delta) {
		super.act(delta);
	}

	public void collideSolid(Solid block) {
		if (state == State.GRABBED) {
			return;
		}
		Rectangle blockRect = block.getBounds();
		Rectangle myRect = new Rectangle(x, y + dy, getWidth(), getHeight());
		if (myRect.overlaps(blockRect)) {
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
		if (myRect.overlaps(blockRect)) {
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

	public void draw(Batch batch, float alpha) {
		batch.end();
		ShapeRenderer shapes = new ShapeRenderer();
		switch (state) {
		case ACTIVE:
			shapes.setColor(new Color(0, 1, 0, 1));
			break;
		case STUNNED:
			shapes.setColor(new Color(1, 1, 0, 1));
			break;
		case GRABBED:
			shapes.setColor(new Color(0, 0, 1, 1));
			break;
		case THROWN:
			shapes.setColor(new Color(1, 0, 0, 1));
			break;
		}
		shapes.begin(ShapeType.Filled);
		shapes.rect(x, y, getWidth(), getHeight());
		shapes.end();
		batch.begin();
	}
}