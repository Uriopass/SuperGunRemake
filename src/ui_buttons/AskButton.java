package ui_buttons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Rectangle;

import data.GSB;
import data.SoundManager;
import data.TextureManager;

public class AskButton
{
	boolean activated;
	int x, y, width, height;
	Sound clicked;
	
	public AskButton()
	{
		this.activated = false;
		x = 0;
		y = 0;
		width = TextureManager.get("askbutton_no.png").getWidth();
		height = TextureManager.get("askbutton_no.png").getHeight();

		yes = new Rectangle(33, 9, 25, 24);
		no = new Rectangle(5, 9, 25, 24);
		clicked = SoundManager.get("askbutton.wav");
	}
	
	public void setLocation(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	
	public void center(boolean x, boolean y)
	{
		if(x)
			this.x -= width/2;
		if(y)
			this.y -= height/2;
	}
	
	public int getX()
	{
		return x;
	}
	
	public int getY()
	{
		return y;
	}
	
	Rectangle yes, no;
	
	public void update()
	{
		if(Gdx.input.isButtonPressed(Input.Buttons.LEFT) && Gdx.input.justTouched())
		{
			int touchedx = Gdx.input.getX();
			int touchedy = Gdx.graphics.getHeight() - Gdx.input.getY();
			
			touchedx -= x;
			touchedy -= y;
			
			if(yes.contains(touchedx, touchedy))
			{
				if(!activated)
					clicked.play(1, 0.8f, 0);
				setActivated(true);	
			}
			if(no.contains(touchedx, touchedy))
			{
				if(activated)
					clicked.play(1, 0.8f, 0);
				setActivated(false);
			}
			
		}
	}
	
	public void render()
	{
		if(activated)
		{
			GSB.hud.draw(TextureManager.get("askbutton_yes.png"), x, y);
		}
		else
		{
			GSB.hud.draw(TextureManager.get("askbutton_no.png"), x, y);	
		}
	}
	
	public void setActivated(boolean activated)
	{
		this.activated = activated;
	}
	
	public int getHeight()
	{
		return height;
	}
	
	public boolean getValue()
	{
		return activated;
	}
}
