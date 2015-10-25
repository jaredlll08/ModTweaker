package modtweaker2.mods.forestry.recipes;

import net.minecraft.item.ItemStack;

import net.minecraftforge.fluids.FluidStack;

import forestry.api.recipes.IFabricatorSmeltingRecipe;

public class FabricatorSmeltingRecipe implements IFabricatorSmeltingRecipe {
	private final ItemStack resource;
	private final FluidStack product;
	private final int meltingPoint;

	public FabricatorSmeltingRecipe(ItemStack resource, FluidStack molten, int meltingPoint) {
		if (resource == null) {
			throw new IllegalArgumentException("Resource cannot be null");
		}
		if (molten == null) {
			throw new IllegalArgumentException("Molten cannot be null");
		}

		this.resource = resource;
		this.product = molten;
		this.meltingPoint = meltingPoint;
	}

	public ItemStack getResource() {
		return resource;
	}

	public FluidStack getProduct() {
		return product;
	}

	public int getMeltingPoint() {
		return meltingPoint;
	}
}
