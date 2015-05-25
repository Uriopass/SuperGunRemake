package data;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;

public class ColorManager
{
	static ArrayList<Color> colors = new ArrayList<Color>();
	
	public static Color decode(String str)
	{
		str = str.substring(1, str.length());
		Color c = new Color(Integer.decode("#"+str.substring(0, 2))/255f, Integer.decode("#"+str.substring(2, 4))/255f,Integer.decode("#"+str.substring(4, 6))/255f, 1);
		return c;
	}
	
	public static Color decode(float r, float g, float b) // percent
	{
		return new Color(r/100, g/100, b/100, 1f);
	}
	
	public static void reset()
	{
		colors.clear();
		/*;// Purple is S0 D4RK
		colors.add(1, new Color(decode("#96f97b")));
		colors.add(2, new Color(decode("#FF2400")));
		;// Yay, sand ftw !
		colors.add(3, new Color(decode("#3399CC")));
		*/
		// war3 colors
		colors.add(decode(100.00f, 1.17f, 1.17f)); // red
		colors.add(decode(9.80f, 90.20f, 72.55f)); // teal
		colors.add(decode("#D797EB"));             // purple
		colors.add(decode("#EFDD6F"));             //yellow
		colors.add(decode(99.61f, 72.94f, 5.49f)); //orange
		colors.add(decode(12.55f, 75.30f, 0.00f)); //green
		colors.add(decode(89.80f, 35.69f, 69.02f));//pink
		colors.add(decode(58.43f, 58.82f, 59.21f));//grey
		colors.add(decode(49.41f, 74.90f, 94.51f));//light blue
	}
	
	public static Color getBeautifulColor()
	{
		int id = (int)(Math.random()*colors.size());
		Color c = colors.get(id);
		colors.remove(id);
		return c;
	}
}
