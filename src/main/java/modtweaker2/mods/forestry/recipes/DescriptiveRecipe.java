package modtweaker2.mods.forestry.recipes;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import net.minecraftforge.oredict.ShapedOreRecipe;

import forestry.api.recipes.IDescriptiveRecipe;

public class DescriptiveRecipe implements IDescriptiveRecipe {

	private final int width;
	private final int height;
	private final ShapedOreRecipe recipe;
	private final boolean preserveNBT;

	public DescriptiveRecipe(int width, int height, Object[] ingredients, ItemStack output, boolean preserveNBT) {
		this.width = width;
		this.height = height;
		this.recipe = new ShapedOreRecipe(output, ingredients);
		this.preserveNBT = preserveNBT;
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public ItemStack getRecipeOutput() {
		return recipe.getRecipeOutput();
	}

	@Override
	public Object[] getIngredients() {
		return recipe.getInput();
	}

	@Override
	public boolean preserveNBT() {
		return preserveNBT;
	}

	@Override
	public boolean matches(InventoryCrafting inventoryCrafting, World world) {
		return recipe.matches(inventoryCrafting, world);
	}

	@Override
	@Deprecated
	public boolean matches(IInventory inventoryCrafting, World world) {
		return false;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inventoryCrafting) {
		return recipe.getCraftingResult(inventoryCrafting);
	}

	@Override
	public int getRecipeSize() {
		return width * height;
	}
}
