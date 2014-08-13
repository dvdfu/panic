package com.dvdfu.panic.handlers;

import com.badlogic.gdx.utils.Pool;
import com.dvdfu.panic.objects.EnemyBasic;
import com.dvdfu.panic.objects.GameObject;
import com.dvdfu.panic.objects.Item;

public class ObjectPool {
	private Pool<EnemyBasic> enemyBasic;
	private Pool<Item> item;

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
	}

	public EnemyBasic getEnemyBasic() {
		return enemyBasic.obtain();
	}

	public Item getItem() {
		return item.obtain();
	}

	public void free(GameObject object) {
		if (object instanceof EnemyBasic) {
			enemyBasic.free((EnemyBasic) object);
		} else if (object instanceof Item) {
			item.free((Item) object);
		}
	}
}
