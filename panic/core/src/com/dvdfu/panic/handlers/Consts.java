package com.dvdfu.panic.handlers;


public class Consts {
	public static final int Resolution = 2;
	public static final int ScreenHeight = 240;
	public static final int ScreenWidth = ScreenHeight / 5 * 8;
	public static final int WindowWidth = ScreenWidth * Resolution;
	public static final int WindowHeight = ScreenHeight * Resolution;
	public static final int F1Height = ScreenHeight / 4;
	public static final int F1Width = ScreenWidth * 2 / 3;
	public static final int F2Y = ScreenHeight * 2 / 3;
	public static final int F2Width = ScreenWidth / 3;
	public static final int F2Height = 16;
	public static final int BoundsL = 0;
	public static final int BoundsR = ScreenWidth - BoundsL;
	public static final float Gravity = 0.3f;

	public static boolean F1;
}
