package com.dvdfu.panic.handlers;

import com.badlogic.gdx.utils.Pool;
import com.dvdfu.panic.objects.EnemyBasic;
import com.dvdfu.panic.objects.GameObject;
import com.dvdfu.panic.objects.Item;
import com.dvdfu.panic.objects.Particle;

public class ObjectPool {
	private Pool<EnemyBasic> enemyBasic;
	private Pool<Item> item;
	private Pool<Particle> particle;

	public ObjectPool() {
		enemyBasic = new Pool<EnemyBasic>() {
			protected EnemyBasic newObject() {
				return new EnemyBasic();
			}
		};
		item = new Pool<Item>() {
			protected Item newObject() {
				return new Item();
			}
		};
		particle = new Pool<Particle>() {
			protected Particle newObject() {
				return new Particle();
			}
		};
	}

	public EnemyBasic getEnemyBasic() {
		return enemyBasic.obtain();
	}

	public Item getItem() {
		return item.obtain();
	}

	public Particle getParticle() {
		return particle.obtain();
	}

	public void free(GameObject object) {
		if (object instanceof EnemyBasic) {
			enemyBasic.free((EnemyBasic) object);
		} else if (object instanceof Item) {
			item.free((Item) object);
		} else if (object instanceof Particle) {
			particle.free((Particle) object);
		}
	}
}
