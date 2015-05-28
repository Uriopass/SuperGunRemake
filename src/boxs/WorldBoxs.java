package boxs;

import game.Map;
import game.Personnage;

import java.util.ArrayList;

import com.badlogic.gdx.math.Polygon;

import data.Coord;

public class WorldBoxs
{
	ArrayList<Box> boxs;
	Map m;
	float time = 0;
	static float maxY;
	static float minx, maxx;
	
	public WorldBoxs()
	{
		boxs = new ArrayList<Box>();
	}
	
	public static void setMaxY(float maxy)
	{
		WorldBoxs.maxY = maxy;
	}
	public static void setMaxx(float maxx)
	{
		WorldBoxs.maxx = maxx;
	}
	public static void setMinx(float minx)
	{
		WorldBoxs.minx = minx;
	}
	public void setMap(Map m)
	{
		this.m = m;
	}
	
	public void update(float delta, Personnage gentil, Personnage mechant)
	{
		time += delta;
		if(time > 1)
		{
			time -= 1;
			double random = Math.random();
			if(random < AmmunitionBox.probability)
			{
				boxs.add(new AmmunitionBox(getCoord()));
			}
			if(random < HealthBox.probability)
			{
				boxs.add(new HealthBox(getCoord()));
			}
			if(random < SniperBox.probability)
			{
				boxs.add(new SniperBox(getCoord()));
			}
		}
		
		for(Box b : boxs)
		{
			if(!b.onground)
			{
				b.position.setY(b.position.getY() - 10);
			}
			for(Polygon p : gentil.getCollisions())
			{
				if(gentil.isCollision(p, b.getBoundingBox()))
				{
					b.onground = true;
				}
			}
		}
		
		gentil.testBoxs(boxs);
		mechant.testBoxs(boxs);
	}
	
	private Coord getCoord()
	{
		Coord c = new Coord((float) (Math.random()*(maxx-minx)+minx), maxY);
		return c;
	}
	
	public void render()
	{
		for(Box b : boxs)
		{
			b.render();
		}
	}
}
