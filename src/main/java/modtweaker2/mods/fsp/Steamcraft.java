package modtweaker2.mods.fsp;

import minetweaker.MineTweakerAPI;
import modtweaker2.mods.fsp.handlers.Crucible;
import modtweaker2.mods.fsp.handlers.Furnace;

public class Steamcraft {
    public Steamcraft() {
        MineTweakerAPI.registerClass(Crucible.class);
        MineTweakerAPI.registerClass(Furnace.class);
    }
}
