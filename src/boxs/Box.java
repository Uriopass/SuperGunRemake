package boxs;

import game.Personnage;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;

import data.Coord;
import data.GSB;

public class Box
{
	Sprite image;
	Coord position;
	boolean onground = false;
	
	public Box(Coord pos)
	{
		position = new Coord(pos.getX()*256+128, pos.getY()*256+235);
		setImage();
		image.setCenter(position.getX(), position.getY());
	}
	
	protected void setImage()
	{
		
	}
	
	public Rectangle getBoundingBox()
	{
		return image.getBoundingRectangle();
	}
	
	public void render()
	{
		image.setCenter(position.getX(), position.getY());
		image.draw(GSB.sb);
	}

	public void action(Personnage personnage)
	{
		
	}
}
