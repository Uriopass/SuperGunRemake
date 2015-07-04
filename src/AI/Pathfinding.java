package AI;


import java.util.HashMap;

import data.Coord;

public class Pathfinding
{	
	public static Path getPath(Coord start, Coord end, HashMap<Coord, Node> nodes, Links links)
	{
		for(Coord coord : nodes.keySet())
		{
			Node n = nodes.get(coord);
			n.available = true;
			n.distance = 1000000;
			n.previous = null;
		}
		nodes.get(start).distance = 0;
		while(nodes.get(end).available)
		{
			Node n = null;
			float minDist = 100000000;
			for(Coord c : nodes.keySet())
			{
				Node a = nodes.get(c);
				if(a.distance < minDist && a.available)
				{
					n = a;
					minDist = a.distance;
				}
			}
			n.available = false;
			
			for(Coord c : links.getLinks(n.pos))
			{
				Node a = nodes.get(c);
				if(a.distance > n.distance + distance(a, n))
				{
					a.distance = n.distance + distance(a, n);
					a.previous = n;
				}
			}
		}
		Path p = new Path();
		Node n = nodes.get(end);

		try
		{
			while(!n.pos.equals(start))
			{
				p.addCoord(new Coord(n.pos));
				n = n.previous;
			}
			p.addCoord(new Coord(n.pos));
			p.reverse();
			return p;		
		}
		catch(NullPointerException e)
		{
			return null;
		}
	}
    private static float distance(Node a, Node b)
	{
		return (float) Math.sqrt((a.pos.getX()-b.pos.getX())*(a.pos.getX()-b.pos.getX()) + (a.pos.getY() - b.pos.getY())*(a.pos.getY() - b.pos.getY()));
	}
}
