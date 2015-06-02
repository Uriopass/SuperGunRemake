package boxs;

import weapons.Shotgun;
import game.Personnage;

import com.badlogic.gdx.graphics.g2d.Sprite;

import data.Coord;
import data.GSB;
import data.SpriteManager;

public class ShotgunBox extends Box
{
	public final static float probability = .05f;
	Sprite shotgunpic;
	public ShotgunBox(Coord pos)
	{
		super(pos);
	}
	
	@Override
	protected void setImage()
	{
		image = new Sprite(SpriteManager.get("emptybox.png"));
		shotgunpic = new Sprite(SpriteManager.get("Armes/shotgun.png"));
	}
	 
	@Override
	public void render()
	{
		super.render();
		shotgunpic.setCenter(image.getX()+image.getWidth()/2, image.getY()+image.getHeight()/2);
		shotgunpic.draw(GSB.sb);
	}
	
	@Override
	public void action(Personnage personnage)
	{
		personnage.setWeapon(new Shotgun());
	}
}
