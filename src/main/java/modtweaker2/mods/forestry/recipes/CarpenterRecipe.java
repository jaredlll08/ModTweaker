package modtweaker2.mods.forestry.recipes;

import javax.annotation.Nullable;

import net.minecraft.item.ItemStack;

import net.minecraftforge.fluids.FluidStack;

import forestry.api.recipes.ICarpenterRecipe;
import forestry.api.recipes.IDescriptiveRecipe;

public class CarpenterRecipe implements ICarpenterRecipe {

	private final int packagingTime;
	@Nullable
	private final FluidStack liquid;
	@Nullable
	private final ItemStack box;
	private final IDescriptiveRecipe internal;

	public CarpenterRecipe(int packagingTime, @Nullable FluidStack liquid, @Nullable ItemStack box, IDescriptiveRecipe internal) {
		this.packagingTime = packagingTime;
		this.liquid = liquid;
		this.box = box;
		this.internal = internal;
	}

	public int getPackagingTime() {
		return packagingTime;
	}

	@Override
	@Nullable
	public ItemStack getBox() {
		return box;
	}

	@Override
	@Nullable
	public FluidStack getFluidResource() {
		return liquid;
	}

	@Override
	public IDescriptiveRecipe getCraftingGridRecipe() {
		return internal;
	}
}
