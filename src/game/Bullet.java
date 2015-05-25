package game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

import data.GSB;
import data.TextureManager;

public class Bullet
{
	float x, y, vx, vy;
	Rectangle hitbox;
	int dmg;
	
	public Bullet(float x, float y, float vx, float vy, int dmg)
	{
		this.dmg = dmg;
		this.x = x;
		this.y = y;
		this.vx = vx;
		this.vy = vy;
		Texture t = TextureManager.get("Armes/bullet.png");
		hitbox = new Rectangle(x, y, t.getWidth(), t.getHeight());
	}
	
	public void update()
	{
		x += vx;
		y += vy;
	}
	
	public Rectangle getHitbox()
	{
		hitbox.x = x;
		hitbox.y = y;
		return hitbox;
	}
	
	public void render()
	{
		Texture t = TextureManager.get("Armes/bullet.png");
		GSB.sb.draw(t, x-t.getWidth()/2, y-t.getHeight()/2);
	}
	
	public int getDmg()
	{
		return dmg;
	}
	
	public float getX()
	{
		return x;
	}
	
	public float getY()
	{
		return y;
	}
}
