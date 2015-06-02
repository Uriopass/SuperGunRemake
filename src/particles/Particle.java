package particles;

import com.badlogic.gdx.graphics.Color;

import data.GSB;
import data.TextureManager;

public class Particle
{
	public float x, y;
	float vx, vy;
	Color c;
	float gravity = 0;
	float life = 2;
	String texture = null;
	
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
	
	public void setLife(float life)
	{
		this.life = life;
	}
	
	public void updateParticle(float delta)
	{
		life -= delta;
		x += vx*delta*60;
		y += vy*delta*60;
		
		vy += gravity;
	}
	
	public void enableGravity(float gravity)
	{
		this.gravity = gravity;
	}
	
	public boolean dieCondition()
	{
		if(life < 0)
			return true;
		return false;
	}

	public void render()
	{
		if(texture == null)
		{
			GSB.sr.setColor(c);
			GSB.sr.rect(x, y, 2, 2);
		}
		else
		{
			GSB.sb.draw(TextureManager.get(texture), x, y);
		}
	}

	public void setTexture(String texture)
	{
		this.texture = texture;
	}
}