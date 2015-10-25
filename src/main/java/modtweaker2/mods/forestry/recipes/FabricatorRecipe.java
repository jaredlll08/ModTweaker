package modtweaker2.mods.forestry.recipes;

import javax.annotation.Nullable;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

import net.minecraftforge.fluids.FluidStack;

import forestry.api.recipes.IDescriptiveRecipe;
import forestry.api.recipes.IFabricatorRecipe;

public class FabricatorRecipe implements IFabricatorRecipe {

	private final ItemStack plan;
	private final FluidStack molten;
	private final IDescriptiveRecipe internal;

	public FabricatorRecipe(ItemStack plan, FluidStack molten, ItemStack result, boolean preservesNbt, Object[] ingredients) {
		this(plan, molten, new DescriptiveRecipe(3, 3, ingredients, result, preservesNbt));
	}

	public FabricatorRecipe(ItemStack plan, FluidStack molten, IDescriptiveRecipe internal) {
		this.plan = plan;
		this.molten = molten;
		this.internal = internal;
	}

	@Override
	@Deprecated
	public boolean matches(@Nullable ItemStack plan, ItemStack[][] resources) {
		return false;
	}

	@Override
	public Object[] getIngredients() {
		return internal.getIngredients();
	}

	@Override
	public int getWidth() {
		return internal.getWidth();
	}

	@Override
	public int getHeight() {
		return internal.getHeight();
	}

	@Override
	public boolean preservesNbt() {
		return internal.preserveNBT();
	}

	@Override
	@Nullable
	public ItemStack getPlan() {
		return plan;
	}

	@Override
	public FluidStack getLiquid() {
		return molten;
	}

	@Override
	@Deprecated
	public ItemStack getCraftingResult(IInventory craftingInventory) {
		return getRecipeOutput();
	}

	@Override
	public ItemStack getRecipeOutput() {
		return internal.getRecipeOutput();
	}

}
