package screens;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import particles.ParticleEmitter;
import ui_buttons.BigButton;
import ui_buttons.ScrollClass;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.Align;

import data.Coord;
import data.FontManager;
import data.GSB;

public class MainMenu implements Screen
{
	BigButton play, exit, options;
	ArrayList<ParticleEmitter> pe = new ArrayList<ParticleEmitter>();
	ParticleEmitter mouse;
	
	String version = "0.9", downloadedVersion = "ERROR";
	BigButton update, doNotUpdate;
	
	boolean updateMenu = false;
	
	public MainMenu()
	{
		this.checkForUpdate();
		
		play = new BigButton("Play")
		{
			@Override
			protected void onClick()
			{
				((com.badlogic.gdx.Game)Gdx.app.getApplicationListener()).setScreen(new MapMenu());
			}
		};
		play.setLocation(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2+100);
		play.center(true, false);
		
		options = new BigButton("Options")
		{
			protected void onClick() 
			{
				((com.badlogic.gdx.Game)Gdx.app.getApplicationListener()).setScreen(new Options());
			};
		};

		options.setLocation(play.getX(), play.getY()-play.getHeight());
		
		
		exit = new BigButton("Exit")
		{
			@Override
			protected void onClick()
			{
				Gdx.app.exit();
			}
		};
		exit.setLocation(options.getX(), options.getY()-options.getHeight());

		update = new BigButton("Update")
		{
			@Override
			protected void onClick()
			{
				((com.badlogic.gdx.Game)Gdx.app.getApplicationListener()).setScreen(new UpdateMenu(downloadedVersion));
			}
		};
		update.setLocation(play.getX(), play.getY());
		
		doNotUpdate = new BigButton("Don't update")
		{
			@Override
			protected void onClick()
			{
				updateMenu = false;
			}
		};
		doNotUpdate.setLocation(options.getX(), options.getY());
		
			pe.add(new ParticleEmitter(play.getX() + play.getWidth()/2, play.getY()+25, 3f));
			pe.get(0).setRate(5);
		
		mouse = new ParticleEmitter(0, 0, 2f);
		mouse.enableGravity(-.1f);
		mouse.setRate(100);
		
	}
	

	@Override
	public void render(float delta)
	{
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		if(updateMenu)
		{
			GSB.hud.begin();
			FontManager.get(25).draw(GSB.hud, "A more recent update is available", Gdx.graphics.getWidth()/2, update.getY()+200, 0, Align.center, false);
			doNotUpdate.render(0);
			update.render(0);
			GSB.hud.end();
		}
		else
		{
			if(play.isHovered())
			{
				for(ParticleEmitter p : pe)
					p.startEmitting();
			}
			else
			{
				for(ParticleEmitter p : pe)
					p.stopEmitting();
			}
			for(ParticleEmitter p : pe)
				p.render();
			mouse.render();
			GSB.hud.begin();
				play.render(0);
				options.render(0);
				exit.render(0);
			GSB.hud.end();
		}
		update(delta);
	}
	float ILIKETRAINS = 0;
	float speed = 1;
	int count = 0;
	private void update(float delta)
	{
		if(updateMenu)
		{
			update.update();
			doNotUpdate.update();
		}
		else
		{
			ILIKETRAINS+=.03f*Math.sin(speed);
			speed+=.01f;
			play.update();
			exit.update();
			options.update();
			if(Gdx.input.isButtonPressed(Input.Buttons.LEFT))
			{
				mouse.setX(Gdx.input.getX());
				mouse.setY(Gdx.graphics.getHeight()-Gdx.input.getY());
				mouse.startEmitting();
			}
			else
			{
				mouse.stopEmitting();
			}
			
			mouse.setRate(mouse.getRate() + -ScrollClass.getScroll());
			//cell1.rotate(5);
			//cell2.rotate(-5);
			count = 0;
			for(ParticleEmitter p : pe)
			{
				p.update(delta);
				count += p.getParticleCount();
			}
			mouse.update(delta);
			count += mouse.getParticleCount();
		}
	}
	
	private void checkForUpdate()
	{
		String url = "http://lablanchisserie.fr/Parissou/SupergunRemake/version.txt";
		try
		{
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
 
		con.setRequestMethod("GET");
 
		con.setRequestProperty("User-Agent", "Java");
 
		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
 
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		
		this.updateMenu = !version.equals(response.toString());
		if(updateMenu)
		{
			downloadedVersion = response.toString();
			System.out.println("[LOAD][UPDATE] An update is available !" + version + " " + response.toString());
		}
		}
		catch(Exception e )
		{
			e.printStackTrace();
		}
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
