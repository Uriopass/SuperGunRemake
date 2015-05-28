package screens;

import game.Map;
import game.Personnage;

import java.awt.Point;
import java.util.ArrayList;

import ui_buttons.ScrollClass;
import weapons.BulletWeapon;
import boxs.WorldBoxs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;

import data.ColorManager;
import data.Coord;
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
	
	
	public static void initCameraAndGSB()
	{
		camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		GSB.init(camera);
		Gdx.input.setInputProcessor(new ScrollClass());
	}
	public Game()
	{
		ColorManager.reset();
		
		boxs = new WorldBoxs();
		camera.zoom = 5f;
		
		background = SpriteManager.get("sky.png");
		
		float centerx = background.getWidth()/2;
		float centery = background.getHeight()/2;
		originX = Gdx.graphics.getWidth()/2 - centerx;
		originY = Gdx.graphics.getHeight()/2 - centery;
		background.setPosition(originX, originY);
		
		m =  MapManager.load("editor");
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
		for(Coord c : m.getCoords())
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
		minx -= 20;
		maxx += 20;
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
		for(Coord c : m.getCoords())
		{
			for(Personnage p : players)
			{
				p.addCollision(c);
			}
		}
		GSB.setUpdateShapeRenderer(false);
		

		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
	}

	Point lastClick = new Point(0, 0);

	float time = 0;
	boolean matrix = false;
	@Override
	public void render(float delta)
	{
		if(matrix)
			delta /= 3;
		time += delta;
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		background.setPosition(originX-camera.position.x/40, originY-camera.position.y/40);
		
		GSB.hud.begin();
			background.draw(GSB.hud);
		GSB.hud.end();
		
		GSB.sb.begin();
			m.render();
			boxs.render();
			for(Personnage p : players)
				p.render();
		GSB.sb.end();
		
		for(Personnage p : players)
			p.renderUI();
		
		//players.get(0).renderCollision();
		update(delta);
	}

	public void update(float delta)
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

		camera.zoom += ScrollClass.getScroll()/5f;
		for(Personnage p : players)
			p.update(delta);
		
		if(Gdx.input.isKeyJustPressed(Input.Keys.M))
		{
			matrix = !matrix;
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
		players.get(0).testWeapon(players.get(1));
		players.get(1).testWeapon(players.get(0));
		
		boxs.update(delta, players.get(0), players.get(1));
		
		computeCamera();
		
		log.log();
		
		camera.update();
		GSB.update(camera);
	}
	FPSLogger log = new FPSLogger();
	private void computeCamera()
	{
		float centerx = players.get(0).getX() + players.get(1).getX();
		centerx /= 2;
		float centery = players.get(0).getY() + players.get(1).getY();
		centery /= 2;
		
		camera.position.x = centerx;
		camera.position.y = centery;
		
		double distance = Math.sqrt(Math.pow(players.get(0).getX() - players.get(1).getX(), 2) + Math.pow(players.get(0).getY() - players.get(1).getY(), 2));
			
		camera.position.x = Math.round(camera.position.x*1000)/1000f;
		camera.position.y = Math.round(camera.position.y*1000)/1000f;
		
		camera.zoom = 2.5f + (float)(distance/(Gdx.graphics.getWidth()));
		
		camera.zoom = Math.round(camera.zoom*1000)/1000f;
		
	}
	
	public void resize(int width, int height)
	{
		
	}

	public void pause()
	{
	}

	public void resume()
	{
	}

	public void hide()
	{
	}

	public void dispose()
	{
	}

	public void show()
	{
	}
}
