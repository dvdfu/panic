package com.dvdfu.panic.visuals.menus;

import com.badlogic.gdx.Gdx;
import com.dvdfu.panic.MainGame;
import com.dvdfu.panic.screens.TestScreen;

public class GameMenu extends AbstractMenu {

	private Button buttonStart;
	private Button buttonHelp;
	private Button buttonQuit;

	public GameMenu(MainGame game) {
		super(game);
		buttonStart = new Button("START!");
		buttonStart.setPosition(32, 160);
		buttons.add(buttonStart);
		buttonHelp = new Button("HELP!");
		buttonHelp.setPosition(32, 140);
		buttons.add(buttonHelp);
		buttonQuit = new Button("QUIT!");
		buttonQuit.setPosition(32, 120);
		buttons.add(buttonQuit);

		buttonStart.bDown = buttonHelp;
		buttonStart.bUp = buttonQuit;
		buttonHelp.bDown = buttonQuit;
		buttonHelp.bUp = buttonStart;
		buttonQuit.bDown = buttonStart;
		buttonQuit.bUp = buttonHelp;

		selectedButton = buttonStart;
	}

	public void selectButton(Button button) {
		if (button.equals(buttonStart)) {
			game.changeScreen(new TestScreen(game));
		} else if (button.equals(buttonQuit)) {
			Gdx.app.exit();
		}
	}
}
