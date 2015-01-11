package modtweaker.mods.hee;

import minetweaker.MineTweakerAPI;
import modtweaker.mods.hee.handlers.EssenceAltar;

public class HardcoreEnderExpansion {
    public HardcoreEnderExpansion() {
        MineTweakerAPI.registerClass(EssenceAltar.class);
    }
}
