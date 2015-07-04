package AI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import data.Coord;
import data.GSB;

public class Links
{
	class Link
	{
		Coord a, b;
		public Link(Coord a, Coord b)
		{
			this.a = a;
			this.b = b;
		}
		
		public boolean linked(Coord c, Coord d)
		{
			return (a.equals(c) && b.equals(d)) || (a.equals(d) && b.equals(c));
		}
		
		public boolean hasNode(Coord c)
		{
			return a.equals(c) || b.equals(c);
		}
	}
	ArrayList<Link> links;
	public Links()
	{
		links = new ArrayList<Link>();
	}
	
	public void addLink(Coord a, Coord b)
	{
		links.add(new Link(a, b));
	}
	
	public boolean isLinked(Coord a, Coord b)
	{
		for(Link l : links)
		{
			if(l.linked(a, b))
			{
				return true;
			}
		}
		return false;
	}

	public void remove(Coord a)
	{
		for(int i = 0 ; i < links.size() ; i++)
		{
			if(links.get(i).hasNode(a))
			{
				links.remove(i);
				i--;
			}
		}
	}

	public void renderLinks()
	{
		for(Link l : links)
		{
			GSB.srCam.line(l.a.getX()*256, l.a.getY()*256, l.b.getX()*256, l.b.getY()*256);
		}
	}

	public Set<Coord> getLinks(Coord pos)
	{
		Set<Coord> toReturn = new HashSet<Coord>();
		
		for(Link l : links)
		{
			if(l.a.equals(pos))
			{
				toReturn.add(l.b);
			}
			if(l.b.equals(pos))
			{
				toReturn.add(l.a);
			}
		}
		return toReturn;
	}

	public void clear(HashMap<Coord, Node> nodes)
	{
		for(int i = 0 ; i < links.size() ; i++)
		{
			Link l = links.get(i);
			if(nodes.get(l.a) == null || nodes.get(l.b) == null)
			{
				links.remove(i);
				i--;
			}
		}
	}
}
