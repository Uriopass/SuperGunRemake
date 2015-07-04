package ui_buttons;

import screens.Options;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

import data.FontManager;
import data.GSB;
import data.SoundManager;
import data.TextureManager;

public class BigButton
{
	String name;
	ButtonType position = ButtonType.NONE;
	
	int x = 0, originalY = 0, drawy;
	int width, height;
	
	public BigButton(String name)
	{
		this.name = name;
		width = 200;
		height = 50;
		gl.setText(FontManager.get(20), name);
	}
	GlyphLayout gl = new GlyphLayout();
	public void render(int scroll)
	{
		drawy = originalY + scroll;
		String path = "";
		switch(position)
		{
			case HOVER:
				path ="mainmenu_button_hover.png";
				break;
			case CLICK:
				path = "mainmenu_button_click.png";
				break;
			default:
				path = "mainmenu_button.png";
		}
		GSB.hud.draw(TextureManager.get(path), x, drawy);
		FontManager.get(20).draw(GSB.hud, name, x+(width-gl.width)/2, 15+drawy+(height-gl.height)/2);
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public boolean isHovered()
	{
		return position == ButtonType.HOVER || position == ButtonType.CLICK;
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setLocation(int x, int y)
	{
		this.x = x;
		this.originalY = y;
		this.drawy = y;
	}
	
	public void center(boolean x, boolean y)
	{
		if(x)
			this.x -= width/2;
		if(y)
			this.originalY -= height/2;
	}
	
	public int getX()
	{
		return x;
	}
	
	public int getY()
	{
		return drawy;
	}
	
	boolean contains(int x, int y)
	{
		return x > this.x && y > this.drawy && x < this.x + width && y < this.drawy + height; 
	}
	int test = 0;
	public void update()
	{
		int x = Gdx.input.getX(), y = Gdx.graphics.getHeight() - Gdx.input.getY();
		if(contains(x, y))
		{
			if(Gdx.input.isButtonPressed(Input.Buttons.LEFT) && Gdx.input.justTouched())
			{
				this.position = ButtonType.CLICK;
				onClick();
				if(Options.get("sound"))
					SoundManager.get("askbutton.wav").play(1, .5f, 0);
			}
			else
			{
				this.position = ButtonType.HOVER;
			}
		}
		else
		{
			this.position = ButtonType.NONE;
		}
	}
	
	public int getHeight()
	{
		return height;
	}

	public int getWidth()
	{
		return width;
	}

	protected void onClick()
	{
		
	}

	public void forceClick()
	{
		onClick();
	}
}
