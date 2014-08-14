package com.dvdfu.panic.visuals;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class Sprites {
	private static final TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("data/panic.pack"));
	public static final Animation player = new Animation(atlas.createSprite("player"), 32, 32);
	public static final Animation plain = new Animation(atlas.createSprite("plain"), 32, 32);
	public static final Animation enemyWalk = new Animation(atlas.createSprite("enemy_walk"), 24, 32);
	public static final Animation enemyThrow = new Animation(atlas.createSprite("enemy_throw"), 22, 16);
}
