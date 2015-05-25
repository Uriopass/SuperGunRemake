package data;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class SpriteManager
{
	
	static Map<String, Sprite> Sprites = new HashMap<String, Sprite>();
	static Map<String, Pixmap> pixs = new HashMap<String, Pixmap>();
	/**
	 * This is the main method and is used to get an Sprite
	 * @param filename the name of the file with the extension, it's picked in res/
	 * @return an Sprite class.
	 */
	public static Sprite get(String filename)
	{
		if(Sprites.get(filename) == null)
		{
			try
			{
				Sprites.put(filename, new Sprite(TextureManager.get(filename)));
			}
			catch (Exception e)
			{
				System.out.println("Sprite non trouvée : "+ filename);
				e.printStackTrace();
				System.exit(0);
			}
		}
		return Sprites.get(filename);
	}
}
