package com.dvdfu.panic.objects;

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
	
	public void setSolid(boolean solid) {
		this.solid = solid;
	}
	
	public boolean getSolid() {
		return solid;
	}
}
