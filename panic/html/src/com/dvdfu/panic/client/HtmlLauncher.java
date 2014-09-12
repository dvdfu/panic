package com.dvdfu.panic.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.dvdfu.panic.MainGame;
import com.dvdfu.panic.handlers.Consts;

public class HtmlLauncher extends GwtApplication {

	@Override
	public GwtApplicationConfiguration getConfig() {
		return new GwtApplicationConfiguration(Consts.ScreenWidth, Consts.ScreenHeight);
	}

	@Override
	public ApplicationListener getApplicationListener() {
		return new MainGame();
	}
}