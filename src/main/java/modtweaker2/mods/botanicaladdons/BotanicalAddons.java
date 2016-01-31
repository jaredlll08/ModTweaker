package modtweaker2.mods.botanicaladdons;

import minetweaker.MineTweakerAPI;
import modtweaker2.mods.botanicaladdons.handlers.DendricSuffuser;
import modtweaker2.mods.botanicaladdons.handlers.IridescentTree;

public class BotanicalAddons {
    public BotanicalAddons() {
        MineTweakerAPI.registerClass(DendricSuffuser.class);
        MineTweakerAPI.registerClass(IridescentTree.class);
    }
}
