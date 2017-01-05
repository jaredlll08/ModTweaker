package modtweaker.mods.forestry.handlers;

import com.blamejared.mtlib.helpers.LogHelper;
import forestry.api.recipes.*;
import forestry.core.recipes.ShapedRecipeCustom;
import forestry.factory.recipes.CarpenterRecipeManager;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.*;
import minetweaker.api.liquid.ILiquidStack;
import modtweaker.mods.forestry.*;
import modtweaker.mods.forestry.recipes.CarpenterRecipe;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.*;

import java.util.*;

import static com.blamejared.mtlib.helpers.InputHelper.*;
import static com.blamejared.mtlib.helpers.StackHelper.matches;

@ZenClass("mods.forestry.Carpenter")
public class Carpenter {
	
	public static final String name = "Forestry Carpenter";
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Adds shaped recipe to Carpenter
	 *
	 * @param output        recipe product
	 * @param ingredients   required ingredients
	 * @param packagingTime amount of ticks per crafting operation
	 *                      * @param fluidInput      required mB of fluid (optional)
	 *                      * @param box             required box in top slot (optional)
	 */
	@ZenMethod
	public static void addRecipe(IItemStack output, IIngredient[][] ingredients, int packagingTime, @Optional ILiquidStack fluidInput, @Optional IItemStack box) {
		MineTweakerAPI.apply(new Add(new CarpenterRecipe(packagingTime, toFluid(fluidInput), toStack(box), new ShapedRecipeCustom(toStack(output), toShapedObjects(ingredients)))));
	}
	
	private static IItemStack[][] transform(IItemStack[] arr, int N) {
		int M = (arr.length + N - 1) / N;
		IItemStack[][] mat = new IItemStack[M][];
		int start = 0;
		for(int r = 0; r < M; r++) {
			int L = Math.min(N, arr.length - start);
			mat[r] = java.util.Arrays.copyOfRange(arr, start, start + L);
			start += L;
		}
		return mat;
	}
	
	private static class Add extends ForestryListAddition<ICarpenterRecipe> {
		
		public Add(ICarpenterRecipe recipe) {
			super(Carpenter.name, ForestryHelper.carpenter);
			recipes.add(recipe);
		}
		
//		@Override
//		public void apply() {
//			for(ICarpenterRecipe recipe : recipes) {
//				if(recipe != null) {
//					if(RecipeManagers.carpenterManager.addRecipe(recipe)) {
//						successful.add(recipe);
//					} else {
//						LogHelper.logError(String.format("Error adding %s Recipe for %s", name, getRecipeInfo(recipe)));
//					}
//				} else {
//					LogHelper.logError(String.format("Error removing %s Recipe: null object", name));
//				}
//			}
//			successful.forEach(ent -> {
//				if(!CarpenterRecipeManager.getRecipeFluids().contains(ent.getFluidResource().getFluid())) {
//					CarpenterRecipeManager.getRecipeFluids().add(ent.getFluidResource().getFluid());
//				}
//			});
//
//		}
		
		@Override
		protected String getRecipeInfo(ICarpenterRecipe recipe) {
			return LogHelper.getStackDescription(recipe.getCraftingGridRecipe().getRecipeOutput());
		}
	}
	
	/**
	 * Adds shapeless recipe to Carpenter
	 *
	 * @param output           recipe product
	 * @param ingredients      required ingredients
	 * @param packagingTime    amount of ticks per crafting operation
	 ** @param fluidInput      required mB of fluid (optional)
	 ** @param box             required box in top slot (optional)
	 ** @param remainingItems  no idea (optional)
	 */
	//TODO
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Removes recipe from Carpenter
	 *
	 * @param output = recipe result
	 *               * @param fluidInput = required type of fluid (optional)
	 */
	@ZenMethod
	public static void removeRecipe(IIngredient output, @Optional IIngredient fluidInput) {
		List<ICarpenterRecipe> recipes = new LinkedList<ICarpenterRecipe>();
		
		for(ICarpenterRecipe recipe : ForestryHelper.carpenter) {
			if(recipe != null) {
				ItemStack recipeResult = recipe.getCraftingGridRecipe().getRecipeOutput();
				if(recipeResult != null && matches(output, toIItemStack(recipeResult))) {
					if(fluidInput != null) {
						if(matches(fluidInput, toILiquidStack(recipe.getFluidResource())))
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
		public void apply() {
			super.apply();
//			successful.forEach(ent -> {
//				if(CarpenterRecipeManager.getRecipeFluids().contains(ent.getFluidResource().getFluid())) {
//					RecipeManagers.carpenterManager.removeRecipe()
//					CarpenterRecipeManager.getRecipeFluids().remove(ent.getFluidResource().getFluid());
//				}
//			});
		}
		
		@Override
		protected String getRecipeInfo(ICarpenterRecipe recipe) {
			return LogHelper.getStackDescription(recipe.getCraftingGridRecipe().getRecipeOutput());
		}
	}
}
