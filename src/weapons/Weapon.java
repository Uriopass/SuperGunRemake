package weapons;

import game.Personnage;

import com.badlogic.gdx.graphics.Texture;

import data.GSB;
import data.TextureManager;

public class Weapon
{
	String name;
	String path = "";
	int paddingx, paddingy;

	int ammo, maxammo;
	Personnage owner;
	
	public Weapon(String name)
	{
		this.name = name;
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

	public void setMaxAmmo(int maxAmmo)
	{
		this.maxammo = maxAmmo;
		this.ammo = maxAmmo;
	}
	
	public void render()
	{
		Texture text = TextureManager.get(path);
		if(owner.getDirection())
		{
			GSB.sb.draw(text, owner.getX() + paddingx, owner.getY() + paddingy);
		}
		else
		{
			GSB.sb.draw(text, owner.getX(), owner.getY() + paddingy, text.getWidth(), text.getHeight(), 0, 0, text.getWidth(), text.getHeight(), true, false);
		}
	}
	
	public void setOwner(Personnage owner)
	{
		this.owner = owner;
	}
	
	public int getAmmo()
	{
		return ammo;
	}
	
	public String getName()
	{
		return name;
	}
	
	public int getMaxAmmo()
	{
		return maxammo;
	}
	
	public void update(float delta)
	{
		
	}

	public void reset()
	{
		
	}

	public void testHit(Personnage pers)
	{
		
	}
}
