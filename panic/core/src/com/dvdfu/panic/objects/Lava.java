package com.dvdfu.panic.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.dvdfu.panic.handlers.Consts;
import com.dvdfu.panic.handlers.GameShader;
import com.dvdfu.panic.handlers.GameStage;
import com.dvdfu.panic.visuals.Sprites;

public class Lava extends GameObject {
	private float heightGoal;
	private GameShader fbShader;
	private GameShader defShader;
	private float waveAngle;
	private float waveAmplitude;

	public Lava(GameStage stage) {
		super(stage);
		heightGoal = 16;
		setPosition(-16, -Consts.ScreenHeight / 2);
		setSize(Consts.ScreenWidth + 32, Consts.ScreenHeight / 2);
		stretched = true;
		setSprite(Sprites.plain);

		fbShader = new GameShader("shaders/wavy.vsh", "shaders/wavy.fsh");
		defShader = new GameShader("shaders/passthrough.vsh", "shaders/passthrough.fsh");
		waveAmplitude = 5;
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
		final float dt = Gdx.graphics.getRawDeltaTime();
		waveAngle += dt * 4;
		while (waveAngle > MathUtils.PI2) {
			waveAngle -= MathUtils.PI2;
		}
		fbShader.begin();
		fbShader.setUniformf("u_resolution", Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		fbShader.setUniformf("waveData", waveAngle, waveAmplitude);
		fbShader.end();

		batch.setShader(fbShader);
		batch.setColor(1, 0, 0, 1);
		super.draw(batch, parentAlpha);
		batch.setColor(1, 1, 1, 1);
		batch.setShader(defShader);
	}

	public void reset() {}

	public void update() {}
}
