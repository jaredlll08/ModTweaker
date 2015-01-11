package modtweaker.mods.railcraft;

import minetweaker.MineTweakerAPI;
import modtweaker.mods.railcraft.handlers.BlastFurnace;
import modtweaker.mods.railcraft.handlers.CokeOven;
import modtweaker.mods.railcraft.handlers.RockCrusher;
import modtweaker.mods.railcraft.handlers.RollingMachine;

public class Railcraft {
    public Railcraft() {
        MineTweakerAPI.registerClass(BlastFurnace.class);
        MineTweakerAPI.registerClass(CokeOven.class);
        MineTweakerAPI.registerClass(RockCrusher.class);
        MineTweakerAPI.registerClass(RollingMachine.class);
    }
}
