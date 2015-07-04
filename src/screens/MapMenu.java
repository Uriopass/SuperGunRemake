package screens;

import java.util.ArrayList;

import ui_buttons.BigButton;
import ui_buttons.ScrollClass;
import ui_buttons.TextInput;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;

import data.FontManager;
import data.GSB;
import data.MapManager;

public class MapMenu implements Screen
{
	
	BigButton start, edit, delete, exit, newMap;
	
	TextInput name;
	BigButton ok;
	ArrayList<String> maps = new ArrayList<String>();

	String selected = "";
	
	boolean newMapText = false;
	
	public MapMenu()
	{
		try
		{
			FileHandle folder = Gdx.files.internal("./maps/");
			if(!folder.exists())
			{
				folder.file().mkdir();
			}
			for(FileHandle fh : folder.list())
			{
				if(fh.extension().equals("spg"))
				{
					System.out.println("[LOAD][MAP] loaded : "+fh.nameWithoutExtension());
					maps.add(fh.nameWithoutExtension());
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	
		if(maps.isEmpty())
		{
			maps.add("Default");
		}
		
		newMap = new BigButton("New map")
		{
			@Override
			protected void onClick()
			{
				name.setUpdate(true);
				newMapText = true;
			}
		};
		
		newMap.setLocation(Gdx.graphics.getWidth()-newMap.getWidth(), Gdx.graphics.getHeight()-newMap.getHeight());
		
		start = new BigButton("Play")
		{
			@Override
			protected void onClick()
			{
				((com.badlogic.gdx.Game)Gdx.app.getApplicationListener()).setScreen(new Game("./maps/"+selected));
			}
		};
		
		start.setLocation(Gdx.graphics.getWidth()-start.getWidth(), Gdx.graphics.getHeight()/2+start.getHeight());
		
		edit = new BigButton("Edit")
		{
			@Override
			protected void onClick()
			{
				((com.badlogic.gdx.Game)Gdx.app.getApplicationListener()).setScreen(new Editor("./maps/"+selected));
			}
		};
		edit.setLocation(Gdx.graphics.getWidth()-start.getWidth(), Gdx.graphics.getHeight()/2);
			
		delete = new BigButton("Delete")
		{
			@Override
			protected void onClick() 
			{
				Gdx.files.internal(selected+".spg").file().delete();
				maps.remove(selected);
				selected = "";
			}
		};
		delete.setLocation(Gdx.graphics.getWidth()-start.getWidth(), Gdx.graphics.getHeight()/2-delete.getHeight());
		
		exit = new BigButton("Back to main menu")
		{
			@Override
			protected void onClick()
			{
				((com.badlogic.gdx.Game)Gdx.app.getApplicationListener()).setScreen(new MainMenu());
			}
		};
		
		exit.setLocation(Gdx.graphics.getWidth()-exit.getWidth(), 0);
	
		name = new TextInput("Enter map name here");
		name.setLocation(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);
		name.center(true, true);
		name.setRegex("[a-z]|[A-Z]|\\040");
		
		
		Gdx.input.setInputProcessor(name);
		name.setUpdate(false);
		
		ok = new BigButton("Ok")
		{
			@Override
			protected void onClick()
			{
				newMapText = false;
				
				if(!maps.contains(name.getText()) && name.getText() != "")
				{
					maps.add(name.getText());
					MapManager.load("./maps/"+name.getText());
				}
				name.reset();
				name.setUpdate(false);
			}
		};
		ok.setLocation(Gdx.graphics.getWidth()/2, name.getY()-100);
		ok.center(true, false);
		
	}
	
	int scroll = 0;
	
	@Override
	public void render(float delta)
	{
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		if(!newMapText)
		{
			GSB.hud.begin();
			if(selected != "")
			{
				start.render(0);
				edit.render(0);
				delete.render(0);
			}
			exit.render(0);
			newMap.render(0);
			GSB.hud.end();
			
			int x = 30, y = Gdx.graphics.getHeight() - 150;
			
			GSB.srHud.begin(ShapeType.Filled);
				for(String s : maps)
				{
					if(s.equals(selected))
					{
						GSB.srHud.setColor(Color.LIGHT_GRAY);
					}
					else
					{
						GSB.srHud.setColor(0.9f, 0.9f, 0.9f, 1);
					}
					GSB.srHud.rect(x, y, 200, 100);
					x += 250;
					if((x + 250) > Gdx.graphics.getWidth() - exit.getWidth() - 100)
					{
						x = 30;
						y -= 150;
					}
				}
			GSB.srHud.end();
			GSB.hud.begin();

			x = 30;
			y = Gdx.graphics.getHeight() - 150;
			for(String s : maps)
			{
				GlyphLayout gl = new GlyphLayout(FontManager.get(18), s);
				FontManager.get(18).draw(GSB.hud, s, x+100-gl.width/2, y+55);
				x += 250;
				if((x + 250) > Gdx.graphics.getWidth() - exit.getWidth() - 100)
				{
					x = 30;
					y -= 150;
				}
			}
			GSB.hud.end();
		}
		else
		{
			GSB.hud.begin();
			name.render(0);
			ok.render(0);
			GSB.hud.end();
		}
		
		update(delta);
	}
	
	public void update(float delta)
	{
		if(!newMapText)
		{
			if(selected != "")
			{
				start.update();
				edit.update();
				delete.update();
			}
			exit.update();
			newMap.update();
			int x = 30, y = Gdx.graphics.getHeight() - 150;
			if(Gdx.input.justTouched())
			{
				String oldSelected = new String(selected);
				for(String s : maps)
				{
					if(new Rectangle(x, y, 200, 100).contains(Gdx.input.getX(), Gdx.graphics.getHeight()-Gdx.input.getY()))
					{
						selected = s;
						break;
					}
					else
					{
						selected = "";
					}
					x += 250;
					if((x + 250) > Gdx.graphics.getWidth() - exit.getWidth() - 100)
					{
						x = 30;
						y -= 150;
					}
				}
				if(oldSelected.equals(selected))
				{
					start.forceClick();
				}
			}
			
		}
		else
		{
			ok.update();
		}
	}
	
	
	

	@Override
	public void resize(int width, int height)
	{
		
	}

	@Override
	public void show()
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
		Gdx.input.setInputProcessor(new ScrollClass());
	}

	@Override
	public void dispose()
	{
		
	}
	
}
