package map;

import java.io.Serializable;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

import data.Coord;
import data.GSB;
import data.TextureManager;

public class Block implements Serializable
{
	private static final long serialVersionUID = 12L;

	public Coord pos;
	int type = 0;
	int flag;
	
	float animationCount = 0;
	
	public Block(int x, int y)
	{
		pos = new Coord(x, y);
	}
	
	public Block(Coord c)
	{
		pos = c;
	}
	
	public void setType(int type)
	{
		this.type = type;
	}
	
	public void setFlag(int flag)
	{
		this.flag = flag;
	}
	
	public int getFlag()
	{
		return flag;
	}
	
	public int getType()
	{
		return type;
	}
	
	public float getX()
	{
		return pos.getX();
	}
	
	public float getY()
	{
		return pos.getY();
	}

	public void render(float delta)
	{
		animationCount += delta;
		if(type == 0)
		{
			if(!GSB.sb.isDrawing())
			{
				if(GSB.srCam.isDrawing())
				{
					GSB.srCam.end();
				}
				GSB.sb.begin();
			}
			String path = "sol.png";
			
			if(flag == Map.RIGHT)
			{
				path = "bord_g.png";
			}
			if(flag == Map.LEFT)
			{
				path = "bord_d.png";
			}
			
			GSB.sb.draw(TextureManager.get(path), pos.getX()*256, pos.getY()*256);
		}
		if(type == 1)
		{
			int width = 210, height = 210;
			if(!GSB.srCam.isDrawing())
			{
				if(GSB.sb.isDrawing())
				{
					GSB.sb.end();
				}
				GSB.srCam.begin(ShapeType.Filled);
			}
		
			GSB.srCam.setColor(new Color(148/255f,0,211/255f, 1));
			GSB.srCam.rect(getX()*256+(256-width)/2, getY()*256+(256-height)/2, width/2,height/2, width, height, 1, 1, animationCount*360*.1f);
			
			GSB.srCam.setColor(new Color(118/255f,0,118/255f, 1));
			width = 190;
			height = 190;
			GSB.srCam.rect(getX()*256+(256-width)/2, getY()*256+(256-height)/2, width/2,height/2, width, height, 1, 1, -animationCount*360*.15f);
			
			
			GSB.srCam.setColor(new Color(65/255f,0,65/255f, 1));
			width = 160;
			height = 160;
			GSB.srCam.rect(getX()*256+(256-width)/2, getY()*256+(256-height)/2, width/2,height/2, width, height, 1, 1, animationCount*360*0.05f+27);
		}
		if(type > 1)
			System.out.println("WTFFFFFFFFFFFFF");
	}
}
