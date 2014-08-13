package com.dvdfu.panic.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Pool;
import com.dvdfu.panic.MainGame;
import com.dvdfu.panic.handlers.Enums.EnemyState;
import com.dvdfu.panic.handlers.Input;
import com.dvdfu.panic.objects.EnemyBasic;
import com.dvdfu.panic.objects.Item;
import com.dvdfu.panic.objects.Player;
import com.dvdfu.panic.objects.Solid;

public class TestScreen extends AbstractScreen {
	private Pool<EnemyBasic> enemyPool;
	private Stage stage;
	private Group enemies;
	private Group solids;
	private Group items;
	private Player player;
	private int timer;

	public TestScreen(MainGame game) {
		super(game);
		enemyPool = new Pool<EnemyBasic>() {
			protected EnemyBasic newObject() {
				return new EnemyBasic();
			}
		};
		
		stage = new Stage();
		
		items = new Group();
		stage.addActor(items);
		
		solids = new Group();
		solids.setZIndex(0);
		Solid s1 = new Solid(96, 0);
		s1.setSize(Gdx.graphics.getWidth() - 192, 176);
		solids.addActor(s1);
		Solid s2 = new Solid(0, 360);
		s2.setSize(256, 32);
		solids.addActor(s2);
		Solid s3 = new Solid(Gdx.graphics.getWidth() - 256, 360);
		s3.setSize(256, 32);
		solids.addActor(s3);
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
		if (timer == 120) {
			enemies.addActor(enemyPool.obtain());
			timer = 0;
		}
		movement();
		collisions();
		stage.act(delta);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glClearColor(0.5f, 0.5f, 0.5f, 1);
		stage.draw();
	}
	
	private void movement() {
		player.move();
		for (Actor actor : enemies.getChildren()) {
			EnemyBasic enemy = (EnemyBasic) actor;
			enemy.move();
		}
		for (Actor actor : items.getChildren()) {
			Item item = (Item) actor;
			item.move();
		}
	}

	private void collisions() {
		for (Actor actor : solids.getChildren()) {
			Solid solid = (Solid) actor;
			player.collideSolid(solid);
			for (Actor actor2 : enemies.getChildren()) {
				EnemyBasic enemy = (EnemyBasic) actor2;
				enemy.collideSolid(solid);
			}
			for (Actor actor2 : items.getChildren()) {
				Item item = (Item) actor2;
				item.collideSolid(solid);
			}
		}
		for (int i = 0; i < enemies.getChildren().size; i++) {
			EnemyBasic enemy = (EnemyBasic) enemies.getChildren().items[i];
			player.collideEnemy(enemy);
			for (int j = i + 1; j < enemies.getChildren().size; j++) {
				EnemyBasic enemy2 = (EnemyBasic) enemies.getChildren().items[j];
				enemy.collideEnemy(enemy2);
				enemy2.collideEnemy(enemy);
			}
			if (enemy.getState() == EnemyState.REMOVE) {
				spawnItem(enemy.getX(), enemy.getY());
				enemies.removeActor(enemy);
				enemyPool.free(enemy);
			}
		}
		for (Actor actor : items.getChildren()) {
			Item item = (Item) actor;
			if (player.getBounds().overlaps(item.getBounds()) && Input.KeyPressed(Input.CTRL)) {
				items.removeActor(item);
			}
		}
	}
	
	public void spawnItem(float x, float y) {
		Item item = new Item(x, y);
		items.addActor(item);
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
