package modtweaker2.mods.minestuck;

import minetweaker.MineTweakerAPI;
import modtweaker2.mods.minestuck.handlers.*;

public class Minestuck {
	public Minestuck() {
		MineTweakerAPI.registerClass(Alchemy.class);
//		MineTweakerAPI.registerClass(Combinations.class);
	}
}