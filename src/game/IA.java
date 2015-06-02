package game;

public class IA
{
	public IA()
	{
		
	}
	
	float random, randomcount = 1;
	
	public void update(Personnage me, Personnage enemy, float delta)
	{
		randomcount -= delta;
		if(randomcount < 0)
		{
			random = (float) Math.random();
			randomcount = 1;
		}
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
		if(enemy.y > me.y && !me.jumping)
		{
			me.jump();
		}
		
		if(!me.onGround() && Math.abs(me.vy) < 1 && enemy.y > me.y)
		{
			me.jump();
		}
		
		if(Math.abs(me.y - enemy.y) < 200)
		{
			me.fire();
		}
	}
}
