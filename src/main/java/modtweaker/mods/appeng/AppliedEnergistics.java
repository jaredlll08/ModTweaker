package modtweaker.mods.appeng;

import minetweaker.MineTweakerAPI;
import modtweaker.mods.appeng.handlers.Inscriber;

public class AppliedEnergistics {
	public AppliedEnergistics() {
		MineTweakerAPI.registerClass(Inscriber.class);
	}
}
