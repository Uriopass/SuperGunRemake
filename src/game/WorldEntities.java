package game;

import java.util.ArrayList;

import com.badlogic.gdx.math.Polygon;

import data.GSB;
import entities.Entity;

public class WorldEntities
{
	ArrayList<Entity> entities;
	ArrayList<Player> toTest;
	
	public WorldEntities()
	{
		entities = new ArrayList<Entity>();
		toTest = new ArrayList<Player>();
	}
	
	public void addEntity(Entity e)
	{
		entities.add(e);
	}
	
	public void addPersonnage(Player p)
	{
		toTest.add(p);
	}
	
	public void update(float delta)
	{
		for(Entity entity : entities)
		{
			entity.update(delta);
		}
		
		for(Entity e : entities)
		{	
			for(Polygon p : toTest.get(0).getCollisions())
			{
				if(e.polygonTest(p))
				{
					e.onPolygonCollision(p);
				}
			}
			
			for(Player p : toTest)
			{
				if(e.playerTest(p))
				{
					e.onPlayerHit(p);
				}
			}
		}
		
		for(int i = 0 ; i < entities.size() ; i++)
		{
			if(entities.get(i).delete)
			{
				entities.remove(i);
				i--;
			}
		}
	}
	
	public void render()
	{
		GSB.sb.begin();
		for(Entity entity : entities)
		{
			entity.render();
		}
		GSB.sb.end();
	}
}
