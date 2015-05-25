package particles;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

import data.GSB;

public class ParticleEmitter
{
	int x, y, rate = 1;
	float power, deceleration;
	boolean emitting = false;
	ArrayList<Particle> particles = new ArrayList<Particle>();
	public ParticleEmitter(int x, int y, float power, float deceleration)
	{
		this.x = x;
		this.y = y;
		this.power = power;
		this.deceleration = deceleration;
	}
	
	public void startEmitting()
	{
		emitting = true;
	}
	
	public void stopEmitting()
	{
		emitting = false;
	}
	
	public void setX(int x)
	{
		this.x = x;
	}
	public void setY(int y)
	{
		this.y = y;
	}
	
	public void setRate(int rate)
	{
		this.rate = rate;
	}
	
	public void update()
	{
		if(emitting)
		{
			for(int i = 0 ; i < rate ; i++)
			{
				Particle toAdd = new Particle(x, y);
				toAdd.setColor(new Color((float)Math.random(), (float)Math.random(), (float)Math.random(), 1));
				
				float angle = (float) (Math.random()*Math.PI*2);
				toAdd.setSpeed((float)(Math.cos(angle)*(Math.random()+.5))*power, (float)(Math.sin(angle)*(Math.random()+1))*power);
				
				toAdd.setDeceleration(deceleration);
				particles.add(toAdd);
			}
		}
		for(int i = 0 ; i < particles.size() ; i++)
		{
			if(particles.get(i).dieCondition())
			{
				particles.remove(i);
			}
		}
		
		for(Particle p : particles)
		{
			p.updateParticle();
		}
	}
	
	public void render()
	{
		GSB.sr.begin(ShapeType.Filled);
		for(Particle p : particles)
		{
			p.render();
		}
		GSB.sr.end();
	}

	public int getParticleCount()
	{
		return particles.size();
	}

	public int getRate()
	{
		return rate;
	}
}
