package com.dvdfu.panic.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.dvdfu.panic.MainGame;
import com.dvdfu.panic.handlers.Consts;
import com.dvdfu.panic.handlers.GameStage;
import com.dvdfu.panic.visuals.Label;
import com.dvdfu.panic.visuals.menus.QuitMenu;

public class QuitScreen extends AbstractScreen {
	private GameStage stage;
	private QuitMenu menu;
	private Label text;

	public QuitScreen(MainGame game) {
		super(game);
		menu = new QuitMenu(game);
		text = new Label("Do you really want to quit?");
		text.setDrawCentered(true);
		text.setPosition(Consts.ScreenWidth / 2, Consts.ScreenHeight / 2 + 16);
		stage = new GameStage();
		stage.addActor(menu);
		stage.addActor(text);
		stage.setCamPosition(Consts.ScreenWidth / 2, Consts.ScreenHeight / 2);
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