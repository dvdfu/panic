package com.dvdfu.panic.handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class Consts {
	public static final int Resolution = 2;
	public static final int ScreenHeight = 240;
	public static final int ScreenWidth = ScreenHeight * 8 / 5;
	public static final int WindowWidth = ScreenWidth * Resolution;
	public static final int WindowHeight = ScreenHeight * Resolution;
	
	public static final BitmapFont Test = new BitmapFont(Gdx.files.internal("data/test.fnt"));
	
	public static final int FY = 90;
	public static final int F1Height = FY;
	public static final int F1Width = ScreenWidth * 2 / 3;
	public static final int F2Y = FY * 2;
	public static final int F2Width = ScreenWidth / 3;
	public static final int F2Height = 3;
	public static final int F3Y = FY * 3;
	public static final int F3Width = ScreenWidth / 3;
	public static final int F3Height = 16;
	public static final int BoundsL = 0;
	public static final int BoundsR = ScreenWidth - BoundsL;
	
	public static final float Gravity = 0.2f;
	public static final float SpriteSpeed = 6;

	public static boolean F1;
}
