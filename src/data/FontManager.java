package data;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

public class FontManager
{
	static FreeTypeFontGenerator generator;
	static FreeTypeFontParameter parameter;
	
	static Map<Integer, BitmapFont> fonts = new HashMap<Integer, BitmapFont>();
	/**
	 * This is the main method and is used to get an Texture
	 * @param filename the name of the file with the extension, it's picked in res/
	 * @return an Texture class.
	 */
	static boolean initialize = true;
	public static BitmapFont get(int size)
	{
		if(initialize)
		{
			generator = new FreeTypeFontGenerator(Gdx.files.internal("Square.ttf"));
			parameter = new FreeTypeFontParameter();
			initialize = false;
		}
		if(fonts.get(size) == null)
		{
			try
			{
				parameter.size = size;
				
				fonts.put(size, generator.generateFont(parameter));
				fonts.get(size).setColor(Color.BLACK);
			}
			catch (Exception e)
			{
				System.out.println("Impossible de génerer cette taille (oui c'est WTF) : "+ size);
				e.printStackTrace();
				System.exit(0);
			}
		}
		return fonts.get(size);
	}
}
