package com.dvdfu.panic.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class EnemyBasic extends AbstractEnemy {
	private boolean grounded;
	public EnemyBasic() {
		super();
		x = 60;
		y = 300;
		setSize(32, 32);
		// setSprite(Sprites.atlas.createSprite("plain"), 32, 32);
	}

	public void act(float delta) {
		collide();
		if (grounded && state == State.THROWN) {
			state = State.STUNNED;
		}
		if (dx > 0.2f) dx -= 0.2f;
		else if (dx < -0.2f) dx += 0.2f;
		else dx = 0;
		super.act(delta);
	}
	
	private void collide() {
		dy -= 0.3f;
		grounded = false;
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