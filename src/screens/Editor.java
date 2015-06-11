package screens;

import java.awt.Point;
import java.util.ArrayList;

import map.Block;
import map.Map;
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
	BigButton save, exit, changemode, parkour;
	Sprite gentil, mechant;
	Sprite normalBlock, voidBlock;
	
	int blockSize = 60;
	
	public Editor(final String mapName)
	{
		map = MapManager.load(mapName);
		map.computeTypes();
		Game.camera.zoom = 2f;
		
		gentil = new Sprite(SpriteManager.get("Gentil/gauche2.png"));
		gentil.setPosition(map.getGentilPos().getX(), map.getGentilPos().getY());
		gentil.flip(true, false);
		mechant = new Sprite(SpriteManager.get("Mechant/gauche2.png"));
		mechant.setPosition(map.getMechantPos().getX(), map.getMechantPos().getY());
		
		delete = TextureManager.getPixmap("delete.png");
		
		normalBlock = new Sprite(SpriteManager.get("sol.png"));
		voidBlock = new Sprite(SpriteManager.get("void.png"));
		
		normalBlock.setSize(blockSize-10, blockSize-10);
		voidBlock.setSize(blockSize+10, blockSize+10);

		normalBlock.setCenterX(Gdx.graphics.getWidth()/2-normalBlock.getWidth()/2-20);
		normalBlock.setY(Gdx.graphics.getHeight()-normalBlock.getHeight()-5);

		voidBlock.setCenterX(Gdx.graphics.getWidth()/2+voidBlock.getWidth()/2+10);
		voidBlock.setY(Gdx.graphics.getHeight()-normalBlock.getHeight()-15);
		
		save = new BigButton("Save")
		{
			@Override
			protected void onClick()
			{
				map.setPlayersPosition(new Coord((int)gentil.getX(), (int)gentil.getY()), new Coord((int)mechant.getX(), (int)mechant.getY()));
				MapManager.save(map, mapName);
			}
		};
		
		save.setLocation(Gdx.graphics.getWidth()/2, 0);
		save.center(true, false);
		
		exit = new BigButton("Exit")
		{
			protected void onClick() 
			{
				Gdx.input.setCursorImage(null, 0, 0);
				Game.resetCamera();
				GSB.update(Game.camera);
				((com.badlogic.gdx.Game)Gdx.app.getApplicationListener()).setScreen(new MapMenu());
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
		
		parkour = new BigButton("Generate parkour")
		{
			protected void onClick() 
			{
				map = MapManager.generateParkour();
				map.computeTypes();
			};
		};
		parkour.setLocation(0, Gdx.graphics.getHeight()-changemode.getHeight());
	}
	
	Point lastClick = new Point(0, 0);
	
	int type = 0;
	
	@Override
	public void render(float delta)
	{
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		map.render(delta);

		GSB.sb.begin();
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
			GSB.srCam.begin(ShapeType.Filled);
			if(deletemode)
				GSB.srCam.setColor(1f, 0, 0, .2f);
			else
				GSB.srCam.setColor(.9f, .9f, .9f, .3f);
			GSB.srCam.rect(gridx*256, gridy*256, 256, 256);
			
			GSB.srCam.end();
		Gdx.gl.glDisable(GL20.GL_BLEND);
		}

		GSB.srHud.begin(ShapeType.Filled);
		{
			 Gdx.gl.glEnable(GL20.GL_BLEND);
			    Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
			   GSB.srHud.setColor(.7f,.7f,.7f, 0.7f);
			   
			GSB.srHud.rect(Gdx.graphics.getWidth()/2 - blockSize*2, Gdx.graphics.getHeight()-blockSize, blockSize*4, blockSize);
			GSB.srHud.triangle(Gdx.graphics.getWidth()/2 - blockSize*2 - 30, Gdx.graphics.getHeight(),
					Gdx.graphics.getWidth()/2 - blockSize*2, Gdx.graphics.getHeight(),
					Gdx.graphics.getWidth()/2 - blockSize*2, Gdx.graphics.getHeight()-blockSize);
			GSB.srHud.triangle(Gdx.graphics.getWidth()/2 + blockSize*2 + 30, Gdx.graphics.getHeight(),
					Gdx.graphics.getWidth()/2 + blockSize*2, Gdx.graphics.getHeight(),
					Gdx.graphics.getWidth()/2 + blockSize*2, Gdx.graphics.getHeight()-blockSize);
			   GSB.srHud.setColor(.3f,.3f,.3f, 0.2f);
				
			if(type == 0)
			{
				GSB.srHud.rect(normalBlock.getX()-5, normalBlock.getY()-5, normalBlock.getWidth()+10, normalBlock.getHeight()+10);
			}
			if(type == 1)
			{
				GSB.srHud.rect(normalBlock.getX()+83, normalBlock.getY()-5, normalBlock.getWidth()+10, normalBlock.getHeight()+10);
			}
		}
		GSB.srHud.end();
		
		GSB.hud.begin();
			save.render(0);
			exit.render(0);
			changemode.render(0);
			if(Options.ParkourActivated)
			{
				parkour.render(0);
			}
			normalBlock.draw(GSB.hud); 
			voidBlock.draw(GSB.hud);
			
		GSB.hud.end();
		
		update(delta, gridx, gridy, x, y);
	}
	
	public boolean anythingElseIsHovered(float x, float y)
	{
		if(parkour.isHovered() ||changemode.isHovered() || save.isHovered() || gentil.getBoundingRectangle().contains(x, y) || mechant.getBoundingRectangle().contains(x, y))
			return true;
		if(normalBlock.getBoundingRectangle().contains(Gdx.input.getX(),Gdx.graphics.getHeight() - Gdx.input.getY()))
			return true;
		if(voidBlock.getBoundingRectangle().contains(Gdx.input.getX(),Gdx.graphics.getHeight() - Gdx.input.getY()))
			return true;
		return false;
	}
	boolean dragging;
	int whichone;
	Pixmap delete;
	
	boolean deletemode = false;
	
	private boolean alreadyBlocked(int x, int y, int type)
	{
		for(Block c : map.getBlocks())
		{
			if(c.getX() == x && c.getY() == y)
			{
				if(c.getType() == type)
				{
					return true;
				}
			}
		}
		return false;
	}
	
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
		if(Gdx.input.isButtonPressed(Input.Buttons.LEFT) && Gdx.input.justTouched())
		{
			if(normalBlock.getBoundingRectangle().contains(Gdx.input.getX(),Gdx.graphics.getHeight() - Gdx.input.getY()))
				type = 0;
			if(voidBlock.getBoundingRectangle().contains(Gdx.input.getX(),Gdx.graphics.getHeight() - Gdx.input.getY()))
				type = 1;
		}
		if(Gdx.input.isButtonPressed(Input.Buttons.LEFT) && !anythingElseIsHovered(x, y) && !dragging)
		{
			ArrayList<Block> mapBlocks = map.getBlocks();
			if(deletemode)
			{
				for(int i = 0 ; i < mapBlocks.size() ; i++)
				{
					Block c = mapBlocks.get(i);
					if(c.getX() == gridx && c.getY() == gridy)
					{
						mapBlocks.remove(i);
					}
				}
			}
			else
			{
				if(type == 0)
				{
					if(!alreadyBlocked(gridx, gridy, type))
					{
						map.addBox(new Coord(gridx, gridy), type);
					}
				}
				else
				{
					map.addBox(new Coord(gridx, gridy), type);
				}
			}

			map.computeTypes();
		}
		if(dragging)
		{
			if(whichone == 0)
			{
				gentil.setPosition(x-50, y-50);
			}
			if(whichone == 1)
			{
				mechant.setPosition(x-50, y-50);
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
			map.getBlocks().clear();
		}
		if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_1))
			type = 0;
		if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_2))
			type = 1;
		
		save.update();
		exit.update();
		changemode.update();
		if(Options.ParkourActivated)
			parkour.update();
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
