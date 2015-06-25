package weapons;

import java.util.ArrayList;

import screens.Game;
import screens.Options;

import com.badlogic.gdx.graphics.Texture;

import data.SoundManager;
import entities.Bullet;

public class Shotgun extends BulletWeapon
{
	public Shotgun()
	{
		super();

		this.setName("Shotgun");
		this.setPath("Armes/shotgun.png");
		this.setRate(60);
		this.setPadding(27, 8);
		this.setMaxAmmo(35);
		this.setVelocity(30);
		this.setDamage(3);
		if(!Options.get("brawl"))
		{
			this.setDamage(7);
		}
		this.setVelocityScale(.7f);
	}

	@Override
	protected ArrayList<Bullet> getFiredBullets(Texture text)
	{
		ArrayList<Bullet> fired = new ArrayList<Bullet>();
		fired.add(new Bullet(owner.getX() + paddingx, owner.getY() + paddingy + text.getHeight() / 2 + 5, velocity, 5, damage, velocityScale));
		fired.add(new Bullet(owner.getX() + paddingx, owner.getY() + paddingy + text.getHeight() / 2 + 5, velocity, 0, damage, velocityScale));
		fired.add(new Bullet(owner.getX() + paddingx, owner.getY() + paddingy + text.getHeight() / 2 + 5, velocity, -5, damage, velocityScale));
		return fired;
	}

	@Override
	public void onReload()
	{

	}

	@Override
	public void onFire()
	{
		if(Options.get("sound"))
		{
			if(Game.isGameSlowed())
			{
				SoundManager.get("shotgun.ogg").play(1, 0.5f, 0);
			}
			else
			{
				SoundManager.get("shotgun.ogg").play();
			}
		}
	}
}
