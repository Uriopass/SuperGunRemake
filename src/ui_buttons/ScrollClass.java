package ui_buttons;

import com.badlogic.gdx.InputProcessor;

public class ScrollClass implements InputProcessor
{
	static int scroll;
	
	public static int getScroll()
	{
		int tmp = scroll;
		resetScroll();
		return tmp;
	}
	
	public static void resetScroll()
	{
		scroll = 0;
	}
	
	
	
	@Override
	public boolean scrolled(int amount)
	{
		scroll += amount;
		return false;
	}
	@Override
	public boolean keyDown(int keycode)
	{
		return false;
	}

	@Override
	public boolean keyUp(int keycode)
	{
		return false;
	}

	@Override
	public boolean keyTyped(char character)
	{
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button)
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
	public boolean mouseMoved(int screenX, int screenY)
	{
		return false;
	}


}
