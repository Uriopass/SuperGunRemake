package screens;

import java.awt.Point;

import ui_buttons.BigButton;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;

import data.GSB;

public class Options implements Screen
{
	public static boolean ammoActivated = false;
	public static boolean brawlModeActivated = true;
	public static boolean IAActivated = true;
	BigButton ammo, brawl, IA, exit;
	
	public Options()
	{
		ammo = new BigButton(ammoActivated?"Disable ammo":"Enable ammo")
		{
			@Override
			protected void onClick()
			{
				ammoActivated = !ammoActivated;
				this.setName(ammoActivated?"Disable ammo":"Enable ammo");
			}
		};
		ammo.setLocation(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2+100);
		ammo.center(true, false);
		
		brawl = new BigButton(brawlModeActivated?"Disable brawl":"Enable brawl")
		{
			@Override
			protected void onClick()
			{
				brawlModeActivated = !brawlModeActivated;
				this.setName(brawlModeActivated?"Disable brawl":"Enable brawl");
			}
		};
		brawl.setLocation(ammo.getX(), ammo.getY()-ammo.getHeight());
		IA = new BigButton(IAActivated?"Disable IA":"Enable IA")
		{
			@Override
			protected void onClick()
			{
				IAActivated = !IAActivated;
				this.setName(IAActivated?"Disable IA":"Enable IA");
			}
		};
		IA.setLocation(brawl.getX(), brawl.getY()-brawl.getHeight());
		
		exit = new BigButton("Back to main menu")
		{
			@Override
			protected void onClick()
			{
				((com.badlogic.gdx.Game)Gdx.app.getApplicationListener()).setScreen(new MainMenu());
			}
		};
		exit.setLocation(IA.getX(), IA.getY()-IA.getHeight()*2);
		
		
	}
	
	Point lastClick = new Point(0, 0);
	
	@Override
	public void render(float delta)
	{
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		GSB.hud.begin();
			ammo.render(0);
			brawl.render(0);
			exit.render(0);
			IA.render(0);
		GSB.hud.end();
		
		update(delta);
	}
	
	public void update(float delta)
	{
		ammo.update();
		brawl.update();
		IA.update();
		exit.update();
	}
	@Override
	public void show()
	{
		
	}
	public void resize(int width, int height)
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
	public void hide()
	{
		
	}
	@Override
	public void dispose()
	{
		
	}
	
}
