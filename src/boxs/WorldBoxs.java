package boxs;

import game.Personnage;

import java.util.ArrayList;

import map.Map;
import screens.Options;

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
			if(Math.random() < AmmunitionBox.probability && Options.ammoActivated)
			{
				boxs.add(new AmmunitionBox(getCoord()));
			}
			if(Math.random() < HealthBox.probability)
			{
				boxs.add(new HealthBox(getCoord()));
			}
			if(Math.random() < SniperBox.probability)
			{
				boxs.add(new SniperBox(getCoord()));
			}

			if(Math.random() < ShotgunBox.probability)
			{
				boxs.add(new ShotgunBox(getCoord()));
			}
			
			if(Math.random() < RifleBox.probability)
			{
				boxs.add(new RifleBox(getCoord()));
			}
		}
		
		for(Box b : boxs)
		{
			if(!b.onground)
			{
				b.position.setY(b.position.getY() - 10*delta*60);
			}
			for(Polygon p : gentil.getCollisions())
			{
				if(gentil.isCollision(p, b.getBoundingBox()))
				{
					while(gentil.isCollision(p, b.getBoundingBox()))
					{
						b.position.setY(b.position.getY() + 3);
					}
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
