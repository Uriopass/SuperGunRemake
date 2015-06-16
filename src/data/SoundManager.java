package data;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public class SoundManager
{
	
	static Map<String, Sound> sounds = new HashMap<String, Sound>();
	/**
	 * This is the main method and is used to get an Sound
	 * @param filename the name of the file with the extension, it's picked in res/
	 * @return an Sound class.
	 */
	public static Sound get(String filename)
	{
		if(sounds.get(filename) == null)
		{
			try
			{
				sounds.put(filename, Gdx.audio.newSound(Gdx.files.internal(filename)));
			}
			catch (Exception e)
			{
				System.out.println("Son non trouvé : "+ filename);
				e.printStackTrace();
				System.exit(0);
			}
		}
		return sounds.get(filename);
	}
}
