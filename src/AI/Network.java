package AI;
import java.util.HashMap;

import com.badlogic.gdx.graphics.Color;

import data.Coord;
import data.GSB;


public class Network
{
	HashMap<Coord, Node> nodes;
	Links links;
	
	
	public Network()
	{
		nodes = new HashMap<Coord, Node>();
		links = new Links();
	}
	
	public void add(Node n)
	{
		nodes.put(n.pos, n);
	}
	
	public void addLink(Coord a, Coord b)
	{
		links.addLink(a, b);
	}

	public void remove(Coord a)
	{
		if(nodes.containsKey(a))
		{
			nodes.remove(a);
			links.remove(a);
		}
	}

	public void renderNetwork(Coord me, Coord you)
	{
		for(Coord c : nodes.keySet())
		{
			if(c.equals(you) || c.equals(me))
				GSB.srCam.setColor(Color.RED);
			else
				GSB.srCam.setColor(Color.BLUE);
			GSB.srCam.circle(c.getX()*256, c.getY()*256, 30);	
		}
	}

	public void renderLinks()
	{
		GSB.srCam.setColor(Color.RED);
		links.renderLinks();
	}

	/**
	 * @param x
	 * @param y
	 * @return
	 */
	public Coord getNearest(float x, float y)
	{
		Coord mem = null;
		float mindistance = 10000000;
		for(Coord c : nodes.keySet())
		{
			float distance = c.distanceTo(x, y);
			if(distance < mindistance)
			{
				mindistance = distance;
				mem = c;
			}
		}
		return mem;
	}
}
