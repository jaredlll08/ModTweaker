package modtweaker2.mods.forestry.handlers;

import static modtweaker2.helpers.InputHelper.toFluid;
import static modtweaker2.helpers.InputHelper.toIItemStack;
import static modtweaker2.helpers.InputHelper.toILiquidStack;
import static modtweaker2.helpers.InputHelper.toStack;
import static modtweaker2.helpers.InputHelper.toStacks;
import static modtweaker2.helpers.InputHelper.toShapedObjects;
import static modtweaker2.helpers.StackHelper.matches;

import java.util.LinkedList;
import java.util.List;

import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import minetweaker.api.liquid.ILiquidStack;
import modtweaker2.helpers.LogHelper;
import modtweaker2.mods.forestry.ForestryListAddition;
import modtweaker2.mods.forestry.ForestryListRemoval;
import modtweaker2.mods.forestry.recipes.CarpenterRecipe;
import net.minecraft.item.ItemStack;

import modtweaker2.mods.forestry.recipes.DescriptiveRecipe;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import forestry.api.recipes.ICarpenterManager;
import forestry.api.recipes.ICarpenterRecipe;
import forestry.api.recipes.IDescriptiveRecipe;
import forestry.api.recipes.RecipeManagers;

@ZenClass("mods.forestry.Carpenter")
public class Carpenter {
	
	public static final String name = "Forestry Carpenter";

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Adds a shaped recipe for the Carpenter
	 *
	 * @param output recipe output
	 * @param ingredients recipe ingredients
	 * @param packagingTime time per crafting operation
	 * @optionalParam box recipes casting item (optional)
	 */
	@ZenMethod
	public static void addRecipe(IItemStack output, IIngredient[][] ingredients, int packagingTime, @Optional IItemStack box) {
		IDescriptiveRecipe craftRecipe = new DescriptiveRecipe(3, 3, toShapedObjects(ingredients), toStack(output), false);
		MineTweakerAPI.apply(new Add(new CarpenterRecipe(packagingTime, null, toStack(box), craftRecipe)));
	}

	/**
	 * Adds a shaped recipe for the Carpenter
	 * 
	 * @param output recipe output
	 * @param ingredients recipe ingredients
	 * @param fluidInput recipe fluid amount
	 * @param packagingTime time per crafting operation
	 * @optionalParam box recipes casting item (optional)
	 */
	@ZenMethod
	public static void addRecipe(IItemStack output, IIngredient[][] ingredients, ILiquidStack fluidInput, int packagingTime, @Optional IItemStack box) {
		IDescriptiveRecipe craftRecipe = new DescriptiveRecipe(3, 3, toShapedObjects(ingredients), toStack(output), false);
		MineTweakerAPI.apply(new Add(new CarpenterRecipe(packagingTime, toFluid(fluidInput), toStack(box), craftRecipe)));
	}

	@Deprecated
	@ZenMethod
	public static void addRecipe(int packagingTime, ILiquidStack fluidInput, IItemStack[] ingredients, IItemStack box, IItemStack output) {
		IDescriptiveRecipe craftRecipe = new DescriptiveRecipe(3, 3, toShapedObjects(transform(ingredients, 3)), toStack(output), false);
		MineTweakerAPI.apply(new Add(new CarpenterRecipe(packagingTime, toFluid(fluidInput), toStack(box), craftRecipe)));
	}

	private static IItemStack[][] transform(IItemStack[] arr, int N) {
	    int M = (arr.length + N - 1) / N;
	    IItemStack[][] mat = new IItemStack[M][];
	    int start = 0;
	    for (int r = 0; r < M; r++) {
	        int L = Math.min(N, arr.length - start);
	        mat[r] = java.util.Arrays.copyOfRange(arr, start, start + L);
	        start += L;
	    }
	    return mat;
	}

	private static class Add extends ForestryListAddition<ICarpenterRecipe, ICarpenterManager> {

		public Add(ICarpenterRecipe recipe) {
			super(Carpenter.name, RecipeManagers.carpenterManager);
			recipes.add(recipe);
		}
		
		@Override
		protected String getRecipeInfo(ICarpenterRecipe recipe) {
			return LogHelper.getStackDescription(recipe.getCraftingGridRecipe().getRecipeOutput());
		}
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Removes a recipe for the Carpenter
	 * 
	 * @param output = item output
	 * @optionalParam liquid = liquid input
	 */
	@ZenMethod
	public static void removeRecipe(IIngredient output, @Optional IIngredient liquid) {
		List<ICarpenterRecipe> recipes = new LinkedList<ICarpenterRecipe>();
		
		for(ICarpenterRecipe recipe : RecipeManagers.carpenterManager.recipes()) {
			if (recipe != null) {
				ItemStack recipeResult = recipe.getCraftingGridRecipe().getRecipeOutput();
				if (recipeResult != null && matches(output, toIItemStack(recipeResult))) {
					if (liquid != null) {
						if (matches(liquid, toILiquidStack(recipe.getFluidResource())))
							recipes.add(recipe);
					} else {
						recipes.add(recipe);
					}
				}
			}
		}
		
		if(!recipes.isEmpty()) {
			MineTweakerAPI.apply(new Remove(recipes));
		} else {
			LogHelper.logWarning(String.format("No %s Recipe found for %s. Command ignored!", Carpenter.name, output.toString()));
		}
	}
	
	private static class Remove extends ForestryListRemoval<ICarpenterRecipe, ICarpenterManager> {
		
		public Remove(List<ICarpenterRecipe> recipes) {
			super(Carpenter.name, RecipeManagers.carpenterManager, recipes);
		}
		
		@Override
		protected String getRecipeInfo(ICarpenterRecipe recipe) {
			return LogHelper.getStackDescription(recipe.getCraftingGridRecipe().getRecipeOutput());
		}
	}
}
