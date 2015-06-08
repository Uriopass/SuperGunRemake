package screens;

import java.util.ArrayList;

import particles.ParticleEmitter;
import ui_buttons.BigButton;
import ui_buttons.ScrollClass;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;

import data.GSB;

public class MainMenu implements Screen
{
	BigButton play, exit, options;
	ArrayList<ParticleEmitter> pe = new ArrayList<ParticleEmitter>();
	ParticleEmitter mouse;
	
	public MainMenu()
	{
		System.out.println();
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
		
		update(delta);
	}
	float ILIKETRAINS = 0;
	float speed = 1;
	int count = 0;
	private void update(float delta)
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
