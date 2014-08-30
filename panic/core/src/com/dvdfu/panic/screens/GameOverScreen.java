package com.dvdfu.panic.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dvdfu.panic.MainGame;
import com.dvdfu.panic.handlers.Consts;
import com.dvdfu.panic.handlers.Input;
import com.dvdfu.panic.visuals.Label;

public class GameOverScreen extends AbstractScreen {
	private SpriteBatch batch;
	private Label message;
	private boolean rendered;

	public GameOverScreen(MainGame game) {
		super(game);
		batch = new SpriteBatch();
		message = new Label("GAME OVER!");
	}

	public void render(float delta) {
		if (Input.KeyPressed(Input.ANY_KEY)) {
			game.changeScreen(new TestScreen(game));
		}
		// Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		if (!rendered) {
			Gdx.gl.glClearColor(0.5f, 0.5f, 0.5f, 1);
			batch.begin();
			message.drawC(batch, Consts.WindowWidth / 2, Consts.WindowHeight /  2);
			batch.end();
			rendered = true;
		}
	}

	public void resize(int width, int height) {}

	public void show() {}

	public void hide() {}

	public void pause() {}

	public void resume() {}

	public void dispose() {
		batch.dispose();
	}

}
