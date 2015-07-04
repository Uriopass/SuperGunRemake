package AI;


import data.Coord;

public class Node
{
	public Coord pos;
	
	Node previous = null;
	float distance = 0;
	boolean available = true;
	
	public Node(float x, float y)
	{
		pos = new Coord(x, y);
	}
}
