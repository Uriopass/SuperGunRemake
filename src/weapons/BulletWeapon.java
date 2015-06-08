package weapons;

import game.Personnage;

import java.util.ArrayList;

import screens.Options;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import data.TextureManager;

public class BulletWeapon extends Weapon
{

	int fireRate = 3;
	float lastFire = 0;
	int damage, velocity;
	static int minX, maxX;
	float velocityScale = 1.5f;
	
	ArrayList<Bullet> bullets;
	public BulletWeapon()
	{
		super();
		bullets = new ArrayList<Bullet>();
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
	
	public void transfer(BulletWeapon other)
	{
		for(Bullet b : other.bullets)
		{
			bullets.add(b);
		}
		if(other.name == this.name)
		{
			this.lastFire = other.lastFire;
		}
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
		if(Options.ammoActivated)
		{
			ammo--;
		}
		if(ammo < 0)
		{
			ammo = 0;
		}
		else
		{
			for(Bullet b : getFiredBullets(text))
			{
				if(!owner.getDirection())
				{
					b.inverteVx();
					b.x -= owner.getHitbox().width + text.getWidth() - 20;
				}
				bullets.add(b);
				onFire();
			}
		}
	}

	protected ArrayList<Bullet> getFiredBullets(Texture text)
	{
		ArrayList<Bullet> toShoot = new ArrayList<Bullet>();
		toShoot.add(new Bullet(owner.getX()+paddingx, owner.getY() + paddingy + text.getHeight()/2 + 5, velocity, 0, damage));
		return toShoot;
	}
	

	public void updateBulletCollision()
	{
		for(int i = 0 ; i < bullets.size() ; i++)
		{
			Vector2[] hit = bullets.get(i).getCollision(1/60f);
			for(int j = 0 ; j < owner.getCollisions().size() ; j++)
			{
				if(owner.getCollisions().get(j).contains(hit[0].x, hit[0].y))
				{
					bullets.remove(i);
					i--;
					break;
				}
			}
		}
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
	
	public boolean isCollision(Vector2[] seg, Rectangle r)
	{
		Vector2 a, b;
		a = new Vector2(r.getX(), r.getY());
		b = new Vector2(r.getX()+r.getWidth(), r.getY());
		if(Intersector.intersectSegments(seg[0], seg[1], a, b, null))
		{
			return true;
		}
		b.x = r.getX();
		b.y = r.getY() + r.getHeight();
		if(Intersector.intersectSegments(seg[0], seg[1], a, b, null))
		{
			return true;
		}
		a.x = r.getX() + r.getWidth();
		a.y = r.getY() + r.getHeight();
		if(Intersector.intersectSegments(seg[0], seg[1], a, b, null))
		{
			return true;
		}
		
		b.x = r.getX() + r.getWidth();
		b.y = r.getY();
		if(Intersector.intersectSegments(seg[0], seg[1], a, b, null))
		{
			return true;
		}
		return false;
	}
	
	@Override
	public void testHit(Personnage pers, float delta)
	{
		for(int i = 0 ; i < bullets.size() ; i++)
		{
			Bullet b = bullets.get(i);
			Vector2[] pos = b.getCollision(delta);
			
			float lifemultiplier = 1+2*(100f-pers.getLife())/100f;
			
			
			if(isCollision(pos, pers.getVxHitbox()) || pers.getVxHitbox().contains(pos[0]))
			{
				pers.addLife(-damage);
				
				if(Options.brawlModeActivated)
				{
					pers.setVx(pers.getVx()/2 + b.vx*velocityScale*lifemultiplier);
				}
				bullets.remove(i);
				i--;
			}
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
