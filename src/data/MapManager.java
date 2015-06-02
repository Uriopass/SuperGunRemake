package data;

import game.Map;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.badlogic.gdx.Gdx;

public class MapManager
{
	public static void save(Map toSave, String path)
	{
		try
		{
			if(!Gdx.files.internal(path+".spg").exists())
			{
				Gdx.files.internal(path+".spg").file().createNewFile();
			}
			FileOutputStream fout = new FileOutputStream(Gdx.files.internal(path+".spg").file());
			ObjectOutputStream oos = new ObjectOutputStream(fout);
			oos.writeObject(toSave);
			oos.close();
			fout.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static Map load(String path)
	{
		try
		{
			if(!Gdx.files.internal(path+".spg").exists())
			{
				Gdx.files.internal(path+".spg").file().createNewFile();
				save(getDefaultMap(), path);
				return getDefaultMap();
			}
			FileInputStream fin = new FileInputStream(Gdx.files.internal(path+".spg").file());
			ObjectInputStream ois = new ObjectInputStream(fin);
			try
			{
				Map m =  (Map) ois.readObject();
				ois.close();
				return m;
			}
			catch(Exception e)
			{
				System.out.println("[LOAD][MAP] Version map error detected !");
				Gdx.files.internal(path+".spg").file().delete();
				Gdx.files.internal(path+".spg").file().createNewFile();
				save(getDefaultMap(), path);
				ois.close();
				return getDefaultMap();
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	public static Map getDefaultMap()
	{
		Map m = new Map();
		m.addBox(new Coord(0, 1));
		m.addBox(new Coord(1, 1));
		m.addBox(new Coord(2, 1));
		m.addBox(new Coord(3, 1));
		m.addBox(new Coord(4, 1));
		m.addBox(new Coord(-1, 1));
		m.addBox(new Coord(0, 4));
		m.addBox(new Coord(1, 4));
		m.addBox(new Coord(2, 4));
		m.addBox(new Coord(3, 4));
		m.addBox(new Coord(-2, 1));
		m.addBox(new Coord(5, 1));
		m.addBox(new Coord(1, 6));
		m.addBox(new Coord(2, 6));
		m.addBox(new Coord(-5, -1));
		m.addBox(new Coord(-4, -1));
		m.addBox(new Coord(-3, -1));
		m.addBox(new Coord(6, -1));
		m.addBox(new Coord(7, -1));
		m.addBox(new Coord(8, -1));
		m.addBox(new Coord(-4, 4));
		m.addBox(new Coord(-4, 3));
		m.addBox(new Coord(7, 4));
		m.addBox(new Coord(7, 3));
		m.setPlayersPosition(new Coord(200, 300), new Coord(500, 300));
		return m;
	}
}
