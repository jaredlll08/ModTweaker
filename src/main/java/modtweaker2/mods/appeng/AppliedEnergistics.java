package modtweaker2.mods.appeng;

import minetweaker.MineTweakerAPI;
import modtweaker2.mods.appeng.handlers.Grind;
import modtweaker2.mods.appeng.handlers.Inscriber;

public class AppliedEnergistics {

	public AppliedEnergistics() {
		MineTweakerAPI.registerClass(Inscriber.class);
		MineTweakerAPI.registerClass(Grind.class);
	}
}
