package com.dvdfu.panic.objects;

import com.dvdfu.panic.visuals.Sprites;

public class EnemyBasic extends AbstractEnemy {

	public EnemyBasic() {
		super();
		x = 60;
		setSize(64, 64);
		setSprite(Sprites.atlas.createSprite("plain"), 32, 32);
	}

	public void act(float delta) {
		if (state == State.ACTIVE)
		x += 1;
		if (state == State.THROWN) {
			if (y + dy > 0) {
				dy -= 0.3f;
			} else {
				dy = 0;
				y = 0;
				if (dx > 0.1f) {
					dx -= 0.1f;
				} else if (dx < -0.1f) {
					dx += 0.1f;
				} else {
					dx = 0;
				}
			}
		}
		super.act(delta);
	}
}