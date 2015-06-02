package weapons;

import screens.Options;

public class Rifle extends BulletWeapon
{

	public Rifle()
	{
		super();
		this.setPath("Armes/gun.png");
		this.setRate(10);
		this.setPadding(27, 8);
		this.setMaxAmmo(40);
		this.setVelocity(40);
		this.setDamage(3);
		if(!Options.brawlModeActivated)
		{
			this.setDamage(10);
		}
		this.setVelocityScale(.8f);
		this.name = "Rifle";
	}

}
