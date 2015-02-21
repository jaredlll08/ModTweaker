package modtweaker2.mods.railcraft;

import minetweaker.MineTweakerAPI;
import modtweaker2.mods.railcraft.handlers.BlastFurnace;
import modtweaker2.mods.railcraft.handlers.CokeOven;
import modtweaker2.mods.railcraft.handlers.RockCrusher;
import modtweaker2.mods.railcraft.handlers.RollingMachine;

public class Railcraft {
    public Railcraft() {
        MineTweakerAPI.registerClass(BlastFurnace.class);
        MineTweakerAPI.registerClass(CokeOven.class);
        MineTweakerAPI.registerClass(RockCrusher.class);
        MineTweakerAPI.registerClass(RollingMachine.class);
    }
}
