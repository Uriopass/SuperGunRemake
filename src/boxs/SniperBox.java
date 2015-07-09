package boxs;

import weapons.Sniper;
import game.Player;

import com.badlogic.gdx.graphics.g2d.Sprite;

import data.Coord;
import data.GSB;
import data.SpriteManager;

public class SniperBox extends Box
{
	public final static float probability = .05f;
	Sprite sniperpic;
	public SniperBox(Coord pos)
	{
		super(pos);
	}
	
	@Override
	protected void setImage()
	{
		image = new Sprite(SpriteManager.get("emptybox.png"));
		sniperpic = new Sprite(SpriteManager.get("Armes/sniper.png"));
	}
	
	@Override
	public void render()
	{
		super.render();
		sniperpic.setCenter(image.getX()+image.getWidth()/2, image.getY()+image.getHeight()/2);
		sniperpic.draw(GSB.sb);
	}
	
	@Override
	public void action(Player personnage)
	{
		personnage.setWeapon(new Sniper());
	}
}
