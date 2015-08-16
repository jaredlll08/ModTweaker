package modtweaker2.mods.extraUtils;

import minetweaker.MineTweakerAPI;
import modtweaker2.mods.appeng.handlers.Grind;
import modtweaker2.mods.extraUtils.handlers.QED;

public class ExtraUtils {

	public ExtraUtils() {
		MineTweakerAPI.registerClass(QED.class);
	}
}
