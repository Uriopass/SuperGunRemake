package data;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;

public class TextureManager
{
	
	static Map<String, Texture> textures = new HashMap<String, Texture>();
	static Map<String, Pixmap> pixs = new HashMap<String, Pixmap>();
	/**
	 * This is the main method and is used to get an Texture
	 * @param filename the name of the file with the extension, it's picked in res/
	 * @return an Texture class.
	 */
	public static Texture get(String filename)
	{
		if(textures.get(filename) == null)
		{
			try
			{
				textures.put(filename, new Texture(Gdx.files.internal(filename)));
				System.out.println("[LOAD][TEXTURE] loaded : "+filename);
			}
			catch (Exception e)
			{
				System.out.println("Couldn't load texture : "+ filename);
				e.printStackTrace();
				System.exit(0);
			}
		}
		return textures.get(filename);
	}
	public static Pixmap getPixmap(String filename)
	{
		if(pixs.get(filename) == null)
		{
			try
			{
				pixs.put(filename, new Pixmap(Gdx.files.internal(filename)));
			}
			catch (Exception e)
			{
				System.out.println("Couldn't find pixmap : "+ filename);
				e.printStackTrace();
				System.exit(0);
			}
		}
		return pixs.get(filename);
	}
	
	
}
