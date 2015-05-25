package game;

import com.badlogic.gdx.graphics.Texture;

import data.GSB;
import data.TextureManager;

public class Weapon
{
	String name;
	int fireRate = 3;
	int lastFire = 0;
	int ammo, maxammo;
	String path = "";
	int paddingx, paddingy;
	int damage, velocity;
	
	Personnage owner;
	
	public Weapon(String name)
	{
		this.name = name;
	}
	
	public void setRate(int rate)
	{
		fireRate = rate;
	}
	
	public void setPath(String path)
	{
		this.path = path;
	}
	
	public void setPadding(int x, int y)
	{
		paddingx = x;
		paddingy = y;
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
	
	public void render()
	{
		Texture text = TextureManager.get(path);
		if(owner.direction)
		{
			GSB.sb.draw(text, owner.x + paddingx, owner.y + paddingy);
		}
		else
		{
			GSB.sb.draw(text, owner.x, owner.y + paddingy, text.getWidth(), text.getHeight(), 0, 0, text.getWidth(), text.getHeight(), true, false);
		}
	}
	
	public Bullet shoot()
	{
		lastFire = fireRate;
		Texture text = TextureManager.get(path);
		ammo--;
		if(ammo < 0)
		{
			ammo = 0;
			return null;
		}
		if(owner.direction)
		{
			return new Bullet(owner.x + paddingx + text.getWidth(), owner.y + paddingy + text.getHeight()/2 + 5, owner.speed*velocity, 0, damage);
		}
		else
		{
			return new Bullet(owner.x , owner.y + paddingy + text.getHeight()/2 + 5, -owner.speed*velocity, 0, damage);
		}
	}
	
	public void setMaxAmmo(int maxAmmo)
	{
		this.maxammo = maxAmmo;
		this.ammo = maxAmmo;
	}
	
	public void update()
	{
		lastFire--;
	}

	public void reset()
	{
		lastFire = 0;
		ammo = maxammo;
	}
}
