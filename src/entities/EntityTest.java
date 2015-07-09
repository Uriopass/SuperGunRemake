package entities;

import game.Player;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Polygon;

import data.GSB;
import data.SpriteManager;

public class EntityTest extends Entity
{
	public EntityTest()
	{
		id = 0;
		image = new Sprite(SpriteManager.get("pwned.png"));
		hitbox = image.getBoundingRectangle();
		this.vy = -200;
	}

	@Override
	public void onPlayerHit(Player hit)
	{
		hit.addLife(0);
		hit.setVx(-10);
	}

	@Override
	public void onPolygonCollision(Polygon p)
	{
		image.setFlip(true, true);
		this.vy = 0;
	}
	
	@Override
	public void render()
	{
		image.draw(GSB.sb);
	}

	@Override
	public void update(float delta)
	{
		y += vy*delta;
		image.setPosition(getX(), y);
		hitbox.setPosition(getX(), y);
		image.setFlip(false, false);
	}

}
