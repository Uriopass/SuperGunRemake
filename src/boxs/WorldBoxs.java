package boxs;

import game.Map;
import game.Personnage;

import java.util.ArrayList;

import data.Coord;

public class WorldBoxs
{
	ArrayList<Box> boxs;
	Map m;
	float time = 0;
	
	public WorldBoxs()
	{
		boxs = new ArrayList<Box>();
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
		
		gentil.testBoxs(boxs);
		mechant.testBoxs(boxs);
	}
	
	private Coord getCoord()
	{
		int size = m.getCoords().size();
		int index = (int)(Math.random()*size);
		
		return m.getCoords().get(index);
	}
	
	public void render()
	{
		for(Box b : boxs)
		{
			b.render();
		}
	}
}
