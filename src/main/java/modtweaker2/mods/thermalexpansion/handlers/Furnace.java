package modtweaker2.mods.thermalexpansion.handlers;

import static modtweaker2.helpers.InputHelper.toIItemStack;
import static modtweaker2.helpers.InputHelper.toStack;
import static modtweaker2.helpers.StackHelper.matches;

import java.util.LinkedList;
import java.util.List;

import minetweaker.IUndoableAction;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import modtweaker2.helpers.LogHelper;
import modtweaker2.helpers.ReflectionHelper;
import modtweaker2.mods.thermalexpansion.ThermalHelper;
import modtweaker2.utils.BaseListAddition;
import modtweaker2.utils.BaseListRemoval;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import cofh.thermalexpansion.util.crafting.FurnaceManager;
import cofh.thermalexpansion.util.crafting.FurnaceManager.RecipeFurnace;

@ZenClass("mods.thermalexpansion.Furnace")
public class Furnace {
    
    public static final String name = "Thermal Expansion Furnace";
    
//  ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
	@ZenMethod
	public static void addRecipe(int energy, IItemStack input, IItemStack output) {
	    if(input == null || output == null) {
	        LogHelper.logError(String.format("Required parameters missing for %s Recipe.", name));
	        return;
	    }
	    
        if(FurnaceManager.recipeExists(toStack(input))) {
            LogHelper.logWarning(String.format("Duplicate %s Recipe found for %s. Command ignored!", name, LogHelper.getStackDescription(toStack(input))));
            return;
        }
        
        RecipeFurnace recipe = ReflectionHelper.getInstance(ThermalHelper.furanceRecipe, toStack(input), toStack(output), energy);
        
        if(recipe != null) {
            MineTweakerAPI.apply(new Add(recipe));
        } else {
            LogHelper.logError(String.format("Error while creating instance for %s recipe.", name));
        }
	}

	private static class Add extends BaseListAddition<RecipeFurnace> {
		public Add(RecipeFurnace recipe) {
		    super(Furnace.name, null);
		    recipes.add(recipe);
		}

		public void apply() {
		    for(RecipeFurnace recipe : recipes) {
	            boolean applied = FurnaceManager.addRecipe(
	                    recipe.getEnergy(),
	                    recipe.getInput(),
	                    recipe.getOutput(),
	                    false);
	            
	            if(applied) {
	                successful.add(recipe);
	            }
		    }

		}

		public void undo() {
		    for(RecipeFurnace recipe : recipes) {
		        FurnaceManager.removeRecipe(recipe.getInput());    
		    }
			
		}
		
		@Override
		protected boolean equals(RecipeFurnace recipe, RecipeFurnace otherRecipe) {
		    return ThermalHelper.equals(recipe, otherRecipe);
		}

		@Override
		protected String getRecipeInfo(RecipeFurnace recipe) {
		    return LogHelper.getStackDescription(recipe.getOutput());
		}
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@ZenMethod
	public static void removeRecipe(IIngredient input) {
		List<RecipeFurnace> recipes = new LinkedList<RecipeFurnace>();
		
		for(RecipeFurnace recipe : FurnaceManager.getRecipeList()) {
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

	private static class Remove extends BaseListRemoval<RecipeFurnace> {
		public Remove(List<RecipeFurnace> recipes) {
			super(Furnace.name, null, recipes);
		}

		public void apply() {
		    for(RecipeFurnace recipe : recipes) {
		        boolean removed = FurnaceManager.removeRecipe(recipe.getInput());
		        
		        if(removed) {
		            successful.add(recipe);
		        }
		    }
		}

		public void undo() {
		    for(RecipeFurnace recipe : successful) {
		        FurnaceManager.addRecipe(
                        recipe.getEnergy(),
                        recipe.getInput(),
                        recipe.getOutput(),
                        false);
		    }
		}
		
        @Override
        protected boolean equals(RecipeFurnace recipe, RecipeFurnace otherRecipe) {
            return ThermalHelper.equals(recipe, otherRecipe);
        }

        @Override
        protected String getRecipeInfo(RecipeFurnace recipe) {
            return LogHelper.getStackDescription(recipe.getOutput());
        }
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@ZenMethod
	public static void refreshRecipes() {
		MineTweakerAPI.apply(new Refresh());
	}

	private static class Refresh implements IUndoableAction {

		public void apply() {
			FurnaceManager.refreshRecipes();
		}

		public boolean canUndo() {
			return true;
		}

		public String describe() {
			return "Refreshing " + Furnace.name + " recipes";
		}

		public void undo() {
		}

		public String describeUndo() {
			return "Ignoring undo of " + Furnace.name + " recipe refresh";
		}

		public Object getOverrideKey() {
			return null;
		}
	}
}
