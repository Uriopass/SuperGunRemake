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
			rotate += 2f*delta*Math.PI*2f;
			if(rotate > Math.PI*2)
			{
				attacking = false;
				rotate = 0;
			}
		}
		else
		{
			alreadyhit = false;
			wait -= delta;
		}
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
		wait = .4f;
		
		onSwing();
	}
	
	@Override
	public void setPath(String path)
	{
		super.setPath(path);
		s = new Sprite(SpriteManager.get(path));
	}
	
	public float wait = 0f;
	
	public boolean isSwinging()
	{
		return attacking || wait > 0;
	}
	
	boolean alreadyhit = false;
	
	@Override
	public void testHit(Personnage pers, float delta)
	{
		if(attacking && down && !pers.isInvicible())
		{
			if(s.getBoundingRectangle().overlaps(pers.getHitbox()))
			{

				if(!alreadyhit)
				{
					alreadyhit = true;
					pers.addLife(-dmg);
					int multiplier = 1;
					
					if(owner.getDirection())
						multiplier = 1;
					else 
						multiplier = -1;
					if(rotate > Math.PI)
						multiplier*=-1;
					
					pers.setVx(pers.getVx() + multiplier*velocity);
				}
			}
		}
	}
	
	protected void onSwing()
	{
	}
}
