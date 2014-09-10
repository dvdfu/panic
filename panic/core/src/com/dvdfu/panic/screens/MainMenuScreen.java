package com.dvdfu.panic.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dvdfu.panic.MainGame;
import com.dvdfu.panic.visuals.menus.GameMenu;

public class MainMenuScreen extends AbstractScreen {
	private SpriteBatch batch;
	private GameMenu menu;

	public MainMenuScreen(MainGame game) {
		super(game);
		batch = new SpriteBatch();
		menu = new GameMenu(game);
	}

	public void render(float delta) {
		menu.update();
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glClearColor(0.5f, 0.5f, 0.5f, 1);
		batch.begin();
		menu.draw(batch);
		batch.end();
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
		batch.dispose();
	}

}
