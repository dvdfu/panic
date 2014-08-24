package com.dvdfu.panic.objects;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.dvdfu.panic.handlers.Consts;

public class Floor extends Solid {
	private float heightGoal;

	public Floor() {
		setSize(Consts.ScreenWidth, 0);
		heightGoal = 32;
	}

	public void raise() {
		heightGoal += 32;
	}

	public void act(float delta) {
		float heightDiff = heightGoal - getHeight();
		if (heightDiff > 0.1f) {
			setHeight(getHeight() + heightDiff / 30);
		} else {
			setHeight(heightGoal);
		}
		
		if (getHeight() > Consts.F1Height && heightGoal < Consts.F2Y - 80) {
			heightGoal++;
		}
	}

	public void draw(Batch batch, float parentAlpha) {
		batch.setColor(1, 0, 0, 1);
		super.draw(batch, parentAlpha);
		batch.setColor(1, 1, 1, 1);
	}
}
