package com.dvdfu.panic;

import java.util.Stack;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
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
	private FrameBuffer fbo;
	private SpriteBatch fbBatch;
	private Matrix4 fbMatrix;
	private ShaderProgram shader;

	public void create() {
		shader = new ShaderProgram(Gdx.files.internal("data/shader.vert"), Gdx.files.internal("data/shader.frag"));
		Gdx.input.setInputProcessor(new InputController());
		screens = new Stack<AbstractScreen>();
		enterScreen(new TestScreen(this));
		fbo = new FrameBuffer(Format.RGBA8888, Consts.WindowWidth, Consts.WindowHeight, false);
		fbo.getColorBufferTexture().setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		fbBatch = new SpriteBatch();
		fbMatrix = new Matrix4();
		fbMatrix.setToOrtho2D(0, 0, Consts.ScreenWidth, Consts.ScreenHeight);
		fbBatch.setProjectionMatrix(fbMatrix);
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
		fbo.dispose();
		fbBatch.dispose();
	}

	public void render() {
		fbo.begin();
		fbBatch.setShader(shader);
		if (getScreen() != null) {
			super.render();
		}
		fbo.end();
		fbBatch.begin();
		fbBatch.draw(fbo.getColorBufferTexture(), -Consts.ScreenWidth * (Consts.Resolution - 1) / 2, -Consts.ScreenHeight
			* (Consts.Resolution - 1) / 2, Consts.WindowWidth, Consts.WindowHeight, 0, 0, 1, 1);
		fbBatch.end();
		Input.update();
	}

	public void resize(int width, int height) {
		super.resize(width, height);
	}

	public void pause() {}

	public void resume() {}
}