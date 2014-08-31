package com.dvdfu.panic.handlers;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class GameStage extends Stage {
	private Vector3 camPosition;
	private float camSmoothing;
	private int camShake;

	public GameStage() {
		super();
		camPosition = new Vector3();
		camSmoothing = 0.05f;
	}

	private void updateCam() {
		float camX = getCamera().position.x + (camPosition.x - getCamera().position.x) * camSmoothing;
		float camY = getCamera().position.y + (camPosition.y - getCamera().position.y) * camSmoothing;
		if (camShake > 0) {
			camX += MathUtils.random(-camShake, camShake);
			camY += MathUtils.random(-camShake, camShake);
			camShake--;
		}
		setCamPosition(camX, camY);
	}

	public void act(float delta) {
		updateCam();
		super.act(delta);
	}

	public void setCamFocus(float x, float y) {
		camPosition.set(x, y, 0);
	}

	public void setCamPosition(float x, float y) {
		camPosition.set(x, y, 0);
		getCamera().position.set(x, y, 0);
	}

	public void setCamSmooth(float smooth) {
		camSmoothing = smooth;
	}

	public void setCamShake(int shake) {
		camShake = shake;
	}

	public float getCamX() {
		return getCamera().position.x;
	}

	public float getCamY() {
		return getCamera().position.y;
	}
}
