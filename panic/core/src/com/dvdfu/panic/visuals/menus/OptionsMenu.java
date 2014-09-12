package com.dvdfu.panic.visuals.menus;

import com.badlogic.gdx.Gdx;
import com.dvdfu.panic.MainGame;
import com.dvdfu.panic.handlers.Consts;

public class OptionsMenu extends AbstractMenu {

	private Scroller buttonDifficulty;

	public OptionsMenu(MainGame game) {
		super(game);
		buttonDifficulty = new Scroller(new String[] { "x1", "x2", "x3", "x4" });
		buttonDifficulty.setPosition(160, 160);
		buttons.add(buttonDifficulty);

		selectedButton = buttonDifficulty;
	}

	public void pressButton(Button button) {
	}

	public void pressCancel() {
		// Consts.ScreenScale = buttonDifficulty.getIndex() + 1;
		game.screenScale = buttonDifficulty.getIndex() + 1;
		Gdx.graphics.setDisplayMode(game.screenScale * Consts.ScreenWidth,
				game.screenScale * Consts.ScreenHeight, false);
		game.exitScreen();
	}
}
