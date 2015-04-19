package modtweaker2.mods.tfcraft;

import minetweaker.MineTweakerAPI;
import modtweaker2.mods.tfcraft.handlers.Anvil;
import modtweaker2.mods.tfcraft.handlers.Barrel;
import modtweaker2.mods.tfcraft.handlers.Heat;
import modtweaker2.mods.tfcraft.handlers.Kiln;
import modtweaker2.mods.tfcraft.handlers.Loom;
import modtweaker2.mods.tfcraft.handlers.Quern;

public class TerraFirmaCraft {

	public TerraFirmaCraft(){
		MineTweakerAPI.registerClass(Anvil.class);
		MineTweakerAPI.registerClass(Barrel.class);
		MineTweakerAPI.registerClass(Heat.class);
		MineTweakerAPI.registerClass(Kiln.class);
		MineTweakerAPI.registerClass(Loom.class);
		MineTweakerAPI.registerClass(Quern.class);
	}
}
