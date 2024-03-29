package com.dvdfu.panic;

import java.util.Stack;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.dvdfu.panic.handlers.Consts;
import com.dvdfu.panic.handlers.GameShader;
import com.dvdfu.panic.handlers.Input;
import com.dvdfu.panic.handlers.InputController;
import com.dvdfu.panic.screens.AbstractScreen;
import com.dvdfu.panic.screens.MainMenuScreen;

public class MainGame extends Game {
	private Stack<AbstractScreen> screens;
	private FrameBuffer fb;
	private GameShader fbShader;
	private SpriteBatch fbBatch;
	private int screenScale;

	public void create() {
		Gdx.input.setInputProcessor(new InputController());
		screens = new Stack<AbstractScreen>();
		enterScreen(new MainMenuScreen(this));
		fb = new FrameBuffer(Format.RGBA8888, Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight(), false);
		fb.getColorBufferTexture().setFilter(TextureFilter.Nearest,
				TextureFilter.Nearest);
		fbShader = new GameShader("shaders/scale.vsh",
				"shaders/vignette.fsh");
		fbBatch = new SpriteBatch();
		fbBatch.setShader(fbShader);
		// dermetfan: openGL shader tutorial
		// angelcode: BMfont
		// aseprite
	}

	public void enterScreen(AbstractScreen screen) {
		if (!screens.isEmpty()) {
			screens.peek().pause();
		}
		screens.push(screen);
		setScreen(screens.peek());
	}

	public void changeScreen(AbstractScreen screen) {
		if (screens.isEmpty()) {
			return;
		}
		screens.pop();
		screens.push(screen);
		setScreen(screens.peek());
	}

	public void exitScreen() {
		if (screens.isEmpty()) {
			Gdx.app.exit();
		}
		screens.pop();
		screens.peek().resume();
		setScreen(screens.peek());
	}

	public void dispose() {
		fb.dispose();
		fbBatch.dispose();
	}

	public void render() {
		fb.begin();
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glClearColor(36f / 255, 77f / 255, 124f / 255, 1);
		if (getScreen() != null) {
			super.render();
		}
		fb.end();
		fbBatch.begin();
		fbBatch.draw(fb.getColorBufferTexture(), 0, 0, Consts.ScreenWidth,
				Consts.ScreenHeight, 0, 0, 1, 1);
		fbBatch.end();
		Input.update();
	}

	public void resize(int width, int height) {
		super.resize(width, height);
		screenScale = width / Consts.ScreenWidth;
		fb.dispose();
		fb = new FrameBuffer(Format.RGBA8888, width, height, false);
		fb.getColorBufferTexture().setFilter(TextureFilter.Nearest,
				TextureFilter.Nearest);
		fbBatch.begin();
		fbShader.setUniformf("u_resolution", width, height);
		fbShader.setUniformf("u_scale", screenScale);
		fbBatch.end();
	}

	public void pause() {
	}

	public void resume() {
	}
}