package particles;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

import data.GSB;

public class ParticleEmitter
{
	int x, y, rate = 1;
	float power;
	boolean emitting = false;
	ArrayList<Particle> particles = new ArrayList<Particle>();
	float gravity = 0, life = 3;
	float emitTime = 1000;
	
	Color forced = null;
	String texture = "";
	
	public ParticleEmitter(int x, int y, float power)
	{
		this.x = x;
		this.y = y;
		this.power = power;
	}
	
	public void startEmitting()
	{
		emitting = true;
		emitTime = 1000;
	}
	
	public void startEmitting(float time)
	{
		emitting = true;
		emitTime = time;
	}
	
	public void enableGravity(float gravity)
	{
		this.gravity = gravity;
	}
	
	public void stopEmitting()
	{
		emitting = false;
	}
	
	public void setForcedColor(Color forced)
	{
		this.forced = forced;
	}
	
	public void setLife(float life)
	{
		this.life = life;
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
	
	public void setTexture(String path)
	{
		this.texture = path;
	}
	
	public void update(float delta)
	{
		emitTime -= delta;
		if(emitTime <= 0)
			emitting = false;
		if(emitting)
		{
			for(int i = 0 ; i < rate ; i++)
			{
				Particle toAdd = new Particle(x, y);
				toAdd.setColor(new Color((float)Math.random(), (float)Math.random(), (float)Math.random(), 1));
				
				float angle = (float) (Math.random()*Math.PI*2);
				float tmpPower = power;
				tmpPower *= (Math.random()+.5);
				toAdd.setSpeed((float)(Math.cos(angle)*tmpPower), (float)(Math.sin(angle)*tmpPower));
				toAdd.enableGravity(gravity);
				toAdd.setLife(life);
				if(texture != "")
				{
					toAdd.setTexture(texture);
				}
				if(forced != null)
				{
					toAdd.c = forced;
				}
				
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
			p.updateParticle(delta);
		}
	}
	
	
	
	public void render()
	{
		GSB.srCam.begin(ShapeType.Filled);
		for(Particle p : particles)
		{
			p.render();
		}
		GSB.srCam.end();
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
