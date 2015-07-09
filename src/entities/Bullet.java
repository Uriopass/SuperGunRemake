package entities;

import game.Player;
import screens.Game;
import screens.Options;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import data.GSB;
import data.SpriteManager;

public class Bullet extends Entity
{
	int dmg;
	float angle;
	float velocityScale;
	int ownerId;
	
	public Bullet(float x, float y, float vx, float vy, int dmg, float velocityScale, int ownerId)
	{
		id = 2;
		this.ownerId = ownerId;
		this.dmg = dmg;
		this.velocityScale = velocityScale;
		
		this.setX(x);
		this.y = y;
		this.vx = vx;
		this.vy = vy;
		
		image = SpriteManager.get("Armes/bullet.png");
		image.setOrigin(image.getWidth()/2, image.getHeight()/2);
		angle = getAngle(vx, vy);
	}
	
	@Override
	public void onPlayerHit(Player hit)
	{
		if(hit.id != ownerId)
		{
			float lifemultiplier = 1+4*(100f-hit.getLife())/100f;
			
			if(hit.isInvicible() || Game.invincible)
			{
				hit.addLife(0);
			}
			else
			{
				hit.addLife(-dmg);
				
				if(Options.get("brawl"))
				{
					hit.setVx(hit.getVx()/2 + vx*velocityScale*lifemultiplier);
				}
			}
			
			delete = true;
		}
	}
	
	private float getAngle(float vx, float vy)
	{
		float angle = (float) Math.toDegrees(Math.acos( vx /Math.sqrt(vx*vx+vy*vy)));
		if(vy < 0)
			angle = -angle;
		return angle;
	}
	
	@Override
	public void update(float delta)
	{
		setX(getX() + vx*(delta*60));
		y += vy*(delta*60);
		lastDelta = delta;
		
	}
	
	float lastDelta = 0;
	
	public Vector2[] getCollision(float delta)
	{
		Vector2[] collision = new Vector2[2];
		
		if(vx > 0)
		{
			collision[0] = new Vector2(getX()+image.getWidth()-30, y+image.getHeight()/2);
		}
		else
		{
			collision[0] = new Vector2(getX()+30, y+image.getHeight()/2);
		}
		collision[1] = new Vector2(collision[0].x + vx*delta*60, collision[0].y + vy*delta*60);
		return collision;
	}
	
	public void inverteVx()
	{
		this.vx = -vx;
		angle = getAngle(vx, vy);
	}
	
	@Override
	public void render()
	{
		image.setRotation(angle);
		image.setPosition(getX(), y);
		image.draw(GSB.sb);
	}
	
	@Override
	public void onPolygonCollision(Polygon p)
	{
		delete = true;
	}
	
	@Override
	public boolean polygonTest(Polygon p)
	{
		if(this.vx < 0)
			return p.contains(x, y);
		else
			return p.contains(x + image.getWidth(), y);
	}
	
	@Override
	public boolean playerTest(Player p)
	{
		Vector2[] pos = getCollision(lastDelta);
		
		return isCollision(pos, p.getVxHitbox()) || p.getVxHitbox().contains(pos[0]);
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
	
}
