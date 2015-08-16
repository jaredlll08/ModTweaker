package modtweaker2.mods.ic2c;

import minetweaker.MineTweakerAPI;
import modtweaker2.mods.ic2c.mods.BlastFurnace;
import modtweaker2.mods.ic2c.mods.BlockCutter;
import modtweaker2.mods.ic2c.mods.Centrifuge;
import modtweaker2.mods.ic2c.mods.Compressor;
import modtweaker2.mods.ic2c.mods.Extractor;
import modtweaker2.mods.ic2c.mods.Macerator;
import modtweaker2.mods.ic2c.mods.MetalFormerCutting;
import modtweaker2.mods.ic2c.mods.MetalFormerExtruding;
import modtweaker2.mods.ic2c.mods.MetalFormerRolling;
import modtweaker2.mods.ic2c.mods.OreWashing;
import modtweaker2.mods.ic2c.mods.Recycler;

public class IC2C {

	public IC2C() {
		MineTweakerAPI.registerClass(BlastFurnace.class);
		MineTweakerAPI.registerClass(BlockCutter.class);
		MineTweakerAPI.registerClass(Centrifuge.class);
		MineTweakerAPI.registerClass(Compressor.class);
		MineTweakerAPI.registerClass(Extractor.class);
		MineTweakerAPI.registerClass(Macerator.class);
		MineTweakerAPI.registerClass(MetalFormerCutting.class);
		MineTweakerAPI.registerClass(MetalFormerExtruding.class);
		MineTweakerAPI.registerClass(MetalFormerRolling.class);
		MineTweakerAPI.registerClass(OreWashing.class);
		MineTweakerAPI.registerClass(Recycler.class);

	}
}
