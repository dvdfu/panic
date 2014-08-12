package com.dvdfu.panic.objects;

import com.dvdfu.panic.handlers.Enums.ItemType;
import com.dvdfu.panic.visuals.Sprites;

public class Item extends GameObject {
	private ItemType type;
	
	public Item() {
		reset();
		setSize(32, 32);
		setSprite(Sprites.atlas.createSprite("plain"), 32, 32);
	}
	
	public void setType(ItemType type) {
		this.type = type;
	}
	
	public ItemType getType() {
		return type;
	}

	public void reset() {}
}
