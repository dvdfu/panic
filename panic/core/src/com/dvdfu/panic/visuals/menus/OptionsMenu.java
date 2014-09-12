package com.dvdfu.panic.visuals.menus;

import com.badlogic.gdx.Gdx;
import com.dvdfu.panic.MainGame;
import com.dvdfu.panic.handlers.Consts;
import com.dvdfu.panic.screens.MainMenuScreen;

public class OptionsMenu extends AbstractMenu {

	private Scroller buttonDifficulty;

	public OptionsMenu(MainGame game) {
		super(game);
		buttonDifficulty = new Scroller(new String[] { "x1", "x2", "x3", "x4" });
		buttonDifficulty.setPosition(160, 160);
		buttonDifficulty.setIndex(Gdx.graphics.getWidth() / Consts.ScreenWidth - 1);
		buttons.add(buttonDifficulty);

		selectedButton = buttonDifficulty;
	}

	public void pressButton(Button button) {
	}

	public void pressCancel() {
		int newRes = buttonDifficulty.getIndex() + 1;
		Gdx.graphics.setDisplayMode(newRes * Consts.ScreenWidth, newRes
				* Consts.ScreenHeight, false);
		game.changeScreen(new MainMenuScreen(game));
	}
}
