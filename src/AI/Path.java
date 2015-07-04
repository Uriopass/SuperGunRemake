package AI;


import java.util.ArrayList;
import java.util.Collections;

import data.Coord;
import data.GSB;

public class Path
{
	ArrayList<Coord> path;
	public Path()
	{
		path = new ArrayList<Coord>();
	}
	
	public void addCoord(Coord coord)
	{
		path.add(coord);
	}
	
	public ArrayList<Coord> getPath()
	{
		return path;
	}
	
	public boolean exists()
	{
		return path.isEmpty();
	}
	
	public void clear()
	{
		path.clear();
	}
	
	public Coord getLastCoord()
	{
		return path.get(path.size()-1);
	}

	public Coord getCoord(int i)
	{
		if(i == -1)
			return null;
		if(path.size() > i)
			return path.get(i);
		else {
			return getCoord(i-1);
		}
	}
	
	public Coord getFirstCoord()
	{
		if(path.size() > 0)
			return path.get(0);
		else {
			return null;
		}
	}
	
	public Coord getSecondCoord()
	{
		if(path.size() > 1)
			return path.get(1);
		else
			return getFirstCoord();
	}
	
	@Override
	public String toString()
	{
		String s = "";
		for(Coord c : path)
			s += c.toString();
		return s;
	}

	public void renderPath()
	{
		float count = 0;
		for(Coord c : path)
		{
			GSB.srCam.setColor(0, (count++)/path.size(), 0, 1);
			GSB.srCam.circle(c.X()*256, c.Y()*256, 40);
		}
	}
	
	public void reverse()
	{
		Collections.reverse(path);
	}

}
