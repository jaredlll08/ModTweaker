package forestry_old.recipes;

import net.minecraft.item.ItemStack;

import net.minecraft.util.NonNullList;
import net.minecraftforge.fluids.FluidStack;

import forestry.api.recipes.ISqueezerRecipe;

import java.util.Collections;

public class SqueezerRecipe implements ISqueezerRecipe {

	private final int processingTime;
	private final NonNullList<ItemStack> resources;
	private final FluidStack fluidOutput;
	private final ItemStack remnants;
	private final float remnantsChance;

	public SqueezerRecipe(int processingTime, ItemStack[] resources, FluidStack fluidOutput, ItemStack remnants, float remnantsChance) {
		this.processingTime = processingTime;
		this.resources = NonNullList.create();
		Collections.addAll(this.resources, resources);

		this.fluidOutput = fluidOutput;
		this.remnants = remnants;
		this.remnantsChance = remnantsChance;
	}

	@Override
	public NonNullList<ItemStack> getResources() {
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
