package modtweaker2.mods.forestry.recipes;

import net.minecraft.item.ItemStack;

import net.minecraftforge.fluids.FluidStack;

import forestry.api.recipes.ISqueezerRecipe;

public class SqueezerRecipe implements ISqueezerRecipe {

	private final int processingTime;
	private final ItemStack[] resources;
	private final FluidStack fluidOutput;
	private final ItemStack remnants;
	private final float remnantsChance;

	public SqueezerRecipe(int processingTime, ItemStack[] resources, FluidStack fluidOutput, ItemStack remnants, float remnantsChance) {
		this.processingTime = processingTime;
		this.resources = resources;
		this.fluidOutput = fluidOutput;
		this.remnants = remnants;
		this.remnantsChance = remnantsChance;
	}

	@Override
	public ItemStack[] getResources() {
		return resources;
	}

	@Override
	public ItemStack getRemnants() {
		return remnants;
	}

	@Override
	public float getRemnantsChance() {
		return remnantsChance;
	}

	@Override
	public FluidStack getFluidOutput() {
		return fluidOutput;
	}

	@Override
	public int getProcessingTime() {
		return processingTime;
	}

}
