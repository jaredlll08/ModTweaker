package modtweaker2.mods.forestry.handlers;

import static modtweaker2.helpers.InputHelper.toFluid;
import static modtweaker2.helpers.InputHelper.toILiquidStack;
import static modtweaker2.helpers.StackHelper.matches;

import java.util.LinkedList;
import java.util.List;

import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.liquid.ILiquidStack;
import modtweaker2.helpers.LogHelper;
import modtweaker2.utils.BaseListAddition;
import modtweaker2.utils.BaseListRemoval;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import forestry.factory.tiles.TileStill;
import forestry.factory.tiles.TileStill.Recipe;
import forestry.factory.tiles.TileStill.RecipeManager;

@ZenClass("mods.forestry.Still")
public class Still {
	
	public static final String name = "Forestry Still";
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Adds a recipe for the Still
	 * 
	 * @param fluidOutput recipe fluid amount
	 * @param fluidInput recipe fluid input
	 * @param timePerUnit time per crafting operation
	 */
	@ZenMethod
	public static void addRecipe(ILiquidStack fluidOutput, ILiquidStack fluidInput, int timePerUnit) {
		fluidOutput.amount(fluidOutput.getAmount() / 100);
		fluidInput.amount(fluidInput.getAmount() / 100);
		
		MineTweakerAPI.apply(new Add(new Recipe(timePerUnit, toFluid(fluidInput), toFluid(fluidOutput))));
	}
	
	@Deprecated
	@ZenMethod
	public static void addRecipe(int timePerUnit, ILiquidStack input, ILiquidStack output) {
		output.amount(output.getAmount() / 100);
		input.amount(input.getAmount() / 100);
		
		MineTweakerAPI.apply(new Add(new Recipe(timePerUnit, toFluid(input), toFluid(output))));
	}
	
	private static class Add extends BaseListAddition<Recipe> {
		public Add(Recipe recipe) {
			super("Forestry Still", TileStill.RecipeManager.recipes);
			recipes.add(recipe);
		}
		
		@Override
		public void apply() {
			super.apply();
			for (Recipe recipe : recipes) {
				RecipeManager.recipeFluidInputs.add(recipe.input.getFluid());
			}
		}
		
		@Override
		public void undo() {
			super.undo();
			for (Recipe recipe : recipes) {
				RecipeManager.recipeFluidInputs.remove(recipe.input.getFluid());
			}
		}
		
		@Override
		public String getRecipeInfo(Recipe recipe) {
			return LogHelper.getStackDescription(recipe.output);
		}
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Removes a recipe for the Still
	 *
	 * @param output = liquid output
	 * @optionalParam liquid = liquid input
	 */
	@ZenMethod
	public static void removeRecipe(IIngredient output, @Optional ILiquidStack input) {
		List<Recipe> recipes = new LinkedList<Recipe>();
		
		for (Recipe r : RecipeManager.recipes) {
			if (r != null && r.output != null && matches(output, toILiquidStack(r.output))) {
				if (input != null) {
					if (matches(input, toILiquidStack(r.input))) {
						recipes.add(r);
					}
				}
				else
					recipes.add(r);
			}
		}
		
		if(!recipes.isEmpty()) {
			MineTweakerAPI.apply(new Remove(recipes));
		} else {
			LogHelper.logWarning(String.format("No %s Recipe found for %s. Command ignored!", Still.name, LogHelper.getStackDescription(output)));
		}
	}
	
	private static class Remove extends BaseListRemoval<Recipe> {
		public Remove(List<Recipe> recipes) {
			super(Still.name, RecipeManager.recipes, recipes);
		}
		
		@Override
		public String getRecipeInfo(Recipe recipe) {
			return LogHelper.getStackDescription(recipe.output);
		}
		
		@Override
		public void apply() {
			super.apply();
			for (Recipe recipe : recipes) {
				RecipeManager.recipeFluidInputs.remove(recipe.input.getFluid());
			}
		}
		
		@Override
		public void undo() {
			super.undo();
			for (Recipe recipe : recipes) {
				RecipeManager.recipeFluidInputs.add(recipe.input.getFluid());
			}
		}
	}
}
