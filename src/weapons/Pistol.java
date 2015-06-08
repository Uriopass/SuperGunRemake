package weapons;

import screens.Options;

public class Pistol extends BulletWeapon
{
	public Pistol()
	{
		super();
		this.setPath("Armes/gun.png");
		this.setRate(20);
		this.setPadding(27, 8);
		this.setMaxAmmo(30);
		this.setVelocity(40);
		this.setDamage(5);
		if(!Options.brawlModeActivated)
		{
			this.setDamage(15);
		}
		this.setVelocityScale(1);
		this.name = "Pistol";
	}
}
