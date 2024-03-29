package com.dvdfu.panic.handlers;

import com.badlogic.gdx.utils.Pool;
import com.dvdfu.panic.objects.EnemyFly;
import com.dvdfu.panic.objects.EnemyJump;
import com.dvdfu.panic.objects.EnemyWalker;
import com.dvdfu.panic.objects.Floor;
import com.dvdfu.panic.objects.GameObject;
import com.dvdfu.panic.objects.Item;
import com.dvdfu.panic.objects.Particle;

public class ObjectPool {
	private Pool<Floor> solid;
	private Pool<EnemyWalker> enemyWalker;
	private Pool<EnemyJump> enemyJump;
	private Pool<EnemyFly> enemyFly;
	private Pool<Item> item;
	private Pool<Particle> particle;

	public ObjectPool(final GameStage stage) {
		solid = new Pool<Floor>() {
			protected Floor newObject() {
				return new Floor(stage);
			}
		};
		enemyWalker = new Pool<EnemyWalker>() {
			protected EnemyWalker newObject() {
				return new EnemyWalker(stage);
			}
		};
		enemyJump = new Pool<EnemyJump>() {
			protected EnemyJump newObject() {
				return new EnemyJump(stage);
			}
		};
		enemyFly = new Pool<EnemyFly>() {
			protected EnemyFly newObject() {
				return new EnemyFly(stage);
			}
		};
		item = new Pool<Item>() {
			protected Item newObject() {
				return new Item(stage);
			}
		};
		particle = new Pool<Particle>() {
			protected Particle newObject() {
				return new Particle(stage);
			}
		};
	}

	public Floor getSolid() {
		return solid.obtain();
	}

	public EnemyWalker getEnemyWalker() {
		return enemyWalker.obtain();
	}

	public EnemyJump getEnemyJump() {
		return enemyJump.obtain();
	}

	public EnemyFly getEnemyFly() {
		return enemyFly.obtain();
	}

	public Item getItem() {
		return item.obtain();
	}

	public Particle getParticle() {
		return particle.obtain();
	}

	public void free(GameObject object) {
		if (object instanceof Floor) {
			solid.free((Floor) object);
		} else if (object instanceof EnemyWalker) {
			enemyWalker.free((EnemyWalker) object);
		} else if (object instanceof EnemyJump) {
			enemyJump.free((EnemyJump) object);
		} else if (object instanceof EnemyFly) {
			enemyFly.free((EnemyFly) object);
		} else if (object instanceof Item) {
			item.free((Item) object);
		} else if (object instanceof Particle) {
			particle.free((Particle) object);
		}
	}
}
