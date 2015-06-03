package map;

import java.io.Serializable;
import java.util.ArrayList;

import data.Coord;
import data.GSB;

public class Map implements Serializable
{
	private static final long serialVersionUID = 3L;
	ArrayList<Block> map = new ArrayList<Block>();
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
		map.add(new Block(c));
	}
	public void addBox(Coord c, int type)
	{
		map.add(new Block(c));
		map.get(map.size()-1).setType(type);
	}
	
	private int getNeighbour(Block c)
	{
		if(c.type == 1)
			return GROUND;
		boolean right = false, left = false;
		for(Block a : map)
		{
			if(a.type == c.type)
			{
				if(a.getY() == c.getY())
				{
					if(a.getX() == c.getX()-1)
						left = true;
					if(a.getX() == c.getX()+1)
						right = true;
				}
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
		for(Block c : map)
		{
			c.setFlag(getNeighbour(c));
		}
	}
	
	public ArrayList<Block> getBlocks()
	{
		return map;
	}
	
	public void render(float delta)
	{
		for(Block c : map)
		{
			c.render(delta);
		}
		if(GSB.srCam.isDrawing())
			GSB.srCam.end();
		if(GSB.sb.isDrawing())
			GSB.sb.end();
	}
}
