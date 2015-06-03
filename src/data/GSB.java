package data;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;

public class GSB 
{
	public static SpriteBatch sb;
	public static SpriteBatch hud;
	public static ShapeRenderer srCam;
	public static ShapeRenderer srHud;
	
	static Matrix4 original;
	public static void init(OrthographicCamera cam)
	{
		original = new Matrix4(cam.combined);
		sb = new SpriteBatch();
		sb.setProjectionMatrix(cam.combined);
		hud = new SpriteBatch();
		hud.setProjectionMatrix(cam.combined);
		srCam = new ShapeRenderer();
		srCam.setProjectionMatrix(cam.combined);
		srHud = new ShapeRenderer();
	}
	
	public static void update(OrthographicCamera cam)
	{
		sb.setProjectionMatrix(cam.combined);
		srCam.setProjectionMatrix(cam.combined);
	}
}
