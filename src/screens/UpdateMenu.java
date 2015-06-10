package screens;

import java.net.MalformedURLException;
import java.net.URL;

import ui_buttons.BigButton;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.utils.Align;

import data.Download;
import data.FontManager;
import data.GSB;

public class UpdateMenu implements Screen
{
	BigButton ok;
	Download d;
	String version;
	
	public UpdateMenu(String version)
	{
		this.version = version;
		ok = new BigButton("Exit")
		{
			@Override
			protected void onClick()
			{
				Gdx.app.exit();
			}
		};
		
		ok.setLocation(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);
		ok.center(true, true);
		
		try
		{
			d = new Download(new URL("http://lablanchisserie.fr/Parissou/SupergunRemake/supergun.jar"));
			d.setFileName("supergun"+version+".jar");
			d.download();
		}
		catch (MalformedURLException e)
		{
			e.printStackTrace();
		}
	}
	
	boolean ended = false;
	
	@Override
	public void render(float delta)
	{
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
 
		if(ended)
		{
			GSB.hud.begin();
				FontManager.get(20).draw(GSB.hud, "Download finished, press ok to continue. new version downloaded to supergun"+version+".jar", Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2+100, 0, Align.center, false);

				ok.render(0);
			GSB.hud.end();
		
		}
		else
		{
			GSB.hud.begin();
			FontManager.get(20).draw(GSB.hud, "Downloading...", Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2+100, 0, Align.center, false);
			FontManager.get(20).draw(GSB.hud, (int)(d.getProgress())+"%", Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2-100, 0, Align.center, false);
			GSB.hud.end();
			
			int width = 400;
			
			GSB.srHud.begin(ShapeType.Filled);
				GSB.srHud.setColor(Color.GREEN);
				GSB.srHud.rect(Gdx.graphics.getWidth()/2-width/2, Gdx.graphics.getHeight()/2-10, (d.getProgress()/100)*width, 20);
			GSB.srHud.end();
		}
		update(delta);
	}
	
	public void update(float delta)
	{
		ended = d.getStatus() == Download.COMPLETE;
		
		if(ended)
		{
			ok.update();
		}
	}

	@Override
	public void resize(int width, int height)
	{
		
	}

	@Override
	public void show()
	{
		
	}
	@Override
	public void pause()
	{
	}

	@Override
	public void resume()
	{
	}
	
	@Override
	public void dispose()
	{
		
	}

	@Override
	public void hide()
	{
		
	}
	
}
