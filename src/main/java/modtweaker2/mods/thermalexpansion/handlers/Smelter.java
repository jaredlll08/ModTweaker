package modtweaker2.mods.thermalexpansion.handlers;

import static modtweaker2.helpers.InputHelper.toIItemStack;
import static modtweaker2.helpers.InputHelper.toStack;
import static modtweaker2.helpers.StackHelper.matches;

import java.util.LinkedList;
import java.util.List;

import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import modtweaker2.helpers.InputHelper;
import modtweaker2.helpers.LogHelper;
import modtweaker2.mods.thermalexpansion.ThermalHelper;
import modtweaker2.utils.BaseListAddition;
import modtweaker2.utils.BaseListRemoval;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import cofh.thermalexpansion.util.crafting.SmelterManager;
import cofh.thermalexpansion.util.crafting.SmelterManager.RecipeSmelter;

@ZenClass("mods.thermalexpansion.Smelter")
public class Smelter {
    
    public static final String name = "Thermal Expansion Smelter";
    
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@ZenMethod
	public static void addRecipe(int energy, IItemStack input, IItemStack input2, IItemStack output) {
		addRecipe(energy, input, input2, output, null, 0);
	}

	@ZenMethod
	public static void addRecipe(int energy, IItemStack input, IItemStack input2, IItemStack output, IItemStack output2, int chance) {
        if(input == null || input2 == null || output == null) {
            LogHelper.logError(String.format("Required parameters missing for %s Recipe.", name));
            return;
        }
        
        if(SmelterManager.recipeExists(toStack(input), toStack(input2))) {
            LogHelper.logWarning(String.format("Duplicate %s Recipe found for %s and %s. Command ignored!", name, InputHelper.getStackDescription(toStack(input)), InputHelper.getStackDescription(toStack(input2))));
            return;
        }
        
		MineTweakerAPI.apply(new Add(ThermalHelper.getTERecipe(ThermalHelper.smelterRecipe, toStack(input), toStack(input2), toStack(output), toStack(output2), chance, energy)));
	}

	private static class Add extends BaseListAddition<RecipeSmelter> {
		public Add(RecipeSmelter recipe) {
			super(Smelter.name, null);
			recipes.add(recipe);
		}

		@Override
		public void apply() {
		    for(RecipeSmelter recipe : recipes) {
		        boolean applied = SmelterManager.addRecipe(
		                recipe.getEnergy(),
		                recipe.getPrimaryInput(),
		                recipe.getSecondaryInput(),
		                recipe.getPrimaryOutput(),
		                recipe.getSecondaryOutput(),
		                recipe.getSecondaryOutputChance());
		        
		        if(applied) {
		            successful.add(recipe);
		        }
		    }
		}

		@Override
		public void undo() {
		    for(RecipeSmelter recipe : successful) {
		        SmelterManager.removeRecipe(recipe.getPrimaryInput(), recipe.getSecondaryInput());
		    }
		}

		@Override
		public String getRecipeInfo(RecipeSmelter recipe) {
		    return InputHelper.getStackDescription(recipe.getPrimaryOutput());
		}
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@ZenMethod
	public static void removeRecipe(IIngredient input1, IIngredient input2) {
	    List<RecipeSmelter> recipes = new LinkedList<RecipeSmelter>();
	    
        for(RecipeSmelter recipe : SmelterManager.getRecipeList()) {
            if(recipe != null && matches(input1, toIItemStack(recipe.getPrimaryInput())) && matches(input2, toIItemStack(recipe.getSecondaryInput()))) {
                recipes.add(recipe);
            }
        }
        
        if(!recipes.isEmpty()) {
            MineTweakerAPI.apply(new Remove(recipes));
        } else {
            LogHelper.logWarning(String.format("No %s Recipes found for %s and %s.", name, input1.toString(), input2.toString()));
        }
	}

	private static class Remove extends BaseListRemoval<RecipeSmelter> {

		public Remove(List<RecipeSmelter> recipes) {
			super(Smelter.name, null, recipes);
		}

		@Override
		public void apply() {
		    for(RecipeSmelter recipe : recipes) {
		        boolean removed = SmelterManager.removeRecipe(recipe.getPrimaryInput(), recipe.getSecondaryInput());
		        
		        if(removed) {
		            successful.add(recipe);
		        }
		    }
		}
		
		@Override
		public void undo() {
		    for(RecipeSmelter recipe : recipes) {
		        SmelterManager.addRecipe(
		                recipe.getEnergy(),
		                recipe.getPrimaryInput(),
		                recipe.getSecondaryInput(),
		                recipe.getPrimaryOutput(),
		                recipe.getSecondaryOutput(),
		                recipe.getSecondaryOutputChance());
		    }
		}

        @Override
        public String getRecipeInfo(RecipeSmelter recipe) {
            return InputHelper.getStackDescription(recipe.getPrimaryOutput());
        }
	}
}
