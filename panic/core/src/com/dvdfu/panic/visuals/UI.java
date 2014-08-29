package com.dvdfu.panic.visuals;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.dvdfu.panic.handlers.Consts;

public class UI extends Actor {
	private float timerTicks;
	private int timerSeconds;
	private int timerMinutes;
	private Label timerLabel;
	private int score;
	private int scoreTimer;
	private Label scoreLabel;

	public UI() {
		scoreLabel = new Label();
		timerLabel = new Label();
	}

	public void act(float delta) {
		timerTicks += delta * 100;
		while (timerTicks > 100) {
			timerTicks -= 100;
			timerSeconds++;
			if (timerSeconds == 60) {
				timerSeconds = 0;
				timerMinutes++;
			}
		}
		timerLabel.setText(timerMinutes + ":" + (timerSeconds < 10 ? "0" : "") + timerSeconds + ":"
			+ (timerTicks < 10 ? "0" : "") + (int) timerTicks);
		scoreLabel.setText("" + score);
		if (scoreTimer > 0) {
			scoreTimer--;
		}
	}

	public void addScore(int score) {
		this.score += score;
		scoreTimer = 6;
	}

	public void draw(Batch batch, float parentAlpha) {
		scoreLabel.drawC(batch, Consts.ScreenWidth / 2, Consts.ScreenHeight - 16 + scoreTimer * 2);
		timerLabel.drawC(batch, Consts.ScreenWidth / 2, Consts.ScreenHeight - 32);
	}
}
