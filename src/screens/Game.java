package screens;

import game.AI;
import game.Personnage;

import java.awt.Point;
import java.util.ArrayList;

import map.Block;
import map.Map;
import ui_buttons.BigButton;
import ui_buttons.ScrollClass;
import weapons.BulletWeapon;
import weapons.Pistol;
import boxs.WorldBoxs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector3;

import data.ColorManager;
import data.GSB;
import data.MapManager;
import data.SpriteManager;

public class Game implements Screen
{
	public static OrthographicCamera camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	
	Map m = new Map();
	Animation anim;
	ArrayList<Personnage> players;
	
	Sprite background;
	float originX, originY;
	WorldBoxs boxs;
	public static boolean debug = false;
	boolean IA = Options.IAActivated;
	AI theDevil;
	
	BigButton resume, exit;
	
	boolean paused = false;
	
	public static void initCameraAndGSB()
	{
		camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		
		GSB.init(camera);
		Gdx.input.setInputProcessor(new ScrollClass());
	}
	
	public static boolean invincible = false;
	
	public Game(String mapName)
	{
		ColorManager.reset();
		
		camera.zoom = 2.5f;
		
		boxs = new WorldBoxs();
		camera.zoom = 5f;
		
		background = SpriteManager.get("sky.png");
		
		float centerx = background.getWidth()/2;
		float centery = background.getHeight()/2;
		camera.position.x = centerx;
		camera.position.y = centery;
		originX = Gdx.graphics.getWidth()/2 - centerx;
		originY = Gdx.graphics.getHeight()/2 - centery;
		background.setPosition(originX, originY);
		
		m =  MapManager.load(mapName);
		m.computeTypes();
		
		theDevil = new AI(m);
		
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
		
		players = new ArrayList<Personnage>();
		
		players.add(new Personnage(0, (int)minY));
		players.add(new Personnage(1, (int)minY));
		players.get(0).setOrigin(m.getGentilPos());
		players.get(1).setOrigin(m.getMechantPos());
		players.get(0).setEnnemy(players.get(1));
		players.get(1).setEnnemy(players.get(0));
		
		players.get(0).setWeapon(new Pistol());
		players.get(1).setWeapon(new Pistol());
		
		for(Block c : m.getBlocks())
		{
			for(Personnage p : players)
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
				Game.camera.zoom = 1;
				GSB.update(Game.camera);
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

	Point lastClick = new Point(0, 0);

	float time = 0;
	boolean forcedMatrix = false;
	boolean JITMatrix = false;
	
	float fps = 0;
	int frames = 0;
	@Override
	public void render(float delta)
	{
		fps += delta;
		frames++;
		if(fps > 1)
		{
			fps = 0;
			Gdx.graphics.setTitle("Supergun : "+frames);
			frames = 0;
		}
		if(forcedMatrix || JITMatrix)
			delta /= 3;
		JITMatrix = false;
		for(Personnage p : players)
		{
			if(Math.abs(p.getVx()) > 90)
			{
				JITMatrix  = true;
			}
		}
		time += delta;
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT | (Gdx.graphics.getBufferFormat().coverageSampling?GL20.GL_COVERAGE_BUFFER_BIT_NV:0));

		background.setPosition(originX-camera.position.x/40, originY-camera.position.y/40);
		
		GSB.hud.begin();
			background.draw(GSB.hud);
		GSB.hud.end();

		m.render(delta);
		
		GSB.sb.begin();
			boxs.render();
			for(Personnage p : players)
				p.render();
		GSB.sb.end();
		
		for(Personnage p : players)
			p.renderUI();
		if(debug)
		{
			players.get(0).renderCollision();
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
			if(Gdx.input.isButtonPressed(Input.Buttons.LEFT) && !Gdx.input.justTouched())
			{
				camera.translate((lastClick.x - Gdx.input.getX())*camera.zoom, 0);
				lastClick.x = Gdx.input.getX();
				camera.translate(0, (Gdx.input.getY() - lastClick.y)*camera.zoom);
				lastClick.y = Gdx.input.getY();
			}
			if(Gdx.input.isButtonPressed(Input.Buttons.LEFT) && Gdx.input.justTouched())
			{
				lastClick.x = Gdx.input.getX();
				lastClick.y = Gdx.input.getY();
			}
	
			cameraScroll += ScrollClass.getScroll()/100f;
			
			if(Gdx.input.isKeyJustPressed(Input.Keys.M))
			{
				forcedMatrix = !forcedMatrix;
			}
			if(Gdx.input.isKeyJustPressed(Input.Keys.F3))
			{
				debug = !debug;
			}
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
			
			players.get(0).testWeapon(players.get(1), delta);
			players.get(1).testWeapon(players.get(0), delta);
			
			boxs.update(delta, players.get(0), players.get(1));
	
			for(Personnage p : players)
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
		
		log.log();
		
		camera.update();
		GSB.update(camera);
	}
	FPSLogger log = new FPSLogger();
	float cameraScroll = 0;
	private void computeCamera(float delta)
	{
		float centerx = players.get(0).getX() + players.get(1).getX();
		centerx /= 2;
		float centery = players.get(0).getY() + players.get(1).getY();
		centery /= 2;
		
		float lerp = 0.05f;
		Vector3 position = camera.position;
		position.x += (centerx - position.x) * lerp * delta * 60;
		position.y += (centery - position.y) * lerp * delta * 60;
		
		double distance = Math.sqrt(Math.pow(players.get(0).getX() - players.get(1).getX(), 2) + Math.pow(players.get(0).getY() - players.get(1).getY(), 2));
			
		camera.position.x = Math.round(camera.position.x*1000)/1000f;
		camera.position.y = Math.round(camera.position.y*1000)/1000f;
		
		lerp = 0.03f;
		
		camera.zoom += cameraScroll + (2.5f + ((Options.ParkourActivated)?3:0) + (float)(distance/(Gdx.graphics.getWidth())) - camera.zoom) * lerp * delta * 60;
		
		camera.zoom = Math.round(camera.zoom*1000)/1000f;
		
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
	}

	public void show()
	{
	}
	@Override
	public void resize(int width, int height)
	{
		
	}
}
