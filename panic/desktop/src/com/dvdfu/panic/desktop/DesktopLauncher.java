package com.dvdfu.panic.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.dvdfu.panic.MainGame;
import com.dvdfu.panic.handlers.Consts;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.resizable = false;
		config.vSyncEnabled = false;
		config.width = Consts.WindowWidth;
		config.height = Consts.WindowHeight;
		new LwjglApplication(new MainGame(), config);
	}
}