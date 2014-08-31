package com.dvdfu.panic.objects;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.dvdfu.panic.handlers.Consts;

public class Lava extends Floor {
	private float heightGoal;

	public Lava() {
		heightGoal = 16;
		setPosition(0, -Consts.ScreenHeight / 2);
		setSize(Consts.ScreenWidth, Consts.ScreenHeight / 2);
	}

	public void raise() {
		if (getTop() <= Consts.F1Height || heightGoal >= Consts.F2Y - 36) {
			heightGoal += 16;
		}
	}

	public void act(float delta) {
		float heightDiff = heightGoal - getTop();
		if (heightDiff > 0.1f) {
			float heightIncrease = Math.min(heightDiff / 30, 1);
			setHeight(getHeight() + heightIncrease);
		} else {
			setHeight(heightGoal - getY());
		}

		if (getTop() > Consts.F1Height && heightGoal < Consts.F2Y - 36) {
			Consts.F1 = true;
			heightGoal++;
		}
	}

	public void draw(Batch batch, float parentAlpha) {
		batch.setColor(1, 0, 0, 1);
		super.draw(batch, parentAlpha);
		batch.setColor(1, 1, 1, 1);
	}
}
