package game;

import java.util.ArrayList;

import particles.ParticleEmitter;
import screens.Game;
import screens.Options;
import weapons.BulletWeapon;
import weapons.Pistol;
import weapons.Weapon;
import boxs.Box;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

import data.Coord;
import data.FontManager;
import data.GSB;
import data.SpriteManager;
import data.TextureManager;

public class Personnage
{
	Animation sprite;
	int id;
	boolean moving = true, jumping = false, doublejump = false;
	int originX, originY;
	
	ParticleEmitter blood = new ParticleEmitter(0, 0, 8);
	
	float x, y;
	
	float vy;
	private float vx;
	
	ArrayList<Polygon> collisions;
	/**
	 * direction : True for right and false for left
	 */
	boolean direction = false;
	float time, count;
	int speed = 23;
	float acceleration = 1.2f;
	float realAcceleration = acceleration;
	float friction = .8f;
	int jumpspeed = 32;
	int doublejumpspeed = 30;
	int dieY;
	int life = 100;
	Sprite ui;
	int score;
	float physicsPrecision = 1;
	float gravity = .8f;
	
	Rectangle hitbox = new Rectangle(x + 23, y + 3, 55, 105);
	
	Weapon weapon;
	
	Personnage ennemy;
	
	public Personnage(int id, int dieY)
	{
		this.dieY = dieY;
		this.id = id;
		collisions = new ArrayList<Polygon>();
		
		blood.setTexture("blood.png");
		blood.setRate(8);
		blood.enableGravity(-.2f);
		blood.setLife(20);
		
		ui = new Sprite(SpriteManager.get("personnage_ui.png"));
		if(id == 1)
		{
			ui.flip(true, false);
			ui.setPosition(Gdx.graphics.getWidth()-ui.getWidth(), Gdx.graphics.getHeight()-ui.getHeight());
		}
		else
		{
			ui.setPosition(0, Gdx.graphics.getHeight()-ui.getHeight());
		}
		
		Array<TextureRegion> frames = new Array<TextureRegion>();
		for(int i = 1 ; i <= 4 ; i++)
		{
			Texture cur = null;
			if(id == 0)
				cur = TextureManager.get("Gentil/gauche" + i + ".png");
			else
				cur = TextureManager.get("Mechant/gauche" + i + ".png");
			frames.add(TextureRegion.split(cur, cur.getWidth(), cur.getHeight())[0][0]);
		}
		sprite = new Animation(0.10f, frames);
		sprite.setPlayMode(PlayMode.LOOP);
		
		 setWeapon(new Pistol());
	}
	
	public void setEnnemy(Personnage ennemy)
	{
		this.ennemy = ennemy;
	}
	
	public void render()
	{
		blood.render();
		if(jumping)
		{
			GSB.sb.draw(sprite.getKeyFrame(0.2f), x, y);
		}
		else if(moving)
		{
			GSB.sb.draw(sprite.getKeyFrame(time, true), x, y);
		}
		else
		{
			GSB.sb.draw(sprite.getKeyFrame(0f), x, y);
		}
		weapon.render();
	}
	
	public void renderUI()
	{
		GSB.hud.begin();
			ui.draw(GSB.hud);
		GSB.hud.end();
		if(id == 0)
		{
			GSB.sr.begin(ShapeType.Filled);
				GSB.sr.setColor(1, 0, 0, .7f);
				// Life
				GSB.sr.rect(ui.getX()+64, ui.getY()+93, life*1.5f, 10);
				GSB.sr.setColor(1, ((float)0xd0)/0xFF, ((float)0x14)/0xFF, .7f);
				// Ammo
				if(Options.ammoActivated)
				{
					GSB.sr.rect(ui.getX()+64, ui.getY()+73, ((float)weapon.getAmmo()/weapon.getMaxAmmo())*150f, 10);
				}
			GSB.sr.end();
			GSB.hud.begin();
				FontManager.get(10).draw(GSB.hud, life+"/100", ui.getX()+120, ui.getY()+101);
				if(Options.ammoActivated)
				{
					FontManager.get(10).draw(GSB.hud, weapon.getAmmo()+"/"+weapon.getMaxAmmo(), ui.getX()+120, ui.getY()+81);
				}
				FontManager.get(12).draw(GSB.hud, "Arme : "+weapon.getName(), ui.getX()+80, ui.getY()+60);
				FontManager.get(12).draw(GSB.hud, "Score", ui.getX()+10, ui.getY()+50);
				FontManager.get(12).draw(GSB.hud, ""+score, ui.getX()+10, ui.getY()+30);
				GSB.hud.draw(TextureManager.get("Gentil/avatar.png"), ui.getX()+5, ui.getY()+ui.getHeight()-50);
			GSB.hud.end();
		}
		else
		{
		
			GSB.sr.begin(ShapeType.Filled);
				GSB.sr.setColor(1, 0, 0, .7f);
				// Life
				GSB.sr.rect(ui.getX()+178, ui.getY()+93, -(life*1.5f), 10);
				GSB.sr.setColor(1, ((float)0xd0)/0xFF, ((float)0x14)/0xFF, .7f);
				// Ammo
				if(Options.ammoActivated)
				{
					GSB.sr.rect(ui.getX()+178, ui.getY()+73,-((float)weapon.getAmmo()/weapon.getMaxAmmo())*150f, 10);
				}
			GSB.sr.end();

			GSB.hud.begin();
				FontManager.get(10).draw(GSB.hud, life+"/100", ui.getX()+85, ui.getY()+101);
				if(Options.ammoActivated)
				{
					FontManager.get(10).draw(GSB.hud, weapon.getAmmo()+"/"+weapon.getMaxAmmo(), ui.getX()+85, ui.getY()+81);
				}
				FontManager.get(12).draw(GSB.hud, "Arme : "+weapon.getName(), ui.getX()+64, ui.getY()+60);
				FontManager.get(12).draw(GSB.hud, "Score", ui.getX()+190, ui.getY()+50);
				FontManager.get(12).draw(GSB.hud, ""+score, ui.getX()+200, ui.getY()+30);
				GSB.hud.draw(TextureManager.get("Mechant/avatar.png"), ui.getX()+192, ui.getY()+ui.getHeight()-50);
			GSB.hud.end();
		}
	}

	public void setWeapon(Weapon newweapon)
	{
		newweapon.setOwner(this);
		if(newweapon instanceof BulletWeapon)
		{
			if(weapon instanceof BulletWeapon)
			{
				((BulletWeapon) newweapon).transfer((BulletWeapon) weapon);
			}
		}
		this.weapon = newweapon;
	}
	
	public boolean isDead()
	{
		return y < dieY || life <= 0;
	}
	/**
	 * direction : True for right and false for left
	 * @return the direction
	 */
	public boolean getDirection()
	{
		return direction;
	}
	
	public void setLocation(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	
	public void setOrigin(float f, float g)
	{
		this.originX = (int)f;
		this.originY = (int)g;
		setLocation(originX, originY);
	}

	public void testBoxs(ArrayList<Box> other)
	{
		for(int i = 0 ; i < other.size() ; i++)
		{
			Box b = other.get(i);
			if(hitbox.overlaps(b.getBoundingBox()))
			{
				b.action(this);
				other.remove(i);
				i--;
			}
		}
	}
	int bugCorector = 5;
	public void update(float delta)
	{
		if(bugCorector-- > 0)
		{
			delta = 1/60f;
		}
		blood.update(delta*2);
		time += delta;
		
		x += vx*delta*60;
		if(collision())
		{
			x -= vx*delta*60;
			if(onGround())
				vx = -vx/2;
			else
				vx = 0;
			moving = false;
		}
		
		if(moving && onGround())
		{
			vx = vx + ((vx < 0)?friction:-friction)*delta*60;
			if(Math.abs(vx) < delta*60*friction)
			{
				vx = 0;
			}
		}
		if(onGround())
		{
			realAcceleration = acceleration*2;
		}
		else
		{
			realAcceleration = acceleration;
		}
		moving = (vx != 0);
		count += delta;
		if(count > 5)
		{
			life++;
			count -=5;
		}
		if(life > 100)
			life = 100;

		if(!onGround() || jumping)
		{
			vy -= gravity*(delta*60);
			y += vy*delta*60;
			if(collision())
			{
				if(vy > 0)
				{
					while(collision())
					{
						y -= physicsPrecision;
					}
				}
				if(vy < 0)
				{
					jumping = false;
					doublejump = false;
					float max = -1000000;
					for(int i = 1 ; i < collidedWith.getTransformedVertices().length ; i += 2)
						if(collidedWith.getTransformedVertices()[i] > max)
							max = collidedWith.getTransformedVertices()[i];
					y = max-2.3f;
				}
				vy = 0;
			}
			else
			{
				if(!jumping)
				{
					doublejump = true;
				}
			}
		}
		if(isDead())
		{
			ennemy.score++;
			respawn();
		}
		
		blood.setX((int)(hitbox.x+hitbox.width/2));
		blood.setY((int)(hitbox.y+hitbox.height/2));
	
		weapon.update(delta);
	}

	public boolean onGround()
	{
		hitbox.setPosition(x + 20, y-2);
		for(int i = 0 ; i < collisions.size() ; i++)
			if(isCollision(collisions.get(i), hitbox))
			{
				collidedWith = collisions.get(i);
				return true;
			}
		return false;
	}
	
	public void addCollision(Coord c)
	{
		int type = c.getData();
		float[] vertices = null;
		if(type == Map.GROUND)
		{
			vertices = new float[4 * 2];
			vertices[0] = c.getX() * 256;
			vertices[1] = c.getY() * 256;

			vertices[2] = c.getX() * 256 + 256;
			vertices[3] = c.getY() * 256;

			vertices[4] = c.getX() * 256 + 256;
			vertices[5] = c.getY() * 256 + 234;

			vertices[6] = c.getX() * 256;
			vertices[7] = c.getY() * 256 + 234;
		}
		if(type == Map.RIGHT)
		{
			vertices = new float[5 * 2];
			vertices[0] = c.getX() * 256 + 10;
			vertices[1] = c.getY() * 256 + 234;

			vertices[2] = c.getX() * 256 + 10;
			vertices[3] = c.getY() * 256 + 180;

			vertices[4] = c.getX() * 256 + 190;
			vertices[5] = c.getY() * 256;

			vertices[6] = c.getX() * 256 + 256;
			vertices[7] = c.getY() * 256;

			vertices[8] = c.getX() * 256 + 256;
			vertices[9] = c.getY() * 256 + 234;
		}
		if(type == Map.LEFT)
		{
			vertices = new float[5 * 2];
			vertices[0] = c.getX() * 256;
			vertices[1] = c.getY() * 256;

			vertices[2] = c.getX() * 256 + 70;
			vertices[3] = c.getY() * 256;

			vertices[4] = c.getX() * 256 + 240;
			vertices[5] = c.getY() * 256 + 180;

			vertices[6] = c.getX() * 256 + 240;
			vertices[7] = c.getY() * 256 + 234;

			vertices[8] = c.getX() * 256;
			vertices[9] = c.getY() * 256 + 234;
		}
		collisions.add(new Polygon(vertices));
		
		while(collision())
		{
			y += physicsPrecision;
		}
	}
	
	public int getLife()
	{
		return life;
	}
	
	public boolean isCollision(Polygon p, Rectangle r)
	{
		Polygon rPoly = new Polygon(new float[] { 0, 0, r.width, 0, r.width, r.height, 0, r.height });
		rPoly.setPosition(r.x, r.y);
		if(Intersector.overlapConvexPolygons(rPoly, p))
			return true;
		return false;
	}

	Polygon collidedWith;

	public boolean collision()
	{
		hitbox.setPosition(x + 20, y + 3);
		for(int i = 0 ; i < collisions.size() ; i++)
			if(isCollision(collisions.get(i), hitbox))
			{
				collidedWith = collisions.get(i);
				return true;
			}
		return false;
	}

	public void testWeapon(Personnage enemy, float delta)
	{
		enemy.getWeapon().testHit(this, delta);
	}
	
	public Weapon getWeapon()
	{
		return weapon;
	}
	
	public Rectangle getHitbox()
	{
		return hitbox;
	}
	
	public Rectangle getVxHitbox()
	{
		Rectangle todo = new Rectangle(hitbox);
		if(vx < 0)
			todo.x -= vx;
		todo.width += Math.abs(vx);
		return todo;
	}
	
	public Polygon getPolygonHitbox()
	{
		Rectangle old = new Rectangle(hitbox);
		if(vx < 0)
			hitbox.x -= vx;
		hitbox.width += Math.abs(vx);
		Polygon p = new Polygon(new float[] { 0, 0, hitbox.width, 0, hitbox.width, hitbox.height, 0, hitbox.height });
		
		p.setPosition(hitbox.x, hitbox.y);
		hitbox = old;
		return p;
	}
	
	/**
	 * direction : True for right and false for left
	 * 
	 * @param direction
	 */
	public void move(boolean direction, float delta)
	{
		moving = true;
		if(this.direction != direction)
		{
			for(TextureRegion a : sprite.getKeyFrames())
			{
				a.flip(true, false);
			}
		}
		this.direction = direction;
		if(direction && vx < speed)
		{
			vx += realAcceleration*delta*60;
		}
		if(!direction && vx > -speed)
		{
			vx -= realAcceleration*delta*60;
		}
	}

	public void fire()
	{
		if(weapon instanceof BulletWeapon)
		{
			BulletWeapon casted = (BulletWeapon) weapon;
			if(casted.canShoot())
			{
				casted.fire();
			}
		}
	}
	
	public void renderCollision()
	{
		GSB.sr.begin(ShapeType.Line);
		GSB.sr.setColor(Color.RED);
		for(Polygon p : collisions)
		{
			float[] vertices = p.getVertices();
			for(int i = 0 ; i < vertices.length ; i += 2)
			{
				GSB.sr.line(vertices[i], vertices[i + 1], vertices[(i + 2) % vertices.length], vertices[(i + 3) % vertices.length]);
			}
		}
		GSB.sr.rect(hitbox.getX(), hitbox.getY(), hitbox.getWidth(), hitbox.getHeight());
		GSB.sr.end();
	}

	public void jump()
	{
		if(doublejump || Game.debug)
		{
			doublejump = false;
			vy = doublejumpspeed;
			jumping = true;
			return;
		}
		if(!jumping)
		{
			jumping = true;
			vy = jumpspeed;
			doublejump = true;
		}
	}
	
	public float getX()
	{
		return x;
	}
	
	public float getY()
	{
		return y;
	}
	
	public void respawn()
	{
		this.x = originX;
		this.y = originY;
		
		life = 100;
		jumping = false;
		moving = false;
		vy = 0;
		vx = 0;
		setWeapon(new Pistol());
		
		while(collision())
		{
			y += physicsPrecision;
		}
	}

	public void setOrigin(Coord pos)
	{
		setOrigin(pos.getX(), pos.getY());
	}

	public void addLife(int i)
	{
		if(i < 0)
		{
			blood.startEmitting(0.1f);
		}
		this.life += i;
		if(life > 100)
			life = 100;
	}

	public ArrayList<Polygon> getCollisions()
	{
		return collisions;
	}

	public float getVx()
	{
		return vx;
	}

	public void setVx(float vx)
	{
		this.vx = vx;
	}

}
