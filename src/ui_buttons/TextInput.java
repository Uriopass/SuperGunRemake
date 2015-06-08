package ui_buttons;

import java.util.regex.Pattern;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

import data.FontManager;
import data.GSB;
import data.TextureManager;
public class TextInput implements InputProcessor
{
	int x = 0, originalY = 0, drawy;
	int width, height;
	String toShowWhenEmpty;
	boolean writing = false;
	String current = "";
	String regexLimit = "";
	float time = 0;
	boolean update = true;
	
	public TextInput(String text)
	{
		this.toShowWhenEmpty = text; 
		width = 300;
		height = 50;
	}
	
	public void setRegex(String regexLimit)
	{
		this.regexLimit = regexLimit;
	}
	
	public String getText()
	{
		return current;
	}
	GlyphLayout gl = new GlyphLayout();
	
	public void render(int scroll)
	{
		time = System.nanoTime()/100000000;
		System.out.print(time+"\r");
		drawy = originalY + scroll;
		String path;
		path = "textinput.png";
		GSB.hud.draw(TextureManager.get(path), x, drawy);
		
		if(!writing && current.equals(""))
		{
			gl.setText(FontManager.get(20), toShowWhenEmpty);
			FontManager.get(20).draw(GSB.hud, toShowWhenEmpty, x+(width-gl.width)/2, 15+drawy+(height-gl.height)/2);
		}
		else
		{
			gl.setText(FontManager.get(20), current);
			FontManager.get(20).draw(GSB.hud, current, x+10, drawy + (height+gl.height)/2);
		}
		if(writing)
		{
			if(Math.sin(time/2f) > 0)
			{
				FontManager.get(20).draw(GSB.hud, "|", x+10+gl.width, drawy + (height+gl.height)/2);
			}
		}
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
	
	public void setWriting(boolean writing)
	{
		this.writing = writing;
	}
	
	@Override
	public boolean keyTyped(char character)
	{
		if(update)
		{
			gl.setText(FontManager.get(20), current+" "+character);
			boolean catchRegex = true;
			if(regexLimit != "")
			{
				catchRegex = Pattern.matches(regexLimit,""+character);
			}
			if(catchRegex & writing && gl.width < width - 15)
			{
				current += character; 
			}
		}
		return writing;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button)
	{
		screenY = Gdx.graphics.getHeight() - screenY;
		if(button == Input.Buttons.LEFT && contains(screenX, screenY))
		{
			setWriting(true);
		}
		else
		{
			setWriting(false);
		}
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY)
	{
		if(contains(screenX, Gdx.graphics.getHeight() - screenY))
		{
			// TODO Change cursor
		}
		return false;
	}

	@Override
	public boolean keyDown(int keycode)
	{
		if(update)
		{
			if(keycode == Input.Keys.BACKSPACE && !current.equals(""))
			{
				current = current.substring(0, current.length()-1);
			}
		}
		return true;
	}
	
	
	
	
	
	
	
	
	


	@Override
	public boolean keyUp(int keycode)
	{
		return false;
	}
	

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button)
	{
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer)
	{
		return false;
	}

	@Override
	public boolean scrolled(int amount)
	{
		return false;
	}

	public void reset()
	{
		this.current = "";	
	}

	public void setUpdate(boolean b)
	{
		this.update = b;
	}
}
