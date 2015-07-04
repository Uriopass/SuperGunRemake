package data;


import java.io.Serializable;

public class Coord implements Serializable
{
	private static final long serialVersionUID = 1L;
	float x, y;
	int additionalData; // If i want to transmit something
	
	public Coord(float f, float g)
	{
		this.x = f;
		this.y = g;
	}
	
	public Coord(Coord coord)
	{
		this.x = coord.x;
		this.y = coord.y;
		this.additionalData = coord.additionalData;
	}
	
	public void add(float x, float y)
	{
		this.x += x;
		this.y += y;
	}
	
	public void add(Coord c)
	{
		add(c.getX(), c.getY());
	}

	public float getX()
	{
		return x;
	}
	public float getY()
	{
		return y;
	}
	
	public int X()
	{
		return (int)x;
	}
	
	public int Y()
	{
		return (int)y;
	}
	
	public void setX(float x)
	{
		this.x = x;
	}
	public void setY(float f)
	{
		this.y = f;
	}
	public void setData(int data)
	{
		additionalData = data;
	}
	public int getData()
	{
		return additionalData;
	}
	
	@Override
	public int hashCode() 
	{
		int hashCode = 0;
	    hashCode = (hashCode * 397) ^ ((Float) x).hashCode();
	    hashCode = (hashCode * 397) ^ ((Float) y).hashCode();
	    return hashCode;
	}
	
	@Override
	public boolean equals(Object b)
	{
		if(b instanceof Coord)
		{
			Coord casted = (Coord) b;
			return casted.x == x && casted.y == y;
		}
		return false;
	}
	
	@Override
	public String toString()
	{
		return "("+x+", "+y+")";
	}

	public float distanceTo(float x2, float y2)
	{
		return (float)Math.sqrt((x2-x)*(x2-x) + (y2-y)*(y2-y));
	}
}
