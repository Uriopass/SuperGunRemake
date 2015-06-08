package game;

import map.Block;
import map.Map;

import com.badlogic.gdx.math.Polygon;

public class AI
{
	float middlex = 0;
	public AI(Map m)
	{
		for(Block c : m.getBlocks())
		{
			middlex += c.getX();
		}
		middlex /= m.getBlocks().size();
		middlex *= 256;
	}
	
	float random, randomcount = 1;
	
	private boolean isInTheAir(Personnage pers)
	{
		for(Polygon p : pers.getCollisions())
		{
			float x = pers.getX();
			
			System.out.println(x + " "+  p.getX());
			
			if(x > p.getX() && x < p.getX()+256)
			{
				return false;
			}
		}
		return true;
	}
	
	private void iDontKnowWhatToDo(Personnage me, Personnage enemy, float delta)
	{
		me.jump();
		if(middlex > me.x)
			me.move(true, delta);
		else
			me.move(false, delta);
	}
	
	public void update(Personnage me, Personnage enemy, float delta)
	{
		randomcount -= delta;
		if(randomcount < 0)
		{
			random = (float) Math.random();
			randomcount = 1;
		}

		if(isInTheAir(enemy))
		{
			this.iDontKnowWhatToDo(me, enemy, delta);
		}
		else
		{
			if(Math.abs(me.x - enemy.x) > random*300)
			{
				if(enemy.x > me.x)
				{
					me.move(true, delta);
				}
				if(enemy.x < me.x)
				{
					me.move(false, delta);
				}
			}
			else
			{
				if(enemy.x > me.x)
				{
					me.move(false, delta);
				}
				if(enemy.x < me.x) 
				{
					me.move(true, delta);
				}
			}
		}
		if(enemy.y > me.y+10 && !me.jumping || isInTheAir(me))
		{
			me.jump();
		}
		
		if(!me.onGround() && Math.abs(me.vy) < 1 && enemy.y > me.y+10)
		{
			me.jump();
		}
		
		if(Math.abs(me.y - enemy.y) < 200)
		{
			me.fire();
		}
	}
}
