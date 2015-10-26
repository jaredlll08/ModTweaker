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
import modtweaker2.mods.forestry.ForestryListAddition;
import modtweaker2.mods.forestry.ForestryListRemoval;
import modtweaker2.mods.forestry.recipes.StillRecipe;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import forestry.api.recipes.IStillManager;
import forestry.api.recipes.IStillRecipe;
import forestry.api.recipes.RecipeManagers;

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
		
		MineTweakerAPI.apply(new Add(new StillRecipe(timePerUnit, toFluid(fluidInput), toFluid(fluidOutput))));
	}
	
	@Deprecated
	@ZenMethod
	public static void addRecipe(int timePerUnit, ILiquidStack input, ILiquidStack output) {
		output.amount(output.getAmount() / 100);
		input.amount(input.getAmount() / 100);
		
		MineTweakerAPI.apply(new Add(new StillRecipe(timePerUnit, toFluid(input), toFluid(output))));
	}
	
	private static class Add extends ForestryListAddition<IStillRecipe, IStillManager> {
		public Add(IStillRecipe recipe) {
			super("Forestry Still", RecipeManagers.stillManager);
			recipes.add(recipe);
		}
		
		@Override
		public String getRecipeInfo(IStillRecipe recipe) {
			return LogHelper.getStackDescription(recipe.getOutput());
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
		List<IStillRecipe> recipes = new LinkedList<IStillRecipe>();
		
		for (IStillRecipe r : RecipeManagers.stillManager.recipes()) {
			if (r != null && r.getOutput() != null && matches(output, toILiquidStack(r.getOutput()))) {
				if (input != null) {
					if (matches(input, toILiquidStack(r.getInput()))) {
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
	
	private static class Remove extends ForestryListRemoval<IStillRecipe, IStillManager> {
		public Remove(List<IStillRecipe> recipes) {
			super(Still.name, RecipeManagers.stillManager, recipes);
		}
		
		@Override
		public String getRecipeInfo(IStillRecipe recipe) {
			return LogHelper.getStackDescription(recipe.getOutput());
		}
	}
}
