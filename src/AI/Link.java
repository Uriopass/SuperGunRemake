package AI;

import data.Coord;

public class Link
{
	Coord a, b;
	public Link(Coord a, Coord b)
	{
		this.a = a;
		this.b = b;
	}
	
	public Coord a()
	{
		return a;
	}
	
	public Coord b()
	{
		return b;
	}
}
