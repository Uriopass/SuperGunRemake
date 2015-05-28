package game;

import java.io.Serializable;
import java.util.ArrayList;

import data.Coord;
import data.GSB;
import data.TextureManager;

public class Map implements Serializable
{
	private static final long serialVersionUID = 3L;
	ArrayList<Coord> map = new ArrayList<Coord>();
	Coord gentil, mechant;
	
	public static final int GROUND = 8, LEFT = 2, RIGHT = 1;
	
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
		boolean right = false, left = false;
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
		int flag = GROUND;
		
		if(!(right && left))
		{
			if(right)
			{
				flag = RIGHT;
			}
			if(left)
			{
				flag = LEFT;
			}
		}
		return flag;
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
			String path = "sol.png";
			int flag = c.getData();
			
			if(flag == RIGHT)
			{
				path = "bord_g.png";
			}
			if(flag == LEFT)
			{
				path = "bord_d.png";
			}
			
			GSB.sb.draw(TextureManager.get(path), c.getX()*256, c.getY()*256);
		}
	}
}
