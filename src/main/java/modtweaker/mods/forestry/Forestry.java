package modtweaker.mods.forestry;

import minetweaker.MineTweakerAPI;
import modtweaker.mods.forestry.handlers.*;

public class Forestry {
	
	public Forestry() {
		MineTweakerAPI.registerClass(Fermenter.class);
		MineTweakerAPI.registerClass(Still.class);
		MineTweakerAPI.registerClass(Moistener.class);
		MineTweakerAPI.registerClass(Carpenter.class);
		MineTweakerAPI.registerClass(Squeezer.class);
		MineTweakerAPI.registerClass(Centrifuge.class);
		MineTweakerAPI.registerClass(ThermionicFabricator.class);
	}
}
