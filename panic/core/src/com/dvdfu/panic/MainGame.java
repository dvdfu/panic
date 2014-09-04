package com.dvdfu.panic;

import java.util.Stack;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.dvdfu.panic.handlers.Consts;
import com.dvdfu.panic.handlers.Input;
import com.dvdfu.panic.handlers.InputController;
import com.dvdfu.panic.screens.AbstractScreen;
import com.dvdfu.panic.screens.TestScreen;

public class MainGame extends Game {
	private Stack<AbstractScreen> screens;
	private FrameBuffer fb;
	private Matrix4 fbMatrix;
	private ShaderProgram fbShader;
	private SpriteBatch fbBatch;

	public void create() {
		Gdx.input.setInputProcessor(new InputController());
		screens = new Stack<AbstractScreen>();
		enterScreen(new TestScreen(this));
		fb = new FrameBuffer(Format.RGBA8888, Consts.WindowWidth, Consts.WindowHeight, false);
		fb.getColorBufferTexture().setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		fbMatrix = new Matrix4();
		fbMatrix.setToOrtho2D(Consts.ScreenWidth * (Consts.Resolution - 1) / 2, Consts.ScreenHeight * (Consts.Resolution - 1)
			/ 2, Consts.ScreenWidth, Consts.ScreenHeight);
		fbShader = new ShaderProgram(Gdx.files.internal("shaders/passthrough.vsh"), Gdx.files.internal("shaders/vignette.fsh"));
		ShaderProgram.pedantic = false;
		fbShader.begin();
		fbShader.setUniformf("u_resolution", Consts.WindowWidth, Consts.WindowHeight);
		fbShader.end();
		fbBatch = new SpriteBatch();
		fbBatch.setProjectionMatrix(fbMatrix);
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
		if (screens.isEmpty()) { return; }
		screens.pop();
		screens.push(screen);
		setScreen(screens.peek());
	}

	public void exitScreen() {
		if (screens.isEmpty()) { return; }
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
		if (getScreen() != null) {
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
			Gdx.gl.glClearColor(36f/255, 77f/255, 124f/255, 1);
			super.render();
		}
		fb.end();
		fbBatch.begin();
		fbBatch.draw(fb.getColorBufferTexture(), 0, 0, Consts.WindowWidth, Consts.WindowHeight, 0, 0, 1, 1);
		fbBatch.end();
		Input.update();
	}

	public void resize(int width, int height) {
		super.resize(width, height);
	}

	public void pause() {}

	public void resume() {}
}