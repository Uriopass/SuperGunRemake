package boxs;

import game.Personnage;
import weapons.Rifle;

import com.badlogic.gdx.graphics.g2d.Sprite;

import data.Coord;
import data.GSB;
import data.SpriteManager;

public class RifleBox extends Box
{
	public final static float probability = .05f;
	Sprite riflepic;
	public RifleBox(Coord pos)
	{
		super(pos);
	}
	
	@Override
	protected void setImage()
	{
		image = new Sprite(SpriteManager.get("emptybox.png"));
		riflepic = new Sprite(SpriteManager.get("Armes/rifle.png"));
	}
	 
	@Override
	public void render()
	{
		super.render();
		riflepic.setCenter(image.getX()+image.getWidth()/2, image.getY()+image.getHeight()/2);
		riflepic.draw(GSB.sb);
	}
	
	@Override
	public void action(Personnage personnage)
	{
		personnage.setWeapon(new Rifle());
	}
}
