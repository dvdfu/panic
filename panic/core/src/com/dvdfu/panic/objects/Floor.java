package com.dvdfu.panic.objects;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.dvdfu.panic.handlers.GameStage;
import com.dvdfu.panic.visuals.Sprites;

public class Floor extends GameObject {
	private boolean solid;

	public Floor(GameStage stage) {
		super(stage);
		stretched = true;
		setSprite(Sprites.plain);
	}

	public void update() {
		return;
	}

	public void reset() {
		return;
	}
	
	public void draw(Batch batch, float parentAlpha) {
		batch.setColor(115f/255, 184f/255, 217f/255, 1);
		super.draw(batch, parentAlpha);
		batch.setColor(1, 1, 1, 1);
	}
	
	public void setSolid(boolean solid) {
		this.solid = solid;
	}
	
	public boolean getSolid() {
		return solid;
	}
}
