package screens;

import java.util.prefs.BackingStoreException;

import ui_buttons.BigButton;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;

import data.GSB;
import data.SpriteManager;
import data.TextureManager;

public class Tutorial implements Screen
{
	BigButton exit;
	
	public Tutorial()
	{
		exit = new BigButton("Main Menu")
		{
			@Override
			protected void onClick()
			{
				((com.badlogic.gdx.Game)Gdx.app.getApplicationListener()).setScreen(new MainMenu());
			}
		};
		
		exit.setLocation(Gdx.graphics.getWidth()-exit.getWidth(), 0);
	}
	
	@Override
	public void render(float delta)
	{
		float grey = 0xc8/0xFF;
		Gdx.gl.glClearColor(grey, grey, grey, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		GSB.hud.begin();
		Texture t = TextureManager.get("tutorial.png");
		GSB.hud.draw(t, (Gdx.graphics.getWidth()-t.getWidth())/2, (Gdx.graphics.getHeight()-t.getHeight())/2);
		exit.render(0);
		GSB.hud.end();

		exit.update();
	}


	
	
	@Override
	public void resize(int width, int height)
	{

	}

	@Override
	public void pause()
	{

	}

	@Override
	public void show()
	{

	}

	@Override
	public void resume()
	{

	}

	@Override
	public void hide()
	{

	}

	@Override
	public void dispose()
	{

	}
}
