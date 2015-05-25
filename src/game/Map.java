package game;

import java.io.Serializable;
import java.util.ArrayList;

import data.Coord;
import data.GSB;
import data.TextureManager;

public class Map implements Serializable
{
	private static final long serialVersionUID = 2L;
	ArrayList<Coord> map = new ArrayList<Coord>();
	Coord gentil, mechant;
	
	public Map()
	{
		
	}
	
	public void setPlayersPosition(Coord gentil, Coord mechant)
	{
		this.gentil = gentil;
		this.mechant = mechant;
	}
	
	public Coord getGentilPos()
	{
		return gentil;
	}
	
	public Coord getMechantPos()
	{
		return mechant;
	}
	
	public void addBox(Coord c)
	{
		map.add(c);
	}
	private int getNeighbour(Coord c)
	{
		boolean right = false;
		boolean left = false;
		for(Coord a : map)
		{
			if(a.getY() == c.getY())
			{
				if(a.getX() == c.getX()-1)
					left = true;
				if(a.getX() == c.getX()+1)
					right = true;
			}
		}
		if(right && left)
			return 2;
		if(right)
			return 1;
		if(left)
			return -1;
		return 0;
	}
	
	public void computeTypes()
	{
		for(Coord c : map)
		{
			c.setData(getNeighbour(c));
		}
	}
	
	public ArrayList<Coord> getCoords()
	{
		return map;
	}
	
	public void render()
	{
		for(Coord c : map)
		{
			String path = "";
			switch(c.getData())
			{
				case 2:
					path = "sol.png";
					break;
				case 1:
					path = "bord_g.png";
					break;
				case -1:
					path = "bord_d.png";
					break;
				default:
					path = "sol.png";
					break;
			}
			GSB.sb.draw(TextureManager.get(path), c.getX()*256, c.getY()*256);
		}
	}
}
