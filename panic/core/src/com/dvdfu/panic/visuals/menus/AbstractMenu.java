package com.dvdfu.panic.visuals.menus;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.Array;
import com.dvdfu.panic.MainGame;
import com.dvdfu.panic.handlers.Input;

public abstract class AbstractMenu {
	protected MainGame game;
	protected Array<Button> buttons;
	protected Button selectedButton;

	public AbstractMenu(MainGame game) {
		this.game = game;
		buttons = new Array<Button>();
	}

	public void update() {
		if (Input.KeyPressed(Input.ARROW_UP) && selectedButton.bUp != null) {
			selectedButton = selectedButton.bUp;
		}
		if (Input.KeyPressed(Input.ARROW_DOWN) && selectedButton.bDown != null) {
			selectedButton = selectedButton.bDown;
		}
		if (Input.KeyPressed(Input.Z)) {
			selectButton(selectedButton);
		}
	}

	public void draw(Batch batch) {
		for (Button button : buttons) {
			button.setSelect(selectedButton.equals(button));
			button.draw(batch);
		}
	}

	public void addButton(Button button) {
		buttons.add(button);
	}

	public abstract void selectButton(Button button);
}
