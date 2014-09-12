package com.dvdfu.panic.visuals.menus;

import com.badlogic.gdx.Gdx;
import com.dvdfu.panic.MainGame;
import com.dvdfu.panic.handlers.Consts;
import com.dvdfu.panic.screens.MainMenuScreen;

public class QuitMenu extends AbstractMenu {

	private Button buttonYes;
	private Button buttonNo;

	public QuitMenu(MainGame game) {
		super(game);
		buttonYes = new Button("YES!");
		buttonYes.setDrawCentered(true);
		buttonYes.setPosition(Consts.ScreenWidth / 2 - 32,
				Consts.ScreenHeight / 2 - 8);
		buttons.add(buttonYes);
		buttonNo = new Button("NO!");
		buttonNo.setDrawCentered(true);
		buttonNo.setPosition(Consts.ScreenWidth / 2 + 32,
				Consts.ScreenHeight / 2 - 8);
		buttons.add(buttonNo);

		buttonYes.bRight = buttonNo;
		buttonNo.bLeft = buttonYes;

		selectedButton = buttonNo;
	}

	public void pressButton(Button button) {
		if (button.equals(buttonNo)) {
			game.changeScreen(new MainMenuScreen(game));
		} else if (button.equals(buttonYes)) {
			Gdx.app.exit();
		}
	}

	public void pressCancel() {
		game.changeScreen(new MainMenuScreen(game));
	}
}
