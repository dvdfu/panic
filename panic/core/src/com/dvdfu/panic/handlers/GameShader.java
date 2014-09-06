package com.dvdfu.panic.handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class GameShader extends ShaderProgram {
	public GameShader(String vertexShader, String fragmentShader) {
		super(Gdx.files.internal(vertexShader), Gdx.files.internal(fragmentShader));
		if (!isCompiled()) {
			Gdx.app.log("Shader compilation failed: ", getLog());
		}
		ShaderProgram.pedantic = false;
	}
}
