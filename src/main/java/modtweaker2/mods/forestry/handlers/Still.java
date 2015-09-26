package modtweaker2.mods.forestry.handlers;

import static modtweaker2.helpers.InputHelper.getFluid;
import static modtweaker2.helpers.InputHelper.toFluid;
import static modtweaker2.helpers.InputHelper.toILiquidStack;
import static modtweaker2.helpers.StackHelper.matches;

import java.util.LinkedList;
import java.util.List;

import minetweaker.MineTweakerAPI;
import minetweaker.api.liquid.ILiquidStack;
import modtweaker2.helpers.LogHelper;
import modtweaker2.utils.BaseListAddition;
import modtweaker2.utils.BaseListRemoval;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import forestry.factory.gadgets.MachineStill;
import forestry.factory.gadgets.MachineStill.Recipe;
import forestry.factory.gadgets.MachineStill.RecipeManager;

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
		MachineStill.RecipeManager.recipeFluidInputs.add(getFluid(fluidInput));
		MachineStill.RecipeManager.recipeFluidOutputs.add(getFluid(fluidOutput));
	}
    
	@Deprecated
	@ZenMethod
	public static void addRecipe(int timePerUnit, ILiquidStack input, ILiquidStack output) {
		output.amount(output.getAmount() / 100);
		input.amount(input.getAmount() / 100);
		
		MineTweakerAPI.apply(new Add(new Recipe(timePerUnit, toFluid(input), toFluid(output))));
		MachineStill.RecipeManager.recipeFluidInputs.add(getFluid(input));
		MachineStill.RecipeManager.recipeFluidOutputs.add(getFluid(output));
	}

	private static class Add extends BaseListAddition<Recipe> {
		public Add(Recipe recipe) {
			super("Forestry Still", MachineStill.RecipeManager.recipes);
			recipes.add(recipe);
		}

		@Override
		public String getRecipeInfo(Recipe recipe) {
			return LogHelper.getStackDescription(recipe.output);
		}
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@ZenMethod
	public static void removeRecipe(ILiquidStack output, @Optional ILiquidStack input) {
	    List<Recipe> recipes = new LinkedList<Recipe>();
	    
        for (Recipe r : RecipeManager.recipes) {
            if (r != null && r.output != null && matches(output, toILiquidStack(r.output))) {
                recipes.add(r);
            }
        }
        
        if(!recipes.isEmpty()) {
            MineTweakerAPI.apply(new Remove(recipes));
        } else {
            LogHelper.logWarning(String.format("No %s Recipe found for %s. Command ignored!", Still.name, output.toString()));
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
	}
}
