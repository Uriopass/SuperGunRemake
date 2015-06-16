package weapons;

import data.SoundManager;

public class BaseballBat extends MeleeWeapon
{
	public BaseballBat()
	{
		super();
		
		this.setPath("Armes/batte.png");
		this.setMaxAmmo(0);
		this.setName("Baseball bat");
		this.setPadding(28, 12);
		s.setOrigin(17, 17);
	}
	
	@Override
	protected void onSwing()
	{
		SoundManager.get("swoosh.ogg").play();
	}
}
