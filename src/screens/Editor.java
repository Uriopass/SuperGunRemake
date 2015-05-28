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
import com.badlogic.gdx.graphics.Pixmap;
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
	BigButton save, exit, changemode;
	Sprite gentil, mechant;
	
	public Editor()
	{
		map = MapManager.load("editor");
		map.computeTypes();
		Game.camera.zoom = 2f;
		GSB.setUpdateShapeRenderer(true);
		
		gentil = new Sprite(SpriteManager.get("Gentil/gauche2.png"));
		gentil.setPosition(map.getGentilPos().getX(), map.getGentilPos().getY());
		gentil.flip(true, false);
		mechant = new Sprite(SpriteManager.get("Mechant/gauche2.png"));
		mechant.setPosition(map.getMechantPos().getX(), map.getMechantPos().getY());
		
		delete = TextureManager.getPixmap("delete.png");
		
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
		
		changemode = new BigButton("Switch mode")
		{
			protected void onClick() 
			{
				deletemode = !deletemode;
				if(deletemode)
					Gdx.input.setCursorImage(delete, 16, 16);
				else
					Gdx.input.setCursorImage(null, 0, 0);
			};
		};
		
		changemode.setLocation(0, 0);
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
			String path = "sol.png";
			int flag = c.getData();
			
			if(flag == Map.RIGHT)
			{
				path = "bord_g.png";
			}
			if(flag == Map.LEFT)
			{
				path = "bord_d.png";
			}
			System.out.println(c + " : "+ flag);
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
			if(deletemode)
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
			changemode.render(0);
		GSB.hud.end();
		
		update(delta, gridx, gridy, x, y);
	}
	
	public boolean anythingElseIsHovered(float x, float y)
	{
		return changemode.isHovered() || save.isHovered() || gentil.getBoundingRectangle().contains(x, y) || mechant.getBoundingRectangle().contains(x, y);
	}
	boolean dragging;
	int whichone;
	Pixmap delete;
	
	boolean deletemode = false;
	
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
		if(Gdx.input.isButtonPressed(Input.Buttons.LEFT) && !anythingElseIsHovered(x, y))
		{
			ArrayList<Coord> mapCoords = map.getCoords();
			if(deletemode)
			{
				for(int i = 0 ; i < mapCoords.size() ; i++)
				{
					Coord c = mapCoords.get(i);
					if(c.getX() == gridx && c.getY() == gridy)
					{
						mapCoords.remove(i);
					}
				}
			}
			else
			{
				mapCoords.add(new Coord(gridx, gridy));
			}

			map.computeTypes();
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
		changemode.update();
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
