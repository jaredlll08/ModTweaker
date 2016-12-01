package modtweaker.mods.extrautils;

import minetweaker.MineTweakerAPI;
import modtweaker.mods.extrautils.handlers.Resonator;

public class ExtraUtilities {
	
	
	public ExtraUtilities() {
		MineTweakerAPI.registerClass(Resonator.class);
	}
}
