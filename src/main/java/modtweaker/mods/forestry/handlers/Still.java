package modtweaker.mods.forestry.handlers;

import com.blamejared.mtlib.helpers.LogHelper;
import forestry.api.recipes.*;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.liquid.ILiquidStack;
import modtweaker.mods.forestry.*;
import modtweaker.mods.forestry.recipes.StillRecipe;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.*;

import java.util.*;

import static com.blamejared.mtlib.helpers.InputHelper.*;
import static com.blamejared.mtlib.helpers.StackHelper.matches;

@ZenClass("mods.forestry.Still")
public class Still {
	
	public static final String name = "Forestry Still";
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Adds recipe to Still
	 *
	 * @param fluidOutput recipe fluid amount
	 * @param fluidInput  recipe fluid input
	 * @param timePerUnit time per crafting operation
	 */
	@ZenMethod
	public static void addRecipe(ILiquidStack fluidOutput, ILiquidStack fluidInput, int timePerUnit) {
		fluidOutput.amount(fluidOutput.getAmount() / 100);
		fluidInput.amount(fluidInput.getAmount() / 100);
		
		MineTweakerAPI.apply(new Add(new StillRecipe(timePerUnit, toFluid(fluidInput), toFluid(fluidOutput))));
	}
	
	private static class Add extends ForestryListAddition<IStillRecipe> {
		
		public Add(IStillRecipe recipe) {
			super("Forestry Still", ForestryHelper.still);
			recipes.add(recipe);
		}
		
		@Override
		public String getRecipeInfo(IStillRecipe recipe) {
			return LogHelper.getStackDescription(recipe.getOutput());
		}
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Removes recipe from Still
	 *
	 * @param output = liquid output
	 *               * @param fluidInput = liquid input (optional)
	 */
	@ZenMethod
	public static void removeRecipe(IIngredient output, @Optional ILiquidStack fluidInput) {
		List<IStillRecipe> recipes = new LinkedList<IStillRecipe>();
		
		for(IStillRecipe r : RecipeManagers.stillManager.recipes()) {
			if(r != null && r.getOutput() != null && matches(output, toILiquidStack(r.getOutput()))) {
				if(fluidInput != null) {
					if(matches(fluidInput, toILiquidStack(r.getInput()))) {
						recipes.add(r);
					}
				} else
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
