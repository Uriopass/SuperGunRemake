package weapons;

import screens.Options;


public class Sniper extends BulletWeapon
{

	public Sniper()
	{
		super();
		this.setPath("Armes/sniper.png");
		this.setRate(120);
		this.setPadding(27, 8);
		this.setMaxAmmo(8);
		this.setVelocity(100);
		this.setDamage(10);
		if(!Options.brawlModeActivated)
		{
			this.setDamage(35);
		}
		this.setVelocityScale(1f);
		this.name = "Sniper";
	}
}
