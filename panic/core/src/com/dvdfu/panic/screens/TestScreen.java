package com.dvdfu.panic.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.dvdfu.panic.MainGame;
import com.dvdfu.panic.handlers.Consts;
import com.dvdfu.panic.handlers.Enums.EnemyState;
import com.dvdfu.panic.handlers.Enums.ParticleType;
import com.dvdfu.panic.handlers.GameStage;
import com.dvdfu.panic.handlers.Input;
import com.dvdfu.panic.handlers.ObjectPool;
import com.dvdfu.panic.objects.AbstractEnemy;
import com.dvdfu.panic.objects.EnemyFly;
import com.dvdfu.panic.objects.Item;
import com.dvdfu.panic.objects.Lava;
import com.dvdfu.panic.objects.Particle;
import com.dvdfu.panic.objects.Player;
import com.dvdfu.panic.objects.Solid;
import com.dvdfu.panic.visuals.UI;

public class TestScreen extends AbstractScreen {
	private UI ui;
	private ObjectPool objects;
	private GameStage stage;
	private Group enemies;
	private Group solids;
	private Group items;
	private Group particles;
	private Player player;
	private Lava lava;
	private int timer;

	public TestScreen(MainGame game) {
		super(game);
		objects = new ObjectPool();
		stage = new GameStage();
		stage.addActor(items = new Group());
		solids = new Group();
		Solid floor1 = new Solid();
		floor1.setPosition((Consts.ScreenWidth - Consts.F1Width) / 2, 0);
		floor1.setSize(Consts.F1Width, Consts.F1Height);
		solids.addActor(floor1);
		Solid floor2L = new Solid();
		floor2L.setPosition(0, Consts.F2Y - Consts.F2Height);
		floor2L.setSize(Consts.F2Width, Consts.F2Height);
		solids.addActor(floor2L);
		Solid floor2R = new Solid();
		floor2R.setPosition(Consts.ScreenWidth - Consts.F2Width, Consts.F2Y - Consts.F2Height);
		floor2R.setSize(Consts.F2Width, Consts.F2Height);
		solids.addActor(floor2R);
		Solid floor3 = new Solid();
		floor3.setPosition((Consts.ScreenWidth - Consts.F3Width) / 2, Consts.F3Y - Consts.F3Height);
		floor3.setSize(Consts.F3Width, Consts.F3Height);
		solids.addActor(floor3);
		stage.addActor(solids);
		stage.addActor(enemies = new Group());
		stage.addActor(player = new Player());
		stage.addActor(particles = new Group());
		stage.addActor(lava = new Lava());
		stage.addActor(ui = new UI());
		stage.setCameraPosition(Consts.ScreenWidth / 2, player.getY() + player.getHeight() / 2);
	}

	public void render(float delta) {
		timer++;
		if (timer == 120) {
			if (MathUtils.randomBoolean()) {
				enemies.addActor(objects.getEnemyWalker());
			} else {
				if (MathUtils.randomBoolean()) {
					enemies.addActor(objects.getEnemyJump());
				} else {
					enemies.addActor(objects.getEnemyFly());
				}
			}
			timer = 0;
		}
		collisions();
		stage.setCameraFocus(Consts.ScreenWidth / 2, player.getY() + player.getHeight() / 2);
		stage.act(delta);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glClearColor(0.3f, 0.3f, 0.3f, 1);
		stage.draw();

	}

	private void gameOver() {
		game.changeScreen(new GameOverScreen(game));
	}

	private void collisions() {
		// PLAYER
		player.move();
		if (player.getY() < lava.getTop()) {
			gameOver();
		}
		// player.collideFlower(flower);
		// ALL SOLIDS
		for (Actor actor : solids.getChildren()) {
			Solid solid = (Solid) actor;
			player.collideSolid(solid);
			for (Actor actor2 : enemies.getChildren()) {
				AbstractEnemy enemy = (AbstractEnemy) actor2;
				enemy.collideSolid(solid);
			}
			for (Actor actor2 : items.getChildren()) {
				Item item = (Item) actor2;
				item.collideSolid(solid);
			}
		}
		// ALL ENEMIES
		for (int i = 0; i < enemies.getChildren().size; i++) {
			AbstractEnemy enemy = (AbstractEnemy) enemies.getChildren().items[i];
			enemy.move();
			player.collideEnemy(enemy);
			enemy.collidePlayer(player);
			if (enemy instanceof EnemyFly) {
				EnemyFly flyer = (EnemyFly) enemy;
				flyer.setGoal(player.getX(), player.getY());
			}
			if (enemy.getY() < lava.getTop()) {
				if (enemy.getState() == EnemyState.ACTIVE) {
					lava.raise();
				}
				removeEnemy(enemy);
			}
			for (int j = i + 1; j < enemies.getChildren().size; j++) {
				AbstractEnemy enemy2 = (AbstractEnemy) enemies.getChildren().items[j];
				enemy.collideEnemy(enemy2);
				enemy2.collideEnemy(enemy);
			}
			if (enemy.getState() == EnemyState.REMOVE) {
				removeEnemy(enemy);
			}
			if (enemy.getState() == EnemyState.THROWN) {
				Particle p = objects.getParticle();
				p.setType(ParticleType.TRAIL);
				p.setPosition(enemy.getX() + enemy.getWidth() / 2, enemy.getY() + enemy.getHeight() / 2);
				particles.addActor(p);
			}
		}
		// ALL ITEMS
		for (Actor actor : items.getChildren()) {
			Item item = (Item) actor;
			item.move();
			if (player.getBounds().overlaps(item.getBounds()) && Input.KeyPressed(Input.CTRL)) {
				player.getItem(item.getType());
				items.removeActor(item);
				objects.free(item);
			}
			if (item.getTop() < lava.getTop()) {
				items.removeActor(item);
				objects.free(item);
			}
		}
		// ALL PARTICLES
		for (Actor actor : particles.getChildren()) {
			Particle p = (Particle) actor;
			p.move();
			if (p.dead()) {
				particles.removeActor(p);
				objects.free(p);
			}
		}
	}

	private void removeEnemy(AbstractEnemy enemy) {
		for (int j = 0; j < 16; j++) {
			Particle p = objects.getParticle();
			p.setType(ParticleType.EXPLOSION);
			p.setPosition(enemy.getX() + enemy.getWidth() / 2, enemy.getY());
			particles.addActor(p);
		}
		spawnItem(enemy.getX(), enemy.getY());
		ui.addScore(1);
		enemies.removeActor(enemy);
		objects.free(enemy);
	}

	public void spawnItem(float x, float y) {
		if (MathUtils.randomBoolean(0.3f)) {
			Item item = objects.getItem();
			item.setPosition(x, y);
			items.addActor(item);
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
