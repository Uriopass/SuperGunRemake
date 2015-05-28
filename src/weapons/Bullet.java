package weapons;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import data.GSB;
import data.SpriteManager;

public class Bullet
{
	float x, y, vx, vy;
	Rectangle hitbox;
	int dmg;
	float angle;
	Sprite image;
	
	public Bullet(float x, float y, float vx, float vy, int dmg)
	{
		this.dmg = dmg;
		this.x = x;
		this.y = y;
		this.vx = vx;
		this.vy = vy;
		image = SpriteManager.get("Armes/bullet.png");
		image.setOrigin(image.getWidth()/2, image.getHeight()/2);
		angle = getAngle(vx, vy);
	}
	
	private float getAngle(float vx, float vy)
	{
		return (float) Math.toDegrees(Math.acos( (vx) / (Math.sqrt(vx*vx+vy*vy))));
	}
	
	public void update(float delta)
	{
		x += vx*(delta*60);
		y += vy*(delta*60);
	}
	
	public Vector2 getCollision()
	{
		return new Vector2(x, y);
	}
	
	public void render()
	{
		image.setRotation(angle);
		image.setPosition(x, y);
		image.draw(GSB.sb);
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
