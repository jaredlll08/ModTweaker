package modtweaker2.mods.thermalexpansion.handlers;

import static modtweaker2.helpers.InputHelper.toFluid;
import static modtweaker2.helpers.InputHelper.toIItemStack;
import static modtweaker2.helpers.InputHelper.toStack;
import static modtweaker2.helpers.StackHelper.matches;

import java.util.LinkedList;
import java.util.List;

import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import minetweaker.api.liquid.ILiquidStack;
import modtweaker2.helpers.InputHelper;
import modtweaker2.helpers.LogHelper;
import modtweaker2.mods.thermalexpansion.ThermalHelper;
import modtweaker2.utils.BaseListAddition;
import modtweaker2.utils.BaseListRemoval;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import cofh.thermalexpansion.util.crafting.CrucibleManager;
import cofh.thermalexpansion.util.crafting.CrucibleManager.RecipeCrucible;

@ZenClass("mods.thermalexpansion.Crucible")
public class Crucible {
    
    public static final String name = "Thermal Expansion Crucible";
    
	@ZenMethod
	public static void addRecipe(int energy, IItemStack input, ILiquidStack output) {
        if(input == null || output == null) {
            LogHelper.logError(String.format("Required parameters missing for %s Recipe.", name));
            return;
        }
        
        if(CrucibleManager.recipeExists(toStack(input))) {
            LogHelper.logWarning(String.format("Duplicate %s Recipe found for %s. Command ignored!", name, InputHelper.getStackDescription(toStack(input))));
            return;
	    }
	    
	    MineTweakerAPI.apply(new Add(ThermalHelper.getTERecipe(ThermalHelper.crucibleRecipe, toStack(input), toFluid(output), energy)));
	}

	private static class Add extends BaseListAddition<RecipeCrucible> {

		public Add(RecipeCrucible recipe) {
			super(Crucible.name, null);
			recipes.add(recipe);
		}

		public void apply() {
		    for(RecipeCrucible recipe : recipes) {
		        boolean applied = CrucibleManager.addRecipe(
		                recipe.getEnergy(),
		                recipe.getInput(),
		                recipe.getOutput());
		        
		        if(applied) {
		            successful.add(recipe);
		        }
		    }
		}

		public void undo() {
		    for(RecipeCrucible recipe : successful) {
		        CrucibleManager.removeRecipe(recipe.getInput());
		    }
		}
		
		@Override
		protected String getRecipeInfo(RecipeCrucible recipe) {
		    return InputHelper.getStackDescription(recipe.getInput());
		}
	}

	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@ZenMethod
	public static void removeRecipe(IIngredient input) {
	    List<RecipeCrucible> recipes = new LinkedList<RecipeCrucible>();
	    
	    for(RecipeCrucible recipe : CrucibleManager.getRecipeList()) {
	        if(recipe != null && matches(input, toIItemStack(recipe.getInput()))) {
	            recipes.add(recipe);
	        }
	    }
	    
	    if(!recipes.isEmpty()) {
			MineTweakerAPI.apply(new Remove(recipes));
	    } else {
	        LogHelper.logWarning(String.format("No %s Recipe found for %s.", name, input.toString()));
	    }
	}

	private static class Remove extends BaseListRemoval<RecipeCrucible> {

	    public Remove(List<RecipeCrucible> recipes) {
			super(Crucible.name, null, recipes);
		}

		public void apply() {
		    for(RecipeCrucible recipe : recipes) {
		        boolean removed = CrucibleManager.removeRecipe(recipe.getInput());
		        
		        if(removed) {
		            successful.add(recipe);
		        }
		    }
		}

		public void undo() {
            for(RecipeCrucible recipe : successful) {
                CrucibleManager.addRecipe(
                        recipe.getEnergy(),
                        recipe.getInput(),
                        recipe.getOutput());
            }
		}
		
        @Override
        protected String getRecipeInfo(RecipeCrucible recipe) {
            return InputHelper.getStackDescription(recipe.getInput());
        }
	}
}
