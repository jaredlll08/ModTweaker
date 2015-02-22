package modtweaker2.mods.thermalexpansion;

import minetweaker.MineTweakerAPI;
import modtweaker2.mods.thermalexpansion.handlers.Crucible;
import modtweaker2.mods.thermalexpansion.handlers.Furnace;
import modtweaker2.mods.thermalexpansion.handlers.Pulverizer;
import modtweaker2.mods.thermalexpansion.handlers.Sawmill;
import modtweaker2.mods.thermalexpansion.handlers.Smelter;
import modtweaker2.mods.thermalexpansion.handlers.Transposer;

public class ThermalExpansion {

	public ThermalExpansion(){
		MineTweakerAPI.registerClass(Crucible.class);
		MineTweakerAPI.registerClass(Furnace.class);
		MineTweakerAPI.registerClass(Pulverizer.class);
		MineTweakerAPI.registerClass(Sawmill.class);
		MineTweakerAPI.registerClass(Smelter.class);
		MineTweakerAPI.registerClass(Transposer.class);
	}
}
