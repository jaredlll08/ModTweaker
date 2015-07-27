package modtweaker2.mods.thaumcraft;

import minetweaker.MineTweakerAPI;
import modtweaker2.mods.thaumcraft.aspect.AspectBracketHandler;
import modtweaker2.mods.thaumcraft.handlers.Arcane;
import modtweaker2.mods.thaumcraft.handlers.Aspects;
import modtweaker2.mods.thaumcraft.handlers.Crucible;
import modtweaker2.mods.thaumcraft.handlers.Infusion;
import modtweaker2.mods.thaumcraft.handlers.Loot;
import modtweaker2.mods.thaumcraft.handlers.Research;
import modtweaker2.mods.thaumcraft.handlers.Test;
import modtweaker2.mods.thaumcraft.handlers.Warp;

public class Thaumcraft {
	public Thaumcraft() {
	    MineTweakerAPI.registerBracketHandler(new AspectBracketHandler());
		MineTweakerAPI.registerClass(Arcane.class);
		MineTweakerAPI.registerClass(Aspects.class);
		MineTweakerAPI.registerClass(Crucible.class);
		MineTweakerAPI.registerClass(Infusion.class);
		MineTweakerAPI.registerClass(Research.class);
		MineTweakerAPI.registerClass(Warp.class);
		MineTweakerAPI.registerClass(Loot.class);
	}
}
