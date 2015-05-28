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

	public float getX()
	{
		return x;
	}
	public float getY()
	{
		return y;
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
	public boolean equals(Coord b)
	{
		return b.x == x && b.y == y;
	}
	
	@Override
	public String toString()
	{
		return "("+x+", "+y+")";
	}
}
