package modtweaker.mods.thermalexpansion;

import minetweaker.MineTweakerAPI;
import modtweaker.mods.thermalexpansion.handlers.Crucible;
import modtweaker.mods.thermalexpansion.handlers.Furnace;
import modtweaker.mods.thermalexpansion.handlers.Pulverizer;
import modtweaker.mods.thermalexpansion.handlers.Sawmill;
import modtweaker.mods.thermalexpansion.handlers.Smelter;
import modtweaker.mods.thermalexpansion.handlers.Transposer;

public class ThermalExpansion {
    public ThermalExpansion() {
        MineTweakerAPI.registerClass(Crucible.class);
        MineTweakerAPI.registerClass(Furnace.class);
        MineTweakerAPI.registerClass(Pulverizer.class);
        MineTweakerAPI.registerClass(Sawmill.class);
        MineTweakerAPI.registerClass(Smelter.class);
        MineTweakerAPI.registerClass(Transposer.class);
    }
}
