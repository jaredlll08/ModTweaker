package modtweaker2.mods.thermalexpansion;

import minetweaker.MineTweakerAPI;
import modtweaker2.mods.thermalexpansion.handlers.Crucible;
import modtweaker2.mods.thermalexpansion.handlers.Furnace;

public class ThermalExpansion {

	public ThermalExpansion(){
		MineTweakerAPI.registerClass(Crucible.class);
		MineTweakerAPI.registerClass(Furnace.class);
		
	}
}
