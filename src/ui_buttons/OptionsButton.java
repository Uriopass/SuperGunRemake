package ui_buttons;

import com.badlogic.gdx.graphics.g2d.GlyphLayout;

import data.FontManager;
import data.GSB;

public class OptionsButton
{
	String name;
	AskButton modifier;
	int x, y;
	int textWidth;
	
	public OptionsButton(String text)
	{
		name = text;
		modifier = new AskButton();
		
		x = 0;
		y = 0;
		textWidth = (int) (new GlyphLayout(FontManager.get(20), text)).width;
	}
	
	public void setLocation(int x, int y)
	{
		this.x = x;
		this.y = y;
		modifier.setLocation(x, y);
	}
	
	public int getX()
	{
		return x;
	}
	
	public int getY()
	{
		return y;
	}
	
	public int getHeight()
	{
		return modifier.getHeight();
	}
	
	public boolean getValue()
	{
		return modifier.getValue();
	}
	
	public void setActivated(boolean activated)
	{
		modifier.setActivated(activated);
	}
	
	public void update()
	{
		modifier.update();
	}
	
	
	
	public void render()
	{
		modifier.render();
		FontManager.get(20).draw(GSB.hud, name, x - textWidth - 10, y+modifier.getHeight()-10);
	}
}
