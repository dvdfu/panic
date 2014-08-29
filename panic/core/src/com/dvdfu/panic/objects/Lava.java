package com.dvdfu.panic.objects;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.dvdfu.panic.handlers.Consts;

public class Lava extends Solid {
	private float heightGoal;

	public Lava() {
		setSize(Consts.ScreenWidth, 0);
		heightGoal = 32;
	}

	public void raise() {
		if (getHeight() <= Consts.F1Height || heightGoal >= Consts.F2Y - 80) {
			heightGoal += 16;
		}
	}

	public void act(float delta) {
		float heightDiff = heightGoal - getHeight();
		if (heightDiff > 0.1f) {
			float heightIncrease = Math.min(heightDiff / 30, 1);
			setHeight(getHeight() + heightIncrease);
		} else {
			setHeight(heightGoal);
		}

		if (getHeight() > Consts.F1Height && heightGoal < Consts.F2Y - 80) {
			if (!Consts.F1) {
				Consts.F1 = true;
			}
			heightGoal++;
		}
	}

	public void draw(Batch batch, float parentAlpha) {
		batch.setColor(1, 0, 0, 1);
		super.draw(batch, parentAlpha);
		batch.setColor(1, 1, 1, 1);
	}
}
