package com.dvdfu.panic.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Pool;
import com.dvdfu.panic.MainGame;
import com.dvdfu.panic.objects.EnemyBasic;
import com.dvdfu.panic.objects.GameObject;
import com.dvdfu.panic.objects.Player;
import com.dvdfu.panic.objects.Solid;

public class TestScreen extends AbstractScreen {
	private Pool<GameObject> objects;
	private Stage stage;
	private Group enemies;
	private Group solids;
	private Player player;
	private int timer;

	public TestScreen(MainGame game) {
		super(game);
		objects = new Pool<GameObject>() {
			protected EnemyBasic newObject() {
				return new EnemyBasic();
			}
		};
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
		timer++;
		if (timer == 10) {
			enemies.addActor(objects.obtain());
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
		Gdx.gl.glClearColor(0.5f, 0.5f, 0.5f, 1);
		stage.draw();
	}

	private void collisions() {
		for (Actor actor : solids.getChildren()) {
			Solid solid = (Solid) actor;
			player.collideSolid(solid);
			for (Actor actor2 : enemies.getChildren()) {
				EnemyBasic enemy = (EnemyBasic) actor2;
				enemy.collideSolid(solid);
				player.collideEnemy(enemy);
				if (enemy.getX() > Gdx.graphics.getWidth() || enemy.getRight() < 0 || enemy.getTop() < 0) {
					enemies.removeActor(actor2);
					objects.free(enemy);
				}
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
