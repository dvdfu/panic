package com.dvdfu.panic.visuals.menus;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Array;
import com.dvdfu.panic.MainGame;
import com.dvdfu.panic.handlers.Input;

public abstract class AbstractMenu extends Group {
	protected MainGame game;
	protected Array<Button> buttons;
	protected Button selectedButton;
	private int cycleTimer;
	private boolean horizontal;

	public AbstractMenu(MainGame game) {
		this.game = game;
		buttons = new Array<Button>();
	}

	public void update() {
		if (cycleTimer > 0) {
			cycleTimer--;
		} else {
			if (Input.KeyDown(Input.ARROW_UP)) {
				if (selectedButton instanceof Scroller) {
					((Scroller) selectedButton).increase();
					cycleTimer = 6;
				} else if (selectedButton.bUp != null) {
					selectedButton = selectedButton.bUp;
					cycleTimer = 6;
				}
			}
			if (Input.KeyDown(Input.ARROW_DOWN)) {
				if (selectedButton instanceof Scroller) {
					((Scroller) selectedButton).decrease();
					cycleTimer = 6;
				} else if (selectedButton.bDown != null) {
					selectedButton = selectedButton.bDown;
					cycleTimer = 6;
				}
			}
			if (Input.KeyDown(Input.ARROW_LEFT) && selectedButton.bLeft != null) {
				selectedButton = selectedButton.bLeft;
				cycleTimer = 6;
			}
			if (Input.KeyDown(Input.ARROW_RIGHT)
					&& selectedButton.bRight != null) {
				selectedButton = selectedButton.bRight;
				cycleTimer = 6;
			}
			if (Input.KeyReleased(Input.ANY_KEY)) {
				cycleTimer = 0;
			}
		}
		if (Input.KeyPressed(Input.Z)) {
			pressButton(selectedButton);
		}
		if (Input.KeyPressed(Input.CTRL)) {
			pressCancel();
		}
	}

	public void draw(Batch batch, float parentAlpha) {
		for (Button button : buttons) {
			button.setSelect(selectedButton.equals(button));
			button.draw(batch, parentAlpha);
		}
	}

	public void addButton(Button button) {
		buttons.add(button);
	}
	
	public void setHorizontal(boolean horizontal) {
		this.horizontal = horizontal;
	}
	
	public boolean getHorizontal() {
		return horizontal;
	}

	public abstract void pressButton(Button button);

	public abstract void pressCancel();
}
