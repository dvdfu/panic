package com.dvdfu.panic.handlers;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class GameStage extends Stage {
	private Vector3 camPosition;
	private float camSmoothing;

	public GameStage() {
		super();
		camPosition = new Vector3();
		camSmoothing = 0.05f;
	}

	private void updateCamera() {
		getCamera().position.set(getCamera().position.lerp(camPosition, camSmoothing));
	}

	public void act(float delta) {
		updateCamera();
		super.act(delta);
	}

	public void setCameraFocus(float x, float y) {
		camPosition.set(x, y, 0);
	}

	public void setCameraPosition(float x, float y) {
		getCamera().position.set(x, y, 0);
	}

	public void setCameraSmoothing(float smooth) {
		camSmoothing = smooth;
	}
}
