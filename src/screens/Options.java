package screens;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;

import ui_buttons.BigButton;
import ui_buttons.OptionsButton;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;

import data.GSB;

public class Options implements Screen
{
	OptionsButton ammo, brawl, IA, parkour, music, sound, advancedIA;
	BigButton exit;
	
	public static Properties p = new Properties();
	public Options()
	{
		
		loadProperties();
		
		ammo = new OptionsButton("Ammo : ");
		ammo.setLocation(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2+200);
		ammo.setActivated(get("ammo"));
		
		brawl = new OptionsButton("Brawl : ");
		brawl.setLocation(ammo.getX(), ammo.getY()-ammo.getHeight()*2);
		brawl.setActivated(get("brawl"));
		
		IA = new OptionsButton("AI : ");
		IA.setLocation(brawl.getX(), brawl.getY()-brawl.getHeight()*2);
		IA.setActivated(get("IA"));
		
		advancedIA = new OptionsButton("Advanced AI : ");
		advancedIA.setLocation(IA.getX(), IA.getY()-IA.getHeight()*2);
		advancedIA.setActivated(get("advancedIA"));
		
		parkour = new OptionsButton("Parkour : ");
		parkour.setLocation(advancedIA.getX(), advancedIA.getY()-advancedIA.getHeight()*2);
		parkour.setActivated(get("parkour"));
		
		music = new OptionsButton("Music : ");
		music.setLocation((Gdx.graphics.getWidth()*3)/4, brawl.getY());
		music.setActivated(get("music"));
		
		sound = new OptionsButton("Sound : ");
		sound.setLocation(music.getX(), music.getY()-music.getHeight()*2);
		sound.setActivated(get("sound"));
		
		exit = new BigButton("Back to main menu")
		{
			@Override
			protected void onClick()
			{
				p.setProperty("ammo", ammo.getValue()?"true":"false");
				p.setProperty("brawl", brawl.getValue()?"true":"false");
				p.setProperty("IA", IA.getValue()?"true":"false");
				p.setProperty("advancedIA", advancedIA.getValue()?"true":"flse");
				p.setProperty("parkour", parkour.getValue()?"true":"false");
				p.setProperty("music", music.getValue()?"true":"false");
				p.setProperty("sound", sound.getValue()?"true":"false");
				try
				{
					PrintWriter pw = new PrintWriter(Gdx.files.internal("settings.properties").file());
					pw.write("");
					pw.close();
					p.store(new FileOutputStream(Gdx.files.internal("settings.properties").file()), "SupergunArena properties");
					
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
				
				
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
			advancedIA.render();
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
		advancedIA.update();
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


	private static void forceNewProperties() throws IOException
	{
		Gdx.files.internal("settings.properties").file().createNewFile();
		p.setProperty("ammo", "false");
		p.setProperty("brawl", "true");
		p.setProperty("IA", "false");
		p.setProperty("parkour", "false");
		p.setProperty("music", "true");
		p.setProperty("sound", "true");
		p.setProperty("advancedIA", "false");
		p.store(new FileOutputStream(Gdx.files.internal("settings.properties").file()), "SupergunArena properties");
	}
	
	private static void loadProperties()
	{
		try
		{
			if(Gdx.files.internal("settings.properties").exists())
			{
				p.load(Gdx.files.internal("settings.properties").read());
			}
			else
			{
				forceNewProperties();
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public static boolean get(String string)
	{
		if(p.get(string) == null)
		{
			loadProperties();
		}
		if(p.get(string) == null)
		{
			try
			{
				forceNewProperties();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return p.get(string).equals("true");
	}
	
}
