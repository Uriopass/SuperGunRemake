package com.uriopass.supergun;

import screens.MainMenu;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Supergun extends Game {
	SpriteBatch batch;
	Texture img;
	
	@Override
	public void create () {
		screens.Game.initCameraAndGSB();
		this.setScreen(new MainMenu());
	}
}
