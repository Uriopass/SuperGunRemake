package entities;

import game.Player;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;

public abstract class Entity
{
	float x;
	float y;
	float vx;
	float vy;
	Rectangle hitbox;
	Sprite image;
	public int id;
	
	public boolean delete;
	
	abstract public void onPlayerHit(Player hit);
	
	abstract public void render();
	
	abstract public void update(float delta);
	
	abstract public void onPolygonCollision(Polygon p);
	
	Polygon a;
	public boolean polygonTest(Polygon p)
	{
		if(a == null)
			a = new Polygon(new float[] { 0, 0, hitbox.width, 0, hitbox.width, hitbox.height, 0, hitbox.height });
		a.setPosition(hitbox.x, hitbox.y);
		
		return Intersector.overlapConvexPolygons(p, a);
	}
	
	public boolean playerTest(Player p)
	{
		return p.getHitbox().overlaps(hitbox);
	}

	public Rectangle getHitbox()
	{
		return hitbox;
	}

	public float getX()
	{
		return x;
	}

	public void setX(float x)
	{
		this.x = x;
	}
}
