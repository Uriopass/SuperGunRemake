package boxs;

import game.Player;

import com.badlogic.gdx.graphics.g2d.Sprite;

import data.Coord;
import data.SpriteManager;

public class HealthBox extends Box
{
	public final static float probability = .03f;
	public HealthBox(Coord pos)
	{
		super(pos);
	}
	
	@Override
	protected void setImage()
	{
		image = new Sprite(SpriteManager.get("healthbox.png"));
	}
	
	@Override
	public void action(Player personnage)
	{
		personnage.addLife(30);
	}
}
