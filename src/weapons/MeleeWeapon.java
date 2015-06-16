package weapons;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Sprite;

import data.GSB;
import data.SoundManager;
import data.SpriteManager;
import game.Personnage;

public class MeleeWeapon extends Weapon
{
	Sprite s;
	float rotate = 0;
	boolean attacking = false;
	boolean down = true;
	
	public MeleeWeapon()
	{
		
	}
	
	@Override
	public void render(float delta)
	{
		if(attacking)
		{
			if(down)
			{
				rotate += 4f*delta*Math.PI/2f;
				if(rotate > Math.PI/2)
					down = false;
			}
			else
			{
				rotate -= 4f*delta*Math.PI/2f;
				if(rotate < 0)
				{
					rotate = 0;
					down = true;
					attacking = false;
				}
			}
		}
		s.setRotation((float) -Math.toDegrees(rotate));
		s.setPosition(owner.getX()+paddingx, owner.getY()+paddingy);
		s.draw(GSB.sb);		
	}
	
	public void swing()
	{
		attacking = true;
		
		onSwing();
	}
	
	@Override
	public void setPath(String path)
	{
		super.setPath(path);
		s = new Sprite(SpriteManager.get(path));
	}
	
	public boolean isSwinging()
	{
		return attacking;
	}
	
	@Override
	public void testHit(Personnage pers, float delta)
	{
		if(attacking && down)
		{
			if(s.getBoundingRectangle().overlaps(pers.getHitbox()))
			{
				pers.setVx(20);
			}
		}
	}
	
	protected void onSwing()
	{
	}
}
