package com.dvdfu.panic.visuals.menus;

import com.dvdfu.panic.MainGame;
import com.dvdfu.panic.handlers.Consts;
import com.dvdfu.panic.screens.OptionsScreen;
import com.dvdfu.panic.screens.QuitScreen;
import com.dvdfu.panic.screens.TestScreen;

public class MainMenu extends AbstractMenu {

	private Button buttonStart;
	private Scroller buttonDifficulty;
	private Button buttonOptions;
	private Button buttonQuit;

	public MainMenu(MainGame game) {
		super(game);
		buttonStart = new Button("START!");
		buttonStart.setPosition(32, 160);
		buttons.add(buttonStart);
		buttonDifficulty = new Scroller(new String[] { "EASY", "MEDIUM", "HARD"});
		buttonDifficulty.setPosition(100, 160);
		buttons.add(buttonDifficulty);
		buttonOptions = new Button("OPTIONS!");
		buttonOptions.setPosition(32, 140);
		buttons.add(buttonOptions);
		buttonQuit = new Button("QUIT!");
		buttonQuit.setPosition(32, 120);
		buttons.add(buttonQuit);

		buttonStart.bRight = buttonDifficulty;
		buttonStart.bDown = buttonOptions;
		buttonOptions.bUp = buttonStart;
		buttonOptions.bDown = buttonQuit;
		buttonDifficulty.bLeft = buttonStart;
		buttonQuit.bUp = buttonOptions;

		selectedButton = buttonStart;
	}

	public void pressButton(Button button) {
		if (button.equals(buttonStart)) {
			game.changeScreen(new TestScreen(game));
			Consts.Difficulty = buttonDifficulty.getIndex() + 1;
		} else if (button.equals(buttonOptions)) {
			game.enterScreen(new OptionsScreen(game));
		} else if (button.equals(buttonQuit)) {
			game.enterScreen(new QuitScreen(game));
		}
	}

	public void pressCancel() {
	}
}
