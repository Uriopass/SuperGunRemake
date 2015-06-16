package weapons;

import data.SoundManager;
import screens.Game;
import screens.Options;

public class Sniper extends BulletWeapon
{

	public Sniper()
	{
		super();
		this.setPath("Armes/sniper.png");
		this.setRate(120);
		this.setPadding(27, 8);
		this.setMaxAmmo(15);
		this.setVelocity(100);
		this.setDamage(10);
		if(!Options.brawlModeActivated)
		{
			this.setDamage(35);
		}
		this.setVelocityScale(.7f);
		this.name = "Sniper";
	}

	@Override
	public void onFire()
	{
		if(Options.soundActivated)
		{
			if(Game.isGameSlowed())
			{
				SoundManager.get("sniper.ogg").play(1, 0.5f, 0);
			}
			else
			{
				SoundManager.get("sniper.ogg").play();
			}
		}
	}
}
