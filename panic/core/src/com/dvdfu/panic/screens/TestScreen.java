package com.dvdfu.panic.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.dvdfu.panic.MainGame;
import com.dvdfu.panic.objects.EnemyBasic;
import com.dvdfu.panic.objects.Player;
import com.dvdfu.panic.objects.Solid;

public class TestScreen extends AbstractScreen {
	private Stage stage;
	private Group enemies;
	private Group solids;
	private Player player;
	private int timer;

	public TestScreen(MainGame game) {
		super(game);
		stage = new Stage();
		solids = new Group();
		Solid s1 = new Solid(64, 0);
		s1.setSize(Gdx.graphics.getWidth() - 128, 64);
		solids.addActor(s1);
		Solid s2 = new Solid(160, 100);
		s2.setSize(Gdx.graphics.getWidth() - 320, 160);
		solids.addActor(s2);
		stage.addActor(solids);

		enemies = new Group();
		enemies.addActor(new EnemyBasic());
		stage.addActor(enemies);

		player = new Player();
		stage.addActor(player);
		timer = 0;
	}

	public void render(float delta) {
		// timer ++;
		if (timer == 100) {
			enemies.addActor(new EnemyBasic());
			timer = 0;
		}
		player.move();
		for (Actor actor2 : enemies.getChildren()) {
			EnemyBasic enemy = (EnemyBasic) actor2;
			enemy.move();
		}
		collisions();
		stage.act(delta);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glClearColor(1, 1, 1, 1);
		stage.draw();

		ShapeRenderer shapes = new ShapeRenderer();
		shapes.setColor(Color.BLUE);
		shapes.begin(ShapeType.Line);
		shapes.rect(player.getBounds().x, player.getBounds().y, player.getBounds().width, player.getBounds().height);
		shapes.end();
		shapes.dispose();
	}

	private void collisions() {
		for (Actor actor : solids.getChildren()) {
			Solid solid = (Solid) actor;
			player.collideSolid(solid);
			for (Actor actor2 : enemies.getChildren()) {
				EnemyBasic enemy = (EnemyBasic) actor2;
				enemy.collideSolid(solid);
				player.collideEnemy(enemy);
			}
		}
	}

	public void resize(int width, int height) {}

	public void show() {}

	public void hide() {}

	public void pause() {}

	public void resume() {}

	public void dispose() {
		stage.dispose();
	}
}
