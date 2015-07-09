package weapons;

import screens.Game;
import screens.Options;
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
		setVelocity(80);
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
		if(Options.get("sound"))
		{
			if(Game.isGameSlowed())
			{
				SoundManager.get("swoosh.ogg").play(1, 0.5f, 0);
			}
			else
			{
				SoundManager.get("swoosh.ogg").play();
			}
		}
	}
}
