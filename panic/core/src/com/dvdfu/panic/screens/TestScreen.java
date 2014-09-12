package com.dvdfu.panic.screens;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.dvdfu.panic.MainGame;
import com.dvdfu.panic.handlers.Consts;
import com.dvdfu.panic.handlers.Enums.EnemyState;
import com.dvdfu.panic.handlers.Enums.ParticleType;
import com.dvdfu.panic.handlers.GameShader;
import com.dvdfu.panic.handlers.GameStage;
import com.dvdfu.panic.handlers.Input;
import com.dvdfu.panic.handlers.ObjectPool;
import com.dvdfu.panic.objects.AbstractEnemy;
import com.dvdfu.panic.objects.EnemyFly;
import com.dvdfu.panic.objects.Floor;
import com.dvdfu.panic.objects.Item;
import com.dvdfu.panic.objects.Lava;
import com.dvdfu.panic.objects.Particle;
import com.dvdfu.panic.objects.Player;
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
	private GameShader fbShader;

	public TestScreen(MainGame game) {
		super(game);
		stage = new GameStage();
		objects = new ObjectPool(stage);
		stage.addActor(items = new Group());
		solids = new Group();
		Floor floor1 = new Floor(stage);
		floor1.setSolid(true);
		floor1.setPosition((Consts.ScreenWidth - Consts.F1Width) / 2, 0);
		floor1.setSize(Consts.F1Width, Consts.F1Height);
		solids.addActor(floor1);
		Floor floor2L = new Floor(stage);
		floor2L.setPosition(0, Consts.F2Y - Consts.F2Height);
		floor2L.setSize(Consts.F2Width, Consts.F2Height);
		solids.addActor(floor2L);
		Floor floor2R = new Floor(stage);
		floor2R.setPosition(Consts.ScreenWidth - Consts.F2Width, Consts.F2Y - Consts.F2Height);
		floor2R.setSize(Consts.F2Width, Consts.F2Height);
		solids.addActor(floor2R);
		Floor floor3 = new Floor(stage);
		floor3.setSolid(true);
		floor3.setPosition((Consts.ScreenWidth - Consts.F3Width) / 2, Consts.F3Y);
		floor3.setSize(Consts.F3Width, Consts.F3Height);
		solids.addActor(floor3);
		stage.addActor(solids);
		stage.addActor(enemies = new Group());
		stage.addActor(player = new Player(stage));
		stage.addActor(particles = new Group());
		stage.addActor(lava = new Lava(stage));
		stage.addActor(ui = new UI());
		stage.setCamPosition(Consts.ScreenWidth / 2, player.getY() + player.getHeight() / 2);
		fbShader = new GameShader("shaders/passthrough.vsh", "shaders/passthrough.fsh");
	}

	public void render(float delta) {
		spawnEnemies();
		update();
		collisions();
		stage.act(delta);
		stage.setCamFocus(Consts.ScreenWidth / 2, player.getCY());
		ui.setView(stage.getCamX(), stage.getCamY());
		stage.getBatch().setShader(fbShader);
		stage.draw();
	}

	private void spawnEnemies() {
		timer++;
		if (timer == 120 - 36 * Consts.Difficulty) {
			AbstractEnemy newEnemy = null;
			if (MathUtils.randomBoolean()) {
				newEnemy = objects.getEnemyWalker();
			} else {
				if (MathUtils.randomBoolean(1)) {
					newEnemy = objects.getEnemyJump();
				} else {
					newEnemy = objects.getEnemyFly();
				}
			}
			newEnemy.setPosition(Consts.ScreenWidth / 2 - newEnemy.getWidth() / 2,
				Math.max(Consts.F3Y + Consts.ScreenHeight / 2, stage.getCamY() + Consts.ScreenHeight / 2));
			enemies.addActor(newEnemy);
			timer = 0;
		}
	}

	private void gameOver() {
		game.changeScreen(new GameOverScreen(game));
	}

	private void update() {
		player.update();
		for (Actor actor : enemies.getChildren()) {
			AbstractEnemy enemy = (AbstractEnemy) actor;
			enemy.update();
		}
		for (Actor actor : items.getChildren()) {
			Item item = (Item) actor;
			item.update();
		}
		for (Actor actor : particles.getChildren()) {
			Particle p = (Particle) actor;
			p.update();
		}
	}

	private void collisions() {
		// PLAYER
		if (player.getY() < lava.getTop()) {
			gameOver();
		}
		// ALL SOLIDS
		for (Actor actor : solids.getChildren()) {
			Floor solid = (Floor) actor;
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
			if (player.collideEnemy(enemy)) {
				gameOver();
			}
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
			// if (enemy.getState() == EnemyState.THROWN) {
			// float speed = (float) Math.sqrt(enemy.getXSpeed() * enemy.getXSpeed() + enemy.getYSpeed() * enemy.getYSpeed());
			// for (int k = 0; k < speed; k++) {
			// Particle p = objects.getParticle();
			// p.setType(ParticleType.TRAIL);
			// p.setPosition(enemy.getCX() - 1 + k * enemy.getXSpeed() / speed, enemy.getCY() - 1 + k * enemy.getYSpeed()
			// / speed);
			// particles.addActor(p);
			// }
			// }
		}
		// ALL ITEMS
		for (Actor actor : items.getChildren()) {
			Item item = (Item) actor;
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
			if (p.dead()) {
				particles.removeActor(p);
				objects.free(p);
			}
		}
	}

	private void removeEnemy(AbstractEnemy enemy) {
		for (int j = 0; j < 16; j++) {
			Particle p = objects.getParticle();
			p.setType(ParticleType.FIRE);
			p.setPosition(enemy.getX() + enemy.getWidth() / 2, enemy.getY());
			particles.addActor(p);
		}
		spawnItem(enemy);
		ui.addScore(1);
		enemies.removeActor(enemy);
		objects.free(enemy);
	}

	public void spawnItem(AbstractEnemy enemy) {
		if (MathUtils.randomBoolean(0.3f)) {
			Item item = objects.getItem();
			item.setPosition(enemy.getX() + enemy.getWidth() / 2 - item.getWidth() / 2,
				enemy.getY() + enemy.getHeight() - item.getHeight() / 2);
			item.setVelocity(enemy.getXSpeed(), 3);
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
