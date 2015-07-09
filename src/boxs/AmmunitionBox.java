package boxs;

import game.Player;

import com.badlogic.gdx.graphics.g2d.Sprite;

import data.Coord;
import data.SpriteManager;

public class AmmunitionBox extends Box
{
	public final static float probability = .03f;
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
	public void action(Player personnage)
	{
		personnage.getWeapon().setMaxAmmo(personnage.getWeapon().getMaxAmmo());
	}
}
