package game;

import java.util.ArrayList;

import screens.Game;
import boxs.Box;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
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
	
	float x, y;
	
	float vy;
	ArrayList<Polygon> collisions;
	/**
	 * direction : True for right and false for left
	 */
	boolean direction = false;
	float time, count;
	int speed = 13;
	int jumpspeed = 32;
	int doublejumpspeed = 30;
	int dieY;
	int life = 100;
	Sprite ui;
	int score;
	
	Rectangle hitbox = new Rectangle(x + 23, y + 3, 55, 105);
	
	public Weapon weapon;
	
	public Weapon pistol, sniper;
	
	ArrayList<Bullet> bullets;
	Personnage ennemy;
	
	public Personnage(int id, int dieY)
	{
		this.dieY = dieY;
		this.id = id;
		collisions = new ArrayList<Polygon>();
		
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
		
		bullets = new ArrayList<Bullet>();
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
		
		pistol = new Weapon("pistol");
		pistol.owner = this;
		pistol.setPath("Armes/gun.png");
		pistol.setRate(30);
		pistol.setPadding(27, 8);
		pistol.setMaxAmmo(30);
		pistol.setVelocity(3);
		pistol.setDamage(15);
		
		weapon = pistol;
		
		sniper = new Weapon("sniper");
		sniper.owner = this;
		sniper.setPath("Armes/sniper.png");
		sniper.setRate(60);
		sniper.setPadding(27, 8);
		sniper.setMaxAmmo(8);
		sniper.setVelocity(7);
		sniper.setDamage(38);
	}

	public void setEnnemy(Personnage ennemy)
	{
		this.ennemy = ennemy;
	}
	
	public void render()
	{
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
		for(Bullet b : bullets)
			b.render();
	}
	
	public void renderUI()
	{
		GSB.hud.begin();
			ui.draw(GSB.hud);
		GSB.hud.end();
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		if(id == 0)
		{
			GSB.sr.begin(ShapeType.Filled);
				GSB.sr.setColor(1, 0, 0, .7f);
				// Life
				GSB.sr.rect(ui.getX()+64, ui.getY()+93, life*1.5f, 10);
				GSB.sr.setColor(1, ((float)0xd0)/0xFF, ((float)0x14)/0xFF, .7f);
				// Ammo
				GSB.sr.rect(ui.getX()+64, ui.getY()+73, ((float)weapon.ammo/weapon.maxammo)*150f, 10);
			GSB.sr.end();
			GSB.hud.begin();
				FontManager.get(10).draw(GSB.hud, life+"/100", ui.getX()+120, ui.getY()+101);
				FontManager.get(10).draw(GSB.hud, weapon.ammo+"/"+weapon.maxammo, ui.getX()+120, ui.getY()+81);
				FontManager.get(12).draw(GSB.hud, "Arme : "+weapon.name, ui.getX()+80, ui.getY()+60);
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
			GSB.sr.rect(ui.getX()+178, ui.getY()+73,-((float)weapon.ammo/weapon.maxammo)*150f, 10);
		GSB.sr.end();

			GSB.hud.begin();
				FontManager.get(10).draw(GSB.hud, life+"/100", ui.getX()+85, ui.getY()+101);
				FontManager.get(10).draw(GSB.hud, weapon.ammo+"/"+weapon.maxammo, ui.getX()+85, ui.getY()+81);
				FontManager.get(12).draw(GSB.hud, "Arme : "+weapon.name, ui.getX()+64, ui.getY()+60);
				FontManager.get(12).draw(GSB.hud, "Score", ui.getX()+190, ui.getY()+50);
				FontManager.get(12).draw(GSB.hud, ""+score, ui.getX()+200, ui.getY()+30);
				GSB.hud.draw(TextureManager.get("Mechant/avatar.png"), ui.getX()+192, ui.getY()+ui.getHeight()-50);
			GSB.hud.end();
		}
		Gdx.gl.glDisable(GL20.GL_BLEND);
	}

	public boolean isDead()
	{
		return y < dieY || life <= 0;
	}
	
	public void setLocation(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	
	public void setOrigin(int x, int y)
	{
		this.originX = x;
		this.originY = y;
		setLocation(x, y);
	}

	public void testBullets(ArrayList<Bullet> other)
	{
		for(int i = 0 ; i < other.size() ; i++)
		{
			Bullet b = other.get(i);
			if(hitbox.overlaps(b.hitbox))
			{
				life -= b.getDmg();
				other.remove(i);
				i--;
			}
		}
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
	
	public ArrayList<Bullet> getBullets()
	{
		return bullets;
	}
	
	public void update(float delta)
	{
		moving = false;
		time += delta;
		y += vy;
		vy -= 1;
		
		count += delta;
		if(count > 1)
		{
			life++;
			count --;
		}
		if(life > 100)
			life = 100;
		
		if(collision())
		{
			if(vy > 0)
			{
				while(collision())
				{
					y -= 3;
				}
			}
			if(vy < 0)
			{
				jumping = false;
				float max = -1000000;
				for(int i = 1 ; i < collidedWith.getTransformedVertices().length ; i += 2)
					if(collidedWith.getTransformedVertices()[i] > max)
						max = collidedWith.getTransformedVertices()[i];
				y = max - 2;
			}
			vy = 0;
		}
		
		if(isDead())
		{
			ennemy.score++;
			respawn();
		}
		
		weapon.update();
		for(Bullet b : bullets)
		{
			b.update();
		}
		updateBulletCollision();
	}

	public void addCollision(Coord c)
	{
		int type = c.getData();
		float[] vertices = null;
		if(type == 2 || type == 0)
		{
			vertices = new float[4 * 2];
			vertices[0] = c.getX() * 256;
			vertices[1] = c.getY() * 256;

			vertices[2] = c.getX() * 256 + 256;
			vertices[3] = c.getY() * 256;

			vertices[4] = c.getX() * 256 + 256;
			vertices[5] = c.getY() * 256 + 210;

			vertices[6] = c.getX() * 256;
			vertices[7] = c.getY() * 256 + 210;
		}
		if(type == 1)
		{
			vertices = new float[5 * 2];
			vertices[0] = c.getX() * 256 + 10;
			vertices[1] = c.getY() * 256 + 210;

			vertices[2] = c.getX() * 256 + 10;
			vertices[3] = c.getY() * 256 + 180;

			vertices[4] = c.getX() * 256 + 190;
			vertices[5] = c.getY() * 256;

			vertices[6] = c.getX() * 256 + 256;
			vertices[7] = c.getY() * 256;

			vertices[8] = c.getX() * 256 + 256;
			vertices[9] = c.getY() * 256 + 210;
		}
		if(type == -1)
		{
			vertices = new float[5 * 2];
			vertices[0] = c.getX() * 256;
			vertices[1] = c.getY() * 256;

			vertices[2] = c.getX() * 256 + 70;
			vertices[3] = c.getY() * 256;

			vertices[4] = c.getX() * 256 + 240;
			vertices[5] = c.getY() * 256 + 180;

			vertices[6] = c.getX() * 256 + 240;
			vertices[7] = c.getY() * 256 + 210;

			vertices[8] = c.getX() * 256;
			vertices[9] = c.getY() * 256 + 210;
		}
		collisions.add(new Polygon(vertices));
	}
	
	private boolean isCollision(Polygon p, Rectangle r)
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

	public Weapon getWeapon()
	{
		return weapon;
	}
	
	public void updateBulletCollision()
	{
		for(int i = 0 ; i < bullets.size() ; i++)
		{
			Rectangle hit = bullets.get(i).getHitbox();
			for(int j = 0 ; j < collisions.size() ; j++)
			{
				if(isCollision(collisions.get(j), hit))
				{
					bullets.remove(i);
					i--;
					break;
				}
			}
		}
	}
	
	/**
	 * direction : True for right and false for left
	 * 
	 * @param direction
	 */
	public void move(boolean direction)
	{

		if(this.direction != direction)
		{
			for(TextureRegion a : sprite.getKeyFrames())
			{
				a.flip(true, false);
			}
		}
		this.direction = direction;
		if(direction)
		{
			// Right

			x += speed;
			if(collision())
			{
				x -= speed;
			}
			else
			{
				moving = true;
			}
		}
		else
		{
			x -= speed;
			if(collision())
			{
				x += speed;
			}
			else
			{
				moving = true;
			}
		}
	}

	public void fire()
	{
		if(weapon.canShoot())
		{
			Bullet shoted = weapon.shoot();
			if(shoted != null)
			{
				bullets.add(shoted);
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
		if(doublejump)
		{
			doublejump = false;
			vy = doublejumpspeed;
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
		weapon.reset();
		weapon = pistol;
		weapon.reset();
	}

	public void setOrigin(Coord pos)
	{
		setOrigin(pos.getX(), pos.getY());
	}

	public void addLife(int i)
	{
		this.life += i;
		if(life > 100)
			life = 100;
	}
}
