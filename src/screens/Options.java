package screens;

import ui_buttons.BigButton;
import ui_buttons.OptionsButton;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;

import data.GSB;

public class Options implements Screen
{
	public static boolean ammoActivated = false;
	public static boolean brawlModeActivated = true;
	public static boolean IAActivated = false;
	public static boolean parkourActivated = false;
	public static boolean musicActivated = true;
	public static boolean soundActivated = true;
	OptionsButton ammo, brawl, IA, parkour, music, sound;
	BigButton exit;
	
	public Options()
	{
		ammo = new OptionsButton("Ammo : ");
		ammo.setLocation(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2+200);
		ammo.setActivated(ammoActivated);
		
		brawl = new OptionsButton("Brawl : ");
		brawl.setLocation(ammo.getX(), ammo.getY()-ammo.getHeight()*2);
		brawl.setActivated(brawlModeActivated);
		
		IA = new OptionsButton("AI : ");
		IA.setLocation(brawl.getX(), brawl.getY()-brawl.getHeight()*2);
		IA.setActivated(IAActivated);
		
		parkour = new OptionsButton("Parkour : ");
		parkour.setLocation(IA.getX(), IA.getY()-IA.getHeight()*2);
		parkour.setActivated(parkourActivated);
		
		music = new OptionsButton("Music : ");
		music.setLocation((Gdx.graphics.getWidth()*3)/4, brawl.getY());
		music.setActivated(musicActivated);
		
		sound = new OptionsButton("Sound : ");
		sound.setLocation(music.getX(), music.getY()-music.getHeight()*2);
		sound.setActivated(soundActivated);
		
		exit = new BigButton("Back to main menu")
		{
			@Override
			protected void onClick()
			{
				ammoActivated = ammo.getValue();
				brawlModeActivated = brawl.getValue();
				IAActivated = IA.getValue();
				parkourActivated = parkour.getValue();
				musicActivated = music.getValue();
				soundActivated = sound.getValue();
				
				((com.badlogic.gdx.Game)Gdx.app.getApplicationListener()).setScreen(new MainMenu());
			}
		};
		exit.setLocation(Gdx.graphics.getWidth()/2, parkour.getY()-parkour.getHeight()*2);
		exit.center(true, false);
	}
	
	@Override
	public void render(float delta)
	{
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		GSB.hud.begin();
			ammo.render();
			brawl.render();
			exit.render(0);
			IA.render();
			parkour.render();
			music.render();
			sound.render();
		GSB.hud.end();
		
		update(delta);
	}
	
	public void update(float delta)
	{
		ammo.update();
		brawl.update();
		IA.update();
		exit.update();
		parkour.update();
		music.update();
		sound.update();
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
