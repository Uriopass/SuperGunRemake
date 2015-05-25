package boxs;

import game.Personnage;

import com.badlogic.gdx.graphics.g2d.Sprite;

import data.Coord;
import data.SpriteManager;

public class SniperBox extends Box
{
	public final static float probability = .06f;
	public SniperBox(Coord pos)
	{
		super(pos);
	}
	
	@Override
	protected void setImage()
	{
		image = new Sprite(SpriteManager.get("sniperbox.png"));
	}
	
	@Override
	public void action(Personnage personnage)
	{
		personnage.weapon = personnage.sniper;
	}
}
