package com.dvdfu.panic.objects;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.dvdfu.panic.handlers.Consts;
import com.dvdfu.panic.handlers.GameStage;
import com.dvdfu.panic.visuals.Sprites;

public class Lava extends GameObject {
	private float heightGoal;

	public Lava(GameStage stage) {
		super(stage);
		heightGoal = 16;
		setPosition(0, -Consts.ScreenHeight / 2);
		setSize(Consts.ScreenWidth, Consts.ScreenHeight / 2);
		stretched = true;
		setSprite(Sprites.plain);
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

	public void reset() {}

	public void update() {}
}
