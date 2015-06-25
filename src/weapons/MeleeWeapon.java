package weapons;

import game.Personnage;

import com.badlogic.gdx.graphics.g2d.Sprite;

import data.GSB;
import data.SpriteManager;

public class MeleeWeapon extends Weapon
{
	Sprite s;
	float rotate = 0;
	boolean attacking = false;
	boolean down = true;
	int dmg;
	float velocity;
	
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
		else
			wait -= delta;
		s.setRotation((owner.getDirection()?-1:1) * (float) Math.toDegrees(rotate));
		s.setPosition(owner.getX()+paddingx+(owner.getDirection()?0:-30), owner.getY()+paddingy);
		s.setFlip(!owner.getDirection(), false);
		s.draw(GSB.sb);		
	}
	
	public void setDamage(int dmg)
	{
		this.dmg = dmg;
	}
	
	public void setVelocity(float velocity)
	{
		this.velocity = velocity;
	}
	
	public void swing()
	{
		attacking = true;
		wait = 1;
		
		onSwing();
	}
	
	@Override
	public void setPath(String path)
	{
		super.setPath(path);
		s = new Sprite(SpriteManager.get(path));
	}
	
	public float wait = 1f;
	
	public boolean isSwinging()
	{
		return attacking || wait > 0;
	}
	
	@Override
	public void testHit(Personnage pers, float delta)
	{
		if(attacking && down && !pers.isInvicible())
		{
			if(s.getBoundingRectangle().overlaps(pers.getHitbox()))
			{
				pers.addLife(-dmg);
				if(owner.getDirection())
					pers.setVx(velocity);
				else
					pers.setVx(-velocity);
			}
		}
	}
	
	protected void onSwing()
	{
	}
}
