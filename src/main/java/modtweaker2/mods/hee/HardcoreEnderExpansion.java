package modtweaker2.mods.hee;

import minetweaker.MineTweakerAPI;
import modtweaker2.mods.hee.handlers.EssenceAltar;

public class HardcoreEnderExpansion {
    public HardcoreEnderExpansion() {
        MineTweakerAPI.registerClass(EssenceAltar.class);
    }
}
