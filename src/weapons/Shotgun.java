package weapons;

import java.util.ArrayList;

import screens.Options;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

public class Shotgun extends BulletWeapon
{

	Sound reload, fire;
	public Shotgun()
	{
		super();

		this.setName("Shotgun");
		this.setPath("Armes/shotgun.png");
		this.setRate(45);
		this.setPadding(27, 8);
		this.setMaxAmmo(20);
		this.setVelocity(30);
		this.setDamage(3);
		if(!Options.brawlModeActivated)
		{
			this.setDamage(7);
		}
		this.setVelocityScale(1.2f);
		reload = Gdx.audio.newSound(Gdx.files.internal("Armes/reload.mp3"));
		fire = Gdx.audio.newSound(Gdx.files.internal("Armes/pistol.mp3"));
	}

	@Override
	protected ArrayList<Bullet> getFiredBullets(Texture text)
	{
		ArrayList<Bullet> fired = new ArrayList<Bullet>();
		fired.add(new Bullet(owner.getX() + paddingx + text.getWidth(), owner.getY() + paddingy + text.getHeight()/2 + 5, velocity, 5, damage));
		fired.add(new Bullet(owner.getX() + paddingx + text.getWidth(), owner.getY() + paddingy + text.getHeight()/2 + 5, velocity, 0, damage));
		fired.add(new Bullet(owner.getX() + paddingx + text.getWidth(), owner.getY() + paddingy + text.getHeight()/2 + 5, velocity, -5, damage));
		return fired;
	}
	@Override
	public void onReload()
	{
		
	}
	
	@Override
	public void onFire()
	{
		
	}
}
