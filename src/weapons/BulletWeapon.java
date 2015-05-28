package weapons;

import game.Personnage;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import data.TextureManager;

public class BulletWeapon extends Weapon
{

	int fireRate = 3;
	float lastFire = 0;
	int damage, velocity;
	static int minX, maxX;
	
	ArrayList<Bullet> bullets;
	
	public BulletWeapon(String name)
	{
		super(name);
		bullets = new ArrayList<Bullet>();
	}
	public void setRate(int rate)
	{
		fireRate = rate;
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
		ammo--;
		if(ammo < 0)
		{
			ammo = 0;
		}
		else
		{
			for(Bullet b : getFiredBullets(text))
			{
				if(owner.getDirection())
					b.vx = -b.vx;
				bullets.add(b);
			}
		}
	}

	protected ArrayList<Bullet> getFiredBullets(Texture text)
	{
		ArrayList<Bullet> toShoot = new ArrayList<Bullet>();
		toShoot.add(new Bullet(owner.getX() + paddingx + text.getWidth(), owner.getY() + paddingy + text.getHeight()/2 + 5, velocity, 10, damage));
		return toShoot;
	}
	

	public void updateBulletCollision()
	{
		for(int i = 0 ; i < bullets.size() ; i++)
		{
			Vector2 hit = bullets.get(i).getCollision();
			for(int j = 0 ; j < owner.getCollisions().size() ; j++)
			{
				if(owner.getCollisions().get(j).contains(hit.x, hit.y))
				{
					bullets.remove(i);
					i--;
					break;
				}
			}
		}
	}
	
	@Override
	public void update(float delta)
	{
		lastFire-=1*delta*60;
		
		for(int i = 0 ; i < bullets.size() ; i++)
		{
			Bullet b = bullets.get(i);
			b.update(delta);
			if(b.x < minX || b.x > maxX)
			{
				bullets.remove(i);
				i--;
			}
		}
		
		updateBulletCollision();
	}
	
	@Override
	public void render()
	{
		super.render();
		for(Bullet b : bullets)
			b.render();
	}
	
	@Override
	public void testHit(Personnage pers)
	{
		for(int i = 0 ; i < bullets.size() ; i++)
		{
			Bullet b = bullets.get(i);
			Vector2 pos = b.getCollision();
			
			if(pers.getHitbox().contains(pos))
			{
				pers.addLife(-damage);
				bullets.remove(i);
				i--;
				continue;
			}
			
			pos.add(b.vx/2, b.vy/2);
			
			if(pers.getHitbox().contains(pos))
			{
				pers.addLife(-damage);
				bullets.remove(i);
				i--;
			}
		}
	}
	
	@Override
	public void reset()
	{
		lastFire = 0;
		ammo = maxammo;
	}
}
