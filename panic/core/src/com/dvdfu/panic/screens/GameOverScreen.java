package com.dvdfu.panic.screens;

import com.dvdfu.panic.MainGame;
import com.dvdfu.panic.handlers.Consts;
import com.dvdfu.panic.handlers.GameStage;
import com.dvdfu.panic.handlers.Input;
import com.dvdfu.panic.visuals.Label;

public class GameOverScreen extends AbstractScreen {
	private GameStage stage;
	private Label message;

	public GameOverScreen(MainGame game) {
		super(game);
		message = new Label("GAME OVER!");
		message.setDrawCentered(true);
		message.setPosition(Consts.ScreenWidth / 2, Consts.ScreenHeight / 2);
		stage = new GameStage();
		stage.addActor(message);
		stage.setCamPosition(Consts.ScreenWidth / 2, Consts.ScreenHeight / 2);
	}

	public void render(float delta) {
		if (Input.KeyPressed(Input.ANY_KEY)) {
			game.changeScreen(new MainMenuScreen(game));
		}
		stage.draw();
	}

	public void resize(int width, int height) {
	}

	public void show() {
	}

	public void hide() {
	}

	public void pause() {
	}

	public void resume() {
	}

	public void dispose() {
		stage.dispose();
	}
}