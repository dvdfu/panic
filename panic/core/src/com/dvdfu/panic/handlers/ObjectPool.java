package com.dvdfu.panic.handlers;

import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Pool;
import com.dvdfu.panic.objects.EnemyBasic;
import com.dvdfu.panic.objects.GameObject;
import com.dvdfu.panic.objects.Item;
import com.dvdfu.panic.objects.Particle;
import com.dvdfu.panic.objects.Solid;

public class ObjectPool implements Disposable {
	private Pool<Solid> solid;
	private Pool<EnemyBasic> enemyBasic;
	private Pool<Item> item;
	private Pool<Particle> particle;

	public ObjectPool() {
		solid = new Pool<Solid>() {
			protected Solid newObject() {
				return new Solid();
			}
		};
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

	public Solid getSolid() {
		return solid.obtain();
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
		if (object instanceof Solid) {
			solid.free((Solid) object);
		} else if (object instanceof EnemyBasic) {
			enemyBasic.free((EnemyBasic) object);
		} else if (object instanceof Item) {
			item.free((Item) object);
		} else if (object instanceof Particle) {
			particle.free((Particle) object);
		}
	}

	public void dispose() {}
}
