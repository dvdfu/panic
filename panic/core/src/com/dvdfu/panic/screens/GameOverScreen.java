package com.dvdfu.panic.screens;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dvdfu.panic.MainGame;
import com.dvdfu.panic.handlers.Consts;
import com.dvdfu.panic.handlers.Input;
import com.dvdfu.panic.visuals.Label;

public class GameOverScreen extends AbstractScreen {
	private SpriteBatch batch;
	private Label message;

	public GameOverScreen(MainGame game) {
		super(game);
		batch = new SpriteBatch();
		message = new Label("GAME OVER!");
	}

	public void render(float delta) {
		if (Input.KeyPressed(Input.ANY_KEY)) {
			game.changeScreen(new MainMenuScreen(game));
		}
		batch.begin();
		message.drawC(batch, Consts.ScreenWidth / 2, Consts.ScreenHeight / 2);
		batch.end();
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
