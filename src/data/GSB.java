package data;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;

public class GSB 
{
	public static boolean updateShapeRenderer = false;
	public static SpriteBatch sb;
	public static SpriteBatch hud;
	public static ShapeRenderer sr;
	static Matrix4 original;
	public static void init(OrthographicCamera cam)
	{
		original = new Matrix4(cam.combined);
		sb = new SpriteBatch();
		sb.setProjectionMatrix(cam.combined);
		hud = new SpriteBatch();
		hud.setProjectionMatrix(cam.combined);
		sr = new ShapeRenderer();
		sr.setProjectionMatrix(cam.combined);
	}
	
	public static void update(OrthographicCamera cam)
	{
		sb.setProjectionMatrix(cam.combined);
		if(updateShapeRenderer)
		{
			sr.setProjectionMatrix(cam.combined);
		}
	}
	
	public static void setUpdateShapeRenderer(boolean updateShapeRenderer)
	{
		if(updateShapeRenderer == false)
		{
			sr.setProjectionMatrix(original);
		}
		GSB.updateShapeRenderer = updateShapeRenderer;
	}
}
