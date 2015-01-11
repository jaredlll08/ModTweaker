package modtweaker.mods.fsp;

import minetweaker.MineTweakerAPI;
import modtweaker.mods.fsp.handlers.Crucible;
import modtweaker.mods.fsp.handlers.Furnace;

public class Steamcraft {
    public Steamcraft() {
        MineTweakerAPI.registerClass(Crucible.class);
        MineTweakerAPI.registerClass(Furnace.class);
    }
}
