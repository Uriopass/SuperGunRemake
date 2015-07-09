package screens;

import game.Player;
import game.WorldEntities;

import java.util.ArrayList;
import java.util.Collections;

import map.Block;
import map.Map;
import ui_buttons.BigButton;
import ui_buttons.ScrollClass;
import weapons.BulletWeapon;
import weapons.Pistol;
import AI.AI;
import boxs.WorldBoxs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector3;

import data.ColorManager;
import data.FontManager;
import data.GSB;
import data.MapManager;
import data.SpriteManager;

public class Game implements Screen
{
	public static OrthographicCamera camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	
	private static Vector3 position;
	public static void resetCamera()
	{
		camera.position.x = position.x;
		camera.position.y = position.y;
		
		camera.zoom = 1;
	}
	
	Map m = new Map();
	Animation anim;
	ArrayList<Player> players;
	
	Sprite background, slowed;
	float originX, originY;
	WorldBoxs boxs;
	public static boolean debug = false;
	boolean IA = Options.get("IA");
	AI theDevil;
	
	Music music;
	
	BigButton resume, exit;
	
	boolean paused = false;
	
	static float minx, miny, maxx, maxy;
	
	public WorldEntities we = new WorldEntities();
	
	public static void initCameraAndGSB()
	{
		camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		position = new Vector3(camera.position);
		GSB.init(camera);
		Gdx.input.setInputProcessor(new ScrollClass());
	}
	
	public static boolean invincible = false;
	
	public Game(String mapName)
	{
		ColorManager.reset();
		
		camera.zoom = 2.5f;
		
		music = Gdx.audio.newMusic(Gdx.files.internal("music.mp3"));
		music.setLooping(true);
		if(Options.get("music"))
			music.play();
		boxs = new WorldBoxs();
		camera.zoom = 5f;
		
		background = SpriteManager.get("sky.png");
		slowed = SpriteManager.get("slowed.png");
		
		float centerx = background.getWidth()/2;
		float centery = background.getHeight()/2;
		camera.position.x = centerx;
		camera.position.y = centery;
		originX = Gdx.graphics.getWidth()/2 - centerx;
		originY = Gdx.graphics.getHeight()/2 - centery;
		background.setPosition(originX, originY);
		slowed.setCenterX(Gdx.graphics.getWidth()/2);
		slowed.setY(Gdx.graphics.getHeight()-slowed.getHeight());
		
		m =  MapManager.load(mapName);
		m.computeTypes();
		
		boxs.setMap(m);
		/*
		m.addBox(new Coord(0,0));
		m.addBox(new Coord(1, 0));
		m.addBox(new Coord(2, 0));
		
		m.addBox(new Coord(5, 0));
		m.addBox(new Coord(6, 0));
		
		m.computeTypes();
		*/
		//MapManager.save(m.getCoords(), "test");
		float minY = 1000000000, maxy = -10000000;
		float minx = 1000000000, maxx = -10000000;
		for(Block c : m.getBlocks())
		{
			if(c.getY() < minY)
				minY = c.getY();
			if(c.getY() > maxy)
				maxy = c.getY();
			if(c.getX() < minx)
				minx = c.getX();
			if(c.getX() > maxx)
				maxx = c.getX();
		}
		Game.minx = minx;
		Game.miny = minY;
		Game.maxx = maxx;
		Game.maxy = maxy;
		
		maxy += 10;
		WorldBoxs.setMaxY(maxy);
		minY -= 10;
		minY *= 256;

		WorldBoxs.setMaxx(maxx);
		WorldBoxs.setMinx(minx);
		minx -= 40;
		maxx += 40;
		minx *= 256;
		maxx *= 256;
		BulletWeapon.setMinMaxX((int)minx, (int)maxx);
		
		players = new ArrayList<Player>();
		// TODO change players init
		players.add(new Player(0, (int)minY, this));
		players.add(new Player(1, (int)minY, this));
		players.get(0).setOrigin(m.getGentilPos());
		players.get(1).setOrigin(m.getMechantPos());
		players.get(0).setEnnemy(players.get(1));
		players.get(1).setEnnemy(players.get(0));
		
		for(Player p : players)
		{
			p.setWeapon(new Pistol());
		}


		theDevil = new AI(m);
		
		
		for(Player p : players)
		{
			we.addPersonnage(p);
		}
		
		for(Block c : m.getBlocks())
		{
			for(Player p : players)
			{
				p.addCollision(c);
			}
		}
		
		resume = new BigButton("Resume")
		{
			@Override
			protected void onClick()
			{
				paused = false;
			}
		};
		
		resume.setLocation(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);
		resume.center(true, false);
		
		exit = new BigButton("Exit")
		{
			protected void onClick() 
			{
				Game.resetCamera();
				GSB.update(Game.camera);
				music.stop();
				((com.badlogic.gdx.Game)Gdx.app.getApplicationListener()).setScreen(new MainMenu());
			};
		};
		
		exit.setLocation(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2 - resume.getHeight());
		exit.center(true, false);
		
		try
		{
			FileHandle folder = Gdx.files.internal(".");
			for(FileHandle fh : folder.list())
			{
				if(fh.extension().equals("spg"))
				{
					System.out.println("[LOAD][MAP] loaded : "+fh.nameWithoutExtension());
					//maps.add(fh.nameWithoutExtension());
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		 Gdx.gl.glEnable(GL20.GL_BLEND);
		    Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
	}

	float time = 0;
	static boolean forcedMatrix = false;
	static float JITMatrix = 0;
	
	float begin = 3;
	
	float fps = 0;
	int frames = 0;
	long temps = 0;
	
	public static boolean isGameSlowed()
	{
		return forcedMatrix || JITMatrix > 0;
	}
	
	boolean wait = false;
	
	@Override
	public void render(float delta)
	{
		fps += delta;
		frames++;
		if(fps > 1)
		{
			fps = 0;
			Gdx.graphics.setTitle("Supergun Arena : "+frames);
			frames = 0;
		}
		JITMatrix -= delta;
		if(isGameSlowed())
			delta /= 5;
		if(JITMatrix < 0)
		{
			JITMatrix = 0;
		}
		int count = 0;
		for(Player  p : players)
		{
			if(Math.abs(p.getVx()) > 90 && wait == false && JITMatrix == 0)
			{
				JITMatrix = 1f;
				wait = true;
			}
			if(Math.abs(p.getVx()) < 90)
			{
				count++;
			}
		}
		if(count == players.size())
			wait = false;
		time += delta;
		
		
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT | (Gdx.graphics.getBufferFormat().coverageSampling?GL20.GL_COVERAGE_BUFFER_BIT_NV:0));

		background.setPosition(originX-camera.position.x/80, originY-camera.position.y/80);
		background.setScale(Gdx.graphics.getWidth()/(background.getWidth()/2));
		
		GSB.hud.begin();
			background.draw(GSB.hud);
		GSB.hud.end();

		m.render(delta);
		
		GSB.sb.begin();
			boxs.render();
			for(Player p : players)
				p.render(delta);
		GSB.sb.end();
		
		we.render();
		
		for(Player p : players)
			p.renderUI();
		if(debug)
		{
			players.get(0).renderCollision();

			theDevil.renderPath();
		}
		if(begin > 0)
		{
			GSB.srHud.begin(ShapeType.Filled);
			 Gdx.gl.glEnable(GL20.GL_BLEND);
			    Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
				GSB.srHud.setColor(1, 1, 1, 0.4f);
				GSB.srHud.rect(0, Gdx.graphics.getHeight()/2-100, Gdx.graphics.getWidth(), 150);
			GSB.srHud.end();
		
			GSB.hud.begin();
			float ratio = (begin - (int)begin)*2 - 1;
			float xratio = ratio * ratio;
			if(ratio < 0)
				xratio = -xratio;
			xratio/=2;
			xratio += .5f;
			FontManager.get(85).draw(GSB.hud, ""+(int)(begin+1), xratio*Gdx.graphics.getWidth(), Gdx.graphics.getHeight()/2);
			GSB.hud.end();
		}

		
		if(isGameSlowed())
		{
			GSB.hud.begin();
				slowed.draw(GSB.hud, 0.7f);
			GSB.hud.end();
		}
		
		if(paused)
		{
			 Gdx.gl.glEnable(GL20.GL_BLEND);
			    Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
			GSB.srHud.begin(ShapeType.Filled);
				GSB.srHud.setColor(1, 1, 1, 0.4f);
				GSB.srHud.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			GSB.srHud.end();
			GSB.hud.begin();
				resume.render(0);
				exit.render(0);
			GSB.hud.end();
			
		}

		update(delta);
	}
	public void update(float delta)
	{
		if(!paused)
		{
	
			cameraScroll += ScrollClass.getScroll()/5f;
			
			if(Gdx.input.isKeyJustPressed(Input.Keys.M))
			{
				forcedMatrix = !forcedMatrix;
			}
			if(Gdx.input.isKeyJustPressed(Input.Keys.F3))
			{
				debug = !debug;
			}
			if(begin <= 0)
			{
				if(Gdx.input.isKeyPressed(Input.Keys.S))
				{
					players.get(0).move(false, delta);
				}
				if(Gdx.input.isKeyPressed(Input.Keys.F))
				{
					players.get(0).move(true, delta);
				}
				if(Gdx.input.isKeyJustPressed(Input.Keys.E))
				{
					players.get(0).jump();
				}
				if(Gdx.input.isKeyPressed(Input.Keys.SPACE))
				{
					players.get(0).fire();
				}
				
				if(!IA)
				{
					
					if(Gdx.input.isKeyPressed(Input.Keys.LEFT))
					{
						players.get(1).move(false, delta);
					}
					if(Gdx.input.isKeyPressed(Input.Keys.RIGHT))
					{
						players.get(1).move(true, delta);
					}
					if(Gdx.input.isKeyJustPressed(Input.Keys.UP))
					{
						players.get(1).jump();
					}
					if(Gdx.input.isKeyPressed(Input.Keys.CONTROL_RIGHT) || Gdx.input.isKeyPressed(Input.Keys.ENTER))
					{
						players.get(1).fire();
					}
				}
				else
				{
					theDevil.update(players.get(1), players.get(0), delta);
				}
			}
			else {
				begin -= delta;
			}
			
			we.update(delta);
			
			music.setVolume(1f);
			
			for(Player p : players)
			{
				for(Player p2 : players)
				{
					if(p.id != p2.id)
					{
						p.testWeapon(p2, delta);
					}
				}
			}
			
			boxs.update(delta, players);
	
			for(Player p : players)
				p.update(delta);
			
			computeCamera(delta);
		}

		if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE))
		{
			paused = true;
		}
		
		if(Gdx.input.isKeyJustPressed(Input.Keys.I))
		{
			invincible = !invincible;
		}
		
		if(paused)
		{
			resume.update();
			exit.update();
		}
		
		camera.update();
		GSB.update(camera);
	}
	float cameraScroll = 0;
	private void computeCamera(float delta)
	{
		float centerx = 0;
		for(Player p : players) centerx += p.getX();
		centerx /= players.size();
		float centery = 0;
		for(Player p : players) centery += p.getY();
		centery /= players.size();
		
		float lerp = 0.05f;
		Vector3 position = camera.position;
		position.x += (centerx - position.x) * lerp * delta * 60;
		position.y += (centery - position.y) * lerp * delta * 60;
		
		double distance = Math.sqrt(Math.pow(players.get(0).getX() - players.get(1).getX(), 2) + Math.pow(players.get(0).getY() - players.get(1).getY(), 2));
			
		camera.position.x = Math.round(camera.position.x*1000)/1000f;
		camera.position.y = Math.round(camera.position.y*1000)/1000f;
		
		lerp = 0.03f;
		
		camera.zoom += (cameraScroll + 2.5f + ((Options.get("parkour"))?3:0) + (float)(distance/(Gdx.graphics.getWidth())) - camera.zoom) * lerp * delta * 60;
		
		camera.zoom = Math.round(camera.zoom*1000)/1000f;
		
	}
	
	public static float getMaxx()
	{
		return maxx;
	}
	
	public static float getMaxy()
	{
		return maxy;
	}
	
	public static float getMinx()
	{
		return minx;
	}
	
	public static float getMiny()
	{
		return miny;
	}

	public void pause()
	{
		this.paused = true;
	}

	public void resume()
	{
	}

	public void hide()
	{
		this.paused = true;
	}

	public void dispose()
	{
		music.dispose();
	}

	public void show()
	{
	}
	@Override
	public void resize(int width, int height)
	{
		
	}
}
