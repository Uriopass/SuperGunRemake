package boxs;

import game.Personnage;
import weapons.BaseballBat;

import com.badlogic.gdx.graphics.g2d.Sprite;

import data.Coord;
import data.GSB;
import data.SpriteManager;

public class BaseballBox extends Box
{
	public final static float probability = .05f;
	Sprite baseballpic;
	public BaseballBox(Coord pos)
	{
		super(pos);
	}
	
	@Override
	protected void setImage()
	{
		image = new Sprite(SpriteManager.get("emptybox.png"));
		baseballpic = new Sprite(SpriteManager.get("Armes/batte.png"));
	}
	 
	@Override
	public void render()
	{
		super.render();
		baseballpic.setCenter(image.getX()+image.getWidth()/2, image.getY()+image.getHeight()/2);
		baseballpic.draw(GSB.sb);
	}
	
	@Override
	public void action(Personnage personnage)
	{
		personnage.setWeapon(new BaseballBat());
	}
}
