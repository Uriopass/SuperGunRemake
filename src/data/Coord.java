package data;

import java.io.Serializable;

public class Coord implements Serializable
{
	private static final long serialVersionUID = 1L;
	int x, y;
	int additionalData; // If i want to transmit something
	
	public Coord(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	
	public int getX()
	{
		return x;
	}
	public int getY()
	{
		return y;
	}
	public void setX(int x)
	{
		this.x = x;
	}
	public void setY(int y)
	{
		this.y = y;
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
