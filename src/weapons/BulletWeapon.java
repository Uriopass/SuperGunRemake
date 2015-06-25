package weapons;

import java.util.ArrayList;

import screens.Options;

import com.badlogic.gdx.graphics.Texture;

import data.TextureManager;
import entities.Bullet;

public class BulletWeapon extends Weapon
{
	int fireRate = 3;
	float lastFire = 0;
	int damage, velocity;
	static int minX, maxX;
	float velocityScale = 1.5f;
	
	public BulletWeapon()
	{
		super();
	}
	public void setRate(int rate)
	{
		fireRate = rate;
	}
	
	public float getLastFire()
	{
		if(lastFire < 0)
			return 0;
		else
			return lastFire;
	}
	
	public int getFireRate()
	{
		return fireRate;
	}
	
	public static void setMinMaxX(int minx, int maxx)
	{
		minX = minx;
		maxX = maxx;
	}
	
	public boolean canShoot()
	{
		return lastFire <= 0;
	}
	
	public void setDamage(int damage)
	{
		this.damage = damage;
	}
	
	public void setVelocity(int velocity)
	{
		this.velocity = velocity;
	}
	
	public void fire()
	{
		lastFire = fireRate;
		Texture text = TextureManager.get(path);
		if(Options.get("ammo"))
		{
			setAmmo(ammo - 1);;
		}
		if(ammo < 0)
		{
			setAmmo(0);
		}
		else
		{
			for(Bullet b : getFiredBullets(text))
			{
				if(!owner.getDirection())
				{
					b.inverteVx();
					b.setX(b.getX() - (owner.getHitbox().width + text.getWidth() - 20));
				}
				owner.g.we.addEntity(b);
				
				onFire();
			}
		}
	}

	protected ArrayList<Bullet> getFiredBullets(Texture text)
	{
		ArrayList<Bullet> toShoot = new ArrayList<Bullet>();
		toShoot.add(new Bullet(owner.getX()+paddingx, owner.getY() + paddingy + text.getHeight()/2 + 5, velocity, 0, damage, velocityScale));
		return toShoot;
	}
	
	boolean once;
	
	@Override
	public void update(float delta)
	{
		lastFire-=1*delta*60;
		
		if(lastFire > 20)
			once = true;
		if(lastFire <= 20 && once)
		{
			once = false;
			onReload();
		}
	}
	
	public void setVelocityScale(float velocityScale)
	{
		this.velocityScale = velocityScale;
	}
	
	public void onFire()
	{
		
	}
	
	public void onReload()
	{
		
	}
	
	@Override
	public void reset()
	{
		lastFire = 0;
		ammo = maxammo;
	}
}
