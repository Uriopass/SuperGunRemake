package screens;

import game.Map;

import java.awt.Point;
import java.util.ArrayList;

import ui_buttons.BigButton;
import ui_buttons.ScrollClass;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

import data.Coord;
import data.GSB;
import data.MapManager;
import data.SpriteManager;
import data.TextureManager;

public class Editor implements Screen
{
	Map map;
	BigButton save, exit;
	Sprite gentil, mechant;
	
	public Editor()
	{
		map = MapManager.load("editor");
		Game.camera.zoom = 2f;
		GSB.setUpdateShapeRenderer(true);
		
		gentil = new Sprite(SpriteManager.get("Gentil/gauche2.png"));
		gentil.setPosition(map.getGentilPos().getX(), map.getGentilPos().getY());
		gentil.flip(true, false);
		mechant = new Sprite(SpriteManager.get("Mechant/gauche2.png"));
		mechant.setPosition(map.getMechantPos().getX(), map.getMechantPos().getY());
		
		save = new BigButton("Save")
		{
			@Override
			protected void onClick()
			{
				map.setPlayersPosition(new Coord((int)gentil.getX(), (int)gentil.getY()), new Coord((int)mechant.getX(), (int)mechant.getY()));
				MapManager.save(map, "editor");
			}
		};
		
		save.setLocation(Gdx.graphics.getWidth()/2, 0);
		save.center(true, false);
		
		exit = new BigButton("Exit")
		{
			protected void onClick() 
			{
				((com.badlogic.gdx.Game)Gdx.app.getApplicationListener()).setScreen(new MainMenu());
			};
		};
		
		exit.setLocation(Gdx.graphics.getWidth()-exit.getWidth(), 0);
	}
	
	Point lastClick = new Point(0, 0);
	
	@Override
	public void render(float delta)
	{
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		GSB.sb.begin();
		for(Coord c : map.getCoords())
		{
			String path = "";
			switch(c.getData())
			{
				case 2:
					path = "sol.png";
					break;
				case 1:
					path = "bord_g.png";
					break;
				case -1:
					path = "bord_d.png";
					break;
				default:
					path = "sol.png";
					break;
			}
			GSB.sb.draw(TextureManager.get(path), c.getX()*256, c.getY()*256);
		}
		gentil.draw(GSB.sb);
		mechant.draw(GSB.sb);
		GSB.sb.end();
		
		float x = Gdx.input.getX()*Game.camera.zoom - (Gdx.graphics.getWidth()*Game.camera.zoom)/2;
		float y = (Gdx.graphics.getHeight() - Gdx.input.getY())*Game.camera.zoom - (Gdx.graphics.getHeight()*Game.camera.zoom)/2;

		x += Game.camera.position.x;
		y += Game.camera.position.y;
		if(x < 0)
			x -= 256;
		if(y < 0)
			y -= 256;
		int gridx = (int)(x/256);
		int gridy = (int)(y/256);

		if(x < 0)
			x += 256;
		if(y < 0)
			y += 256;
		if(!anythingElseIsHovered(x, y))
		{
		 Gdx.gl.glEnable(GL20.GL_BLEND);
		    Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
			GSB.sr.begin(ShapeType.Filled);
			if(contains(gridx, gridy))
				GSB.sr.setColor(1f, 0, 0, .2f);
			else
				GSB.sr.setColor(.9f, .9f, .9f, .3f);
			GSB.sr.rect(gridx*256, gridy*256, 256, 256);
			
			GSB.sr.end();
		Gdx.gl.glDisable(GL20.GL_BLEND);
		}
		GSB.hud.begin();
			save.render(0);
			exit.render(0);
		GSB.hud.end();
		
		update(delta, gridx, gridy, x, y);
	}
	private int getNeighbour(Coord c)
	{
		boolean right = false;
		boolean left = false;
		for(Coord a : map.getCoords())
		{
			if(a.getY() == c.getY())
			{
				if(a.getX() == c.getX()-1)
					left = true;
				if(a.getX() == c.getX()+1)
					right = true;
			}
		}
		if(right && left)
			return 2;
		if(right)
			return 1;
		if(left)
			return -1;
		return 0;
	}
	
	private boolean contains(int x, int y)
	{
		for(Coord c : map.getCoords())
			if(c.getX() == x && c.getY() == y)
				return true;
		return false;
	}
	
	public boolean anythingElseIsHovered(float x, float y)
	{
		return save.isHovered() || gentil.getBoundingRectangle().contains(x, y) || mechant.getBoundingRectangle().contains(x, y);
	}
	boolean dragging;
	int whichone;
	public void update(float delta, int gridx, int gridy, float x, float y)
	{
		if(Gdx.input.isButtonPressed(Input.Buttons.RIGHT) && !Gdx.input.justTouched())
		{
			Game.camera.translate((lastClick.x - Gdx.input.getX())*Game.camera.zoom, 0);
			lastClick.x = Gdx.input.getX();
			Game.camera.translate(0, (Gdx.input.getY() - lastClick.y)*Game.camera.zoom);
			lastClick.y = Gdx.input.getY();
		}
		if(Gdx.input.isButtonPressed(Input.Buttons.RIGHT) && Gdx.input.justTouched())
		{
			lastClick.x = Gdx.input.getX();
			lastClick.y = Gdx.input.getY();
		}		
		if(((Gdx.input.isButtonPressed(Input.Buttons.LEFT) && Gdx.input.justTouched()) || Gdx.input.isButtonPressed(Input.Buttons.MIDDLE)) && !anythingElseIsHovered(x, y))
		{
			boolean removed = false;
			ArrayList<Coord> mapCoords = map.getCoords();
			for(int i = 0 ; i < mapCoords.size() ; i++)
			{
				Coord c = mapCoords.get(i);
				if(c.getX() == gridx && c.getY() == gridy)
				{
					mapCoords.remove(i);
					removed = true;
				}
			}
			if(!removed)
			{
				mapCoords.add(new Coord(gridx, gridy));
			}

			for(Coord c : map.getCoords())
			{
				c.setData(getNeighbour(c));
			}
		}
		if(dragging)
		{
			if(whichone == 0)
			{
				gentil.setPosition(x-20, y-20);
			}
			if(whichone == 1)
			{
				mechant.setPosition(x-20, y-20);
			}
		}
		if(Gdx.input.isButtonPressed(Input.Buttons.LEFT))
		{
			if(gentil.getBoundingRectangle().contains(x, y))
			{
				dragging = true;
				whichone = 0;
			}
			if(mechant.getBoundingRectangle().contains(x, y))
			{
				dragging = true;
				whichone = 1;
			}
		}
		else
		{
			dragging = false;
		}
		if(Gdx.input.isKeyPressed(Input.Keys.V))
		{
			map.getCoords().clear();
		}
		save.update();
		exit.update();
		Game.camera.zoom += ScrollClass.getScroll()/5f;
		Game.camera.update();
		GSB.update(Game.camera);
	}
	@Override
	public void show()
	{
		
	}
	public void resize(int width, int height)
	{
		
		
	}
	@Override
	public void pause()
	{
		
		
	}
	@Override
	public void resume()
	{
		
		
	}
	@Override
	public void hide()
	{
		
	}
	@Override
	public void dispose()
	{
		
	}
	
}
