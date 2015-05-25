package boxs;

import game.Personnage;

import com.badlogic.gdx.graphics.g2d.Sprite;

import data.Coord;
import data.SpriteManager;

public class AmmunitionBox extends Box
{
	public final static float probability = .1f;
	public AmmunitionBox(Coord pos)
	{
		super(pos);
	}
	@Override
	protected void setImage()
	{
		image = new Sprite(SpriteManager.get("ammobox.png"));
	}
	@Override
	public void action(Personnage personnage)
	{
		personnage.getWeapon().reset();
	}
}
