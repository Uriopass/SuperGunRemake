package particles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;

import data.GSB;

public class Particle
{
	public float x, y;
	float vx, vy;
	float deceleration;
	Color c;

	public Particle(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	
	public void setColor(Color c)
	{
		this.c = c;
	}
	
	public void setSpeed(float x, float y)
	{
		this.vx = x;
		this.vy = y;
	}
	public void setDeceleration(float deceleration)
	{
		this.deceleration = deceleration;
	}
	public void updateParticle()
	{
		x += vx;
		y += vy;
		if(vx < 0)
		{
			vx += deceleration;
			if(vx > 0)
				vx = 0;
		}
		else
		{
			vx -= deceleration;
			if(vx < 0)
				vx = 0;
		}
		if(vy < 0)
		{
			vy += deceleration;
			if(vy > 0)
				vy = 0;
		}
		else
		{
			vy -= deceleration;
			if(vy < 0)
				vx = 0;
		}
	}
	
	public boolean dieCondition()
	{
		if(x < 0 || y < 0 || x > Gdx.graphics.getWidth() || y > Gdx.graphics.getHeight() || Math.abs(vx)+Math.abs(vy) <= deceleration)
			return true;
		return false;
	}

	public void render()
	{
		GSB.sr.setColor(c);
		GSB.sr.rect(x, y, 2, 2);
	}
}