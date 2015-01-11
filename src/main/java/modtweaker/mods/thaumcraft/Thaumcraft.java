package modtweaker.mods.thaumcraft;

import minetweaker.MineTweakerAPI;
import modtweaker.mods.thaumcraft.handlers.Arcane;
import modtweaker.mods.thaumcraft.handlers.Aspects;
import modtweaker.mods.thaumcraft.handlers.Crucible;
import modtweaker.mods.thaumcraft.handlers.Infusion;
import modtweaker.mods.thaumcraft.handlers.Research;
import modtweaker.mods.thaumcraft.handlers.Warp;

public class Thaumcraft {
    public Thaumcraft() {
        MineTweakerAPI.registerClass(Arcane.class);
        MineTweakerAPI.registerClass(Aspects.class);
        MineTweakerAPI.registerClass(Crucible.class);
        MineTweakerAPI.registerClass(Infusion.class);
        MineTweakerAPI.registerClass(Research.class);
        MineTweakerAPI.registerClass(Warp.class);

    }
}
