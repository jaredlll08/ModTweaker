package modtweaker2.mods.thaumcraft;

import minetweaker.MineTweakerAPI;
import modtweaker2.mods.thaumcraft.handlers.Arcane;
import modtweaker2.mods.thaumcraft.handlers.Aspects;
import modtweaker2.mods.thaumcraft.handlers.Crucible;
import modtweaker2.mods.thaumcraft.handlers.Infusion;
import modtweaker2.mods.thaumcraft.handlers.Research;
import modtweaker2.mods.thaumcraft.handlers.Warp;
import modtweaker2.mods.thaumcraft.handlers.Loot;

public class Thaumcraft {
	public Thaumcraft() {
		MineTweakerAPI.registerClass(Arcane.class);
		MineTweakerAPI.registerClass(Aspects.class);
		MineTweakerAPI.registerClass(Crucible.class);
		MineTweakerAPI.registerClass(Infusion.class);
		MineTweakerAPI.registerClass(Research.class);
		MineTweakerAPI.registerClass(Warp.class);
		MineTweakerAPI.registerClass(Loot.class);

	}
}
