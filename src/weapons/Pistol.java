package weapons;

import screens.Game;
import screens.Options;
import data.SoundManager;

public class Pistol extends BulletWeapon
{
	public Pistol()
	{
		super();
		this.setPath("Armes/gun.png");
		this.setRate(30);
		this.setPadding(27, 8);
		this.setMaxAmmo(1);
		this.setVelocity(40);
		this.setDamage(5);
		if(!Options.brawlModeActivated)
		{
			this.setDamage(15);
		}
		this.setVelocityScale(.6f);
		this.name = "Pistol";
	}

	@Override
	public void setAmmo(int ammo)
	{
		this.ammo = maxammo;
	}

	@Override
	public void onFire()
	{
		if(Options.soundActivated)
		{
			if(Game.isGameSlowed())
			{
				SoundManager.get("pistol.wav").play(1, 0.5f, 0);
			}
			else
			{
				SoundManager.get("pistol.wav").play();
			}
		}
	}
}
