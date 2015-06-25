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
		setDamage(5);
		setVelocity(60);
	}
	
	@Override
	public void render(float delta)
	{
		if(owner.getDirection())
			s.setOrigin(17, 17);
		else {
			s.setOrigin(s.getWidth()-17, 17);
		}
		super.render(delta);
	}
	
	@Override
	protected void onSwing()
	{
		SoundManager.get("swoosh.ogg").play();
	}
}
