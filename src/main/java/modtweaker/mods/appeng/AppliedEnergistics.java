package modtweaker.mods.appeng;

import minetweaker.MineTweakerAPI;
import modtweaker.mods.appeng.handlers.*;

public class AppliedEnergistics {
	
	
	public AppliedEnergistics() {
		MineTweakerAPI.registerClass(Inscriber.class);
		MineTweakerAPI.registerClass(Grind.class);
	}
}
