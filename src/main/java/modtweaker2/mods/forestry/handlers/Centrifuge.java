package modtweaker2.mods.forestry.handlers;

import static modtweaker2.helpers.InputHelper.toStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import minetweaker.api.item.WeightedItemStack;
import minetweaker.mc1710.item.MCItemStack;
import modtweaker2.utils.BaseListAddition;
import modtweaker2.utils.BaseListRemoval;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import forestry.api.recipes.ICentrifugeRecipe;
import forestry.factory.gadgets.MachineCentrifuge.CentrifugeRecipe;
import forestry.factory.gadgets.MachineCentrifuge.RecipeManager;


@ZenClass("mods.forestry.Centrifuge")
public class Centrifuge {

	/**
	 * Adds a recipe for the Centrifuge
	 * 
	 * @param output List of items to produce with associated chance
	 * @param timePerItem time per item to process
	 * @param ingredient item input
	 */
	@ZenMethod
	public static void addRecipe(WeightedItemStack[] output, int timePerItem, IItemStack ingredient) {
		Map<ItemStack, Float> products = new HashMap<ItemStack, Float>();
		for (WeightedItemStack product : output) {
			products.put(toStack(product.getStack()), product.getChance());
		}
		MineTweakerAPI.apply(new Add(new CentrifugeRecipe(timePerItem, toStack(ingredient), products)));
	}

	/**
	 * Adds a recipe for the Centrifuge
	 * 
	 * @param output List of items to produce
	 * @param output List of percentages to produce a item (same order as in item list) 
	 * @param timePerItem time per item to process
	 * @param itemInput item input
	 */
	@ZenMethod
	@Deprecated
	public static void addRecipe(int timePerItem, IItemStack itemInput, IItemStack[] output, int[] chances) {
		Map<ItemStack, Float> products = new HashMap<ItemStack, Float>();
		int i = 0;
		for (IItemStack product : output) {
			products.put(toStack(product), ((float) chances[i] / 100));
			i++;
		}
		MineTweakerAPI.apply(new Add(new CentrifugeRecipe(timePerItem, toStack(itemInput), products)));
	}

	/**
	 * Removes a recipe for the Centrifuge
	 * 
	 * @param ingredient item input
	 */
	@ZenMethod
	public static void removeRecipe(IItemStack ingredient) {
		MineTweakerAPI.apply(new Remove(RecipeManager.recipes, toStack(ingredient)));
	}

	/*
	Implements the actions to add the recipe
	*/
	private static class Add extends BaseListAddition {

		public Add(ICentrifugeRecipe recipe) {
			super("Forestry Centrifuge", RecipeManager.recipes, recipe);
		}
		@Override
		public String getRecipeInfo() {
			return " " + new MCItemStack(((ICentrifugeRecipe) recipe).getInput()) + " (Input)";
		}
	}

	/*
	Implements the actions to remove the recipe
	*/
	private static class Remove extends BaseListRemoval {

		public Remove(@SuppressWarnings("rawtypes") List list, ItemStack input) {
			super("Forestry Centrifuge", RecipeManager.recipes, input);
		}

		@SuppressWarnings("unchecked")
		@Override
		public void apply() {
			for (ICentrifugeRecipe r : RecipeManager.recipes) {
				if (ItemStack.areItemStacksEqual(r.getInput(), stack)) {
					recipes.add(r);
				}
			}
			super.apply();
		}

		@Override
		public String getRecipeInfo() {
			return " " + new MCItemStack(stack) + " (Input)";
		}
	}
}
