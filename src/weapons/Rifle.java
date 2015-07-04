package weapons;

import data.SoundManager;
import screens.Game;
import screens.Options;

public class Rifle extends BulletWeapon
{

	public Rifle()
	{
		super();
		this.setPath("Armes/rifle.png");
		this.setRate(10);
		this.setPadding(27, 8);
		this.setMaxAmmo(80);
		this.setVelocity(40);
		this.setDamage(3);
		if(!Options.get("brawl"))
		{
			this.setDamage(10);
		}
		this.setRecoil(7);
		this.setVelocityScale(.5f);
		this.name = "Rifle";
	}

	@Override
	public void onFire()
	{
		if(Options.get("sound"))
		{
			if(Game.isGameSlowed())
			{
				SoundManager.get("auto.ogg").play(1, 0.5f, 0);
			}
			else
			{
				SoundManager.get("auto.ogg").play();
			}
		}
	}
}
