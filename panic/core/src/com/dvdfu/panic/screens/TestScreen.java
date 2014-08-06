package com.dvdfu.panic.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.dvdfu.panic.MainGame;
import com.dvdfu.panic.objects.AbstractEnemy;
import com.dvdfu.panic.objects.EnemyBasic;
import com.dvdfu.panic.objects.Player;
import com.dvdfu.panic.objects.Solid;

public class TestScreen extends AbstractScreen {
	private Stage stage;
	private Group enemies;
	private Group solids;
	private Player player;

	public TestScreen(MainGame game) {
		super(game);
		stage = new Stage();
		
		solids = new Group();
		solids.addActor(new Solid(60, 0));
		stage.addActor(solids);
		
		enemies = new Group();
		enemies.addActor(new EnemyBasic());
		stage.addActor(enemies);
		
		player = new Player();
		player.setBlocks(solids);
		player.setEnemies(enemies);
		stage.addActor(player);
		
		for (Actor actor : enemies.getChildren()) {
			AbstractEnemy enemy = (AbstractEnemy) actor;
			enemy.setPlayer(player);
			enemy.setSolids(solids);
		}
	}

	public void render(float delta) {
		// player.setPosition(Input.mouse.x, Input.mouse.y);
		stage.act(delta);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glClearColor(1, 1, 1, 1);
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
