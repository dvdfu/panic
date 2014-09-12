package com.dvdfu.panic.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.dvdfu.panic.MainGame;
import com.dvdfu.panic.handlers.Consts;
import com.dvdfu.panic.handlers.GameStage;
import com.dvdfu.panic.visuals.Label;
import com.dvdfu.panic.visuals.menus.OptionsMenu;

public class OptionsScreen extends AbstractScreen {
	private GameStage stage;
	private OptionsMenu menu;
	private Label labelResolution;

	public OptionsScreen(MainGame game) {
		super(game);
		menu = new OptionsMenu(game);
		stage = new GameStage();
		stage.addActor(menu);
		stage.setCamPosition(Consts.ScreenWidth / 2, Consts.ScreenHeight / 2);
		labelResolution = new Label("Resolution: ");
		labelResolution.setPosition(60, 160);
		stage.addActor(labelResolution);
	}

	public void render(float delta) {
		menu.update();
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glClearColor(0.5f, 0.5f, 0.5f, 1);
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