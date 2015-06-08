package game;

import java.util.ArrayList;

import map.Block;
import map.Map;
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
import com.badlogic.gdx.graphics.g2d.BitmapFont;
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
	boolean moving = true, jumping = false;
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
	
	int jumps = 2;
	
	private int dieY;
	int life = 100;
	Sprite ui;
	int score;
	float physicsPrecision = 1;
	float gravity = .8f;
	
	float invincible = 2f;
	
	ArrayList<Block> map;
	
	Rectangle hitbox = new Rectangle(x + 23, y + 3, 55, 105);
	
	Weapon weapon;
	
	Personnage enemy;
	
	BitmapFont weaponText = null;
	
	float weaponNameActual = 0;
	float weaponNameTime = 1;
	
	public Personnage(int id, int dieY)
	{
		this.dieY = dieY;
		this.id = id;
		collisions = new ArrayList<Polygon>();
		
		map = new ArrayList<Block>();
		
		blood.setTexture("blood.png");
		blood.setRate(8);
		blood.enableGravity(-.2f);
		blood.setLife(20);
		
		weaponText = FontManager.getInstance(50);
		
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
	}
	
	public void setEnnemy(Personnage ennemy)
	{
		this.enemy = ennemy;
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
		
		if(weaponNameActual > 0)
		{
			weaponText.setColor(0, 0, 0, weaponNameActual/weaponNameTime);
			weaponText.draw(GSB.sb, weapon.getName(), this.x, this.y + hitbox.height+30+(weaponNameTime-weaponNameActual)*100);
		}
	}
	
	public void renderUI()
	{
		GSB.hud.begin();
			ui.draw(GSB.hud);
		GSB.hud.end();
		if(id == 0)
		{
			GSB.srHud.begin(ShapeType.Filled);
				GSB.srHud.setColor(1, 0, 0, .7f);
				// Life
				GSB.srHud.rect(ui.getX()+64, ui.getY()+93, life*1.5f, 10);
				GSB.srHud.setColor(1, ((float)0xd0)/0xFF, ((float)0x14)/0xFF, .7f);
				// Ammo
				if(Options.ammoActivated)
				{
					GSB.srHud.rect(ui.getX()+64, ui.getY()+73, ((float)weapon.getAmmo()/weapon.getMaxAmmo())*150f, 10);
				}
				else
				{
					if(weapon instanceof BulletWeapon)
					{
						BulletWeapon weapon = (BulletWeapon) this.weapon;
						if(weapon.getLastFire() == 0)
							GSB.srHud.setColor(.1f, 1f, 0, .7f);
						GSB.srHud.rect(ui.getX()+64, ui.getY()+73, (1-(float)weapon.getLastFire()/weapon.getFireRate())*150f, 10);
					}
				}
			GSB.srHud.end();
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
		
			GSB.srHud.begin(ShapeType.Filled);
				GSB.srHud.setColor(1, 0, 0, .7f);
				// Life
				GSB.srHud.rect(ui.getX()+178, ui.getY()+93, -(life*1.5f), 10);
				GSB.srHud.setColor(1, ((float)0xd0)/0xFF, ((float)0x14)/0xFF, .7f);
				// Ammo
				if(Options.ammoActivated)
				{
					GSB.srHud.rect(ui.getX()+178, ui.getY()+73,-((float)weapon.getAmmo()/weapon.getMaxAmmo())*150f, 10);
				}
				else
				{
					if(weapon instanceof BulletWeapon)
					{
						BulletWeapon weapon = (BulletWeapon) this.weapon;
						if(weapon.getLastFire() == 0)
							GSB.srHud.setColor(.1f, 1f, 0, .7f);
						GSB.srHud.rect(ui.getX()+178, ui.getY()+73, -(1-(float)weapon.getLastFire()/weapon.getFireRate())*150f, 10);
					}
				}
			GSB.srHud.end();

			GSB.hud.begin();
				FontManager.get(10).draw(GSB.hud, life+"/100", ui.getX()+85, ui.getY()+101);
				if(Options.ammoActivated)
				{
					FontManager.get(10).draw(GSB.hud, weapon.getAmmo()+"/"+weapon.getMaxAmmo(), ui.getX()+85, ui.getY()+81);
				}
				FontManager.get(12).draw(GSB.hud, "Arme : "+weapon.getName(), ui.getX()+64, ui.getY()+60);
				FontManager.get(12).draw(GSB.hud, "Score", ui.getX()+190, ui.getY()+50);
				FontManager.get(12).draw(GSB.hud, ""+score, ui.getX()+210, ui.getY()+30);
				GSB.hud.draw(TextureManager.get("Mechant/avatar.png"), ui.getX()+192, ui.getY()+ui.getHeight()-50);
			GSB.hud.end();
		}
	}

	public void setWeapon(Weapon newweapon)
	{
		weaponNameActual = weaponNameTime;
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
		return y < getDieY() || life <= 0;
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
		if(invincible > 0)
		{
			invincible -= delta;
		}
		if(weaponNameActual > 0)
		{
			weaponNameActual -= delta;
		}
		if(bugCorector-- > 0)
		{
			delta = 1/60f;
		}
		
		if(weapon instanceof BulletWeapon)
		{
			if(((BulletWeapon)weapon).getAmmo() <= 0 && ((BulletWeapon)weapon).getName() != "Pistol")
			{
				this.setWeapon(new Pistol());
			}
		}
		
		blood.update(delta*2);
		time += delta;
		
		x += vx*delta*60;
		if(collision())
		{
			if(collided.getType() == 1)
			{
				collided = null;
				respawn();
			}
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
			jumps = 2;
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

		if(collided != null)
		{
			if(collided.getType() == 1)
			{
				collided = null;
				respawn();
			}
		}
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
					jumps = 2;
					float max = -1000000;
					for(int i = 1 ; i < collidedPoly.getTransformedVertices().length ; i += 2)
						if(collidedPoly.getTransformedVertices()[i] > max)
							max = collidedPoly.getTransformedVertices()[i];
					y = max-2.3f;
				}
				vy = 0;
			}
			else
			{
				if(jumps > 0)
					jumps = 1;
			}
		}
		
		if(isDead())
		{
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
				collidedPoly = collisions.get(i);
				collided = map.get(i);
				return true;
			}
		return false;
	}
	
	public void addCollision(Block c)
	{
		int type = c.getFlag();
		float[] vertices = null;
		if(c.getType() == 0)
		{
			if(type == Map.GROUND)
			{
				vertices = new float[4 * 2];
				vertices[0] = 0;
				vertices[1] = 0;
	
				vertices[2] = 256;
				vertices[3] = 0;
	
				vertices[4] = 256;
				vertices[5] = 210;
	
				vertices[6] = 0;
				vertices[7] = 210;
			}
			if(type == Map.RIGHT)
			{
				vertices = new float[5 * 2];
				vertices[0] = 10;
				vertices[1] = 210;
	
				vertices[2] = 10;
				vertices[3] = 180;
	
				vertices[4] = 190;
				vertices[5] = 0;
	
				vertices[6] = 256;
				vertices[7] = 0;
	
				vertices[8] = 256;
				vertices[9] = 210;
			}
			if(type == Map.LEFT)
			{
				vertices = new float[5 * 2];
				vertices[0] = 0;
				vertices[1] = 0;
	
				vertices[2] = 70;
				vertices[3] = 0;
	
				vertices[4] = 240;
				vertices[5] = 180;
	
				vertices[6] = 240;
				vertices[7] = 210;
	
				vertices[8] = 0;
				vertices[9] = 210;
			}
		}
		else if (c.getType() == 1)
		{
			vertices = new float[4 * 2];
			vertices[0] = 0;
			vertices[1] = 0;

			vertices[2] = 256;
			vertices[3] = 0;

			vertices[4] = 256;
			vertices[5] = 256;

			vertices[6] = 0;
			vertices[7] = 256;
		}
		collisions.add(new Polygon(vertices));
		map.add(c);
		collisions.get(collisions.size()-1).setPosition(c.getX()*256, c.getY()*256);
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

	Polygon collidedPoly;
	Block collided;
	
	public boolean collision()
	{
		hitbox.setPosition(x + 20, y + 3);
		for(int i = 0 ; i < collisions.size() ; i++)
			if(isCollision(collisions.get(i), hitbox))
			{
				collidedPoly = collisions.get(i);
				collided = map.get(i);
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
		GSB.srCam.begin(ShapeType.Line);
		GSB.srCam.setColor(Color.RED);
		for(Polygon p : collisions)
		{
			float[] vertices = p.getVertices();
			for(int i = 0 ; i < vertices.length ; i += 2)
			{
				GSB.srCam.line(p.getX()+vertices[i], p.getY()+vertices[i + 1], p.getX()+vertices[(i + 2) % vertices.length], p.getY()+vertices[(i + 3) % vertices.length]);
			}
		}
		GSB.srCam.rect(hitbox.getX(), hitbox.getY(), hitbox.getWidth(), hitbox.getHeight());
		GSB.srCam.end();
	}

	public void jump()
	{
		if(Game.debug)
		{
			vy = jumpspeed;
			jumping = true;
			return;
		}
		if(jumps > 0)
		{
			jumps--;
			jumping = true;
			vy = jumpspeed;
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
		
		invincible = 1f;
		
		enemy.score++;
		
		life = 100;
		jumping = false;
		moving = false;
		vy = 0;
		vx = 0;
		setWeapon(new Pistol());
		
		blood.setRate(100);
		blood.startEmitting();
		blood.update(1/60f);
		blood.stopEmitting();
		blood.setRate(8);
		
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
		if(i <= 0)
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

	public int getDieY()
	{
		return dieY;
	}

	public boolean isInvicible()
	{
		return invincible > 0;
	}
}
