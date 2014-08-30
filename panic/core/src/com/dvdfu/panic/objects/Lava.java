package com.dvdfu.panic.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.dvdfu.panic.handlers.Consts;

public class Lava extends Solid {
	private FrameBuffer fbo;
	private ShaderProgram fboShader;
	private float heightGoal;

	public Lava() {
		setPosition(0, -Consts.ScreenHeight / 2);
		setSize(Consts.ScreenWidth, Consts.ScreenHeight / 2);
		fbo = new FrameBuffer(Format.RGBA8888, Consts.WindowWidth, Consts.WindowHeight, false);
		fboShader = new ShaderProgram(Gdx.files.internal("data/shader.vert"), Gdx.files.internal("data/shader.frag"));
		heightGoal = 16;
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
