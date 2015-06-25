package game;

import java.util.ArrayList;
import java.util.Comparator;

import com.badlogic.gdx.math.Polygon;

import data.GSB;
import entities.Entity;

public class WorldEntities
{
	ArrayList<Entity> entities;
	ArrayList<Personnage> toTest;
	
	public WorldEntities()
	{
		entities = new ArrayList<Entity>();
		toTest = new ArrayList<Personnage>();
	}
	
	public void addEntity(Entity e)
	{
		entities.add(e);
		entities.sort(new Comparator<Entity>() {

			@Override
			public int compare(Entity o1, Entity o2)
			{
				if(o1.id > o2.id)
					return 1;
				if(o1.id == o2.id)
					return 0;
				return -1;
			}
		});
	}
	
	public void addPersonnage(Personnage p)
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
			
			for(Personnage p : toTest)
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
