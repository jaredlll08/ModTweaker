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
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import cofh.thermalexpansion.util.crafting.InsolatorManager;
import cofh.thermalexpansion.util.crafting.InsolatorManager.RecipeInsolator;

@ZenClass("mods.thermalexpansion.Insolator")
public class Insolator {
	
	public static final String name = "Thermal Expansion Insolator";
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@ZenMethod
	public static void addRecipe(int energy, IItemStack secondaryInput, IItemStack primaryInput, IItemStack primaryOutput, @Optional IItemStack secondaryOutput, @Optional int secondaryChance) {
	    if(primaryInput == null || secondaryInput == null || primaryOutput == null) {
	        LogHelper.logError(String.format("Required parameters missing for %s Recipe.", name));
	        return;
	    }
	    
	    /* TODO: Make it check for meta data
	    if(InsolatorManager.recipeExists(toStack(primaryInput), toStack(secondaryInput))) {
            LogHelper.logWarning(String.format("Duplicate %s Recipe found for %s and %s. Command ignored!", name, primaryInput.toString(), secondaryInput.toString()));
            return;
	    }
	    */
	    
	    RecipeInsolator recipe = ReflectionHelper.getInstance(ThermalHelper.insolatorRecipe, toStack(primaryInput), toStack(secondaryInput), toStack(primaryOutput), toStack(secondaryOutput), secondaryChance, energy);
	    
        if(recipe != null) {
            MineTweakerAPI.apply(new Add(recipe));
        } else {
            LogHelper.logError(String.format("Error while creating instance for %s recipe.", name));
        }
	}
	
	private static class Add extends BaseListAddition<RecipeInsolator> {
		public Add(RecipeInsolator recipe) {
		    super(Insolator.name, null);
		    recipes.add(recipe);
		}

		public void apply() {
		    for(RecipeInsolator recipe : recipes) {
	            boolean applied = InsolatorManager.addRecipe(
	                    recipe.getEnergy(),
	                    recipe.getPrimaryInput(),
	                    recipe.getSecondaryInput(),
	                    recipe.getPrimaryOutput(),
	                    recipe.getSecondaryOutput(),
	                    recipe.getSecondaryOutputChance(),
	                    false);
	            
	            if(applied) {
	                successful.add(recipe);
	            }
		    }

		}

		public void undo() {
		    for(RecipeInsolator recipe : recipes) {
		    	InsolatorManager.removeRecipe(recipe.getPrimaryInput(), recipe.getSecondaryInput());    
		    }
			
		}
		
		@Override
		protected boolean equals(RecipeInsolator recipe, RecipeInsolator otherRecipe) {
		    return ThermalHelper.equals(recipe, otherRecipe);
		}

		@Override
		protected String getRecipeInfo(RecipeInsolator recipe) {
		    return LogHelper.getStackDescription(recipe.getPrimaryOutput());
		}
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@ZenMethod
	public static void removeRecipe(IIngredient input2, IIngredient input1) {
	    List<RecipeInsolator> recipes = new LinkedList<RecipeInsolator>();
	    
        for(RecipeInsolator recipe : InsolatorManager.getRecipeList()) {
            if(recipe != null && matches(input1, toIItemStack(recipe.getPrimaryInput())) && matches(input2, toIItemStack(recipe.getSecondaryInput()))) {
                recipes.add(recipe);
            }
        }
        
        /* TODO: Make it check for meta data
        if(!recipes.isEmpty()) {
            MineTweakerAPI.apply(new Remove(recipes));
        } else {
            LogHelper.logWarning(String.format("No %s Recipes found for %s and %s.", name, input1.toString(), input2.toString()));
        }
        */
	}

	private static class Remove extends BaseListRemoval<RecipeInsolator> {

		public Remove(List<RecipeInsolator> recipes) {
			super(Insolator.name, null, recipes);
		}

		@Override
		public void apply() {
		    for(RecipeInsolator recipe : recipes) {
		        boolean removed = InsolatorManager.removeRecipe(recipe.getPrimaryInput(), recipe.getSecondaryInput());
		        
		        if(removed) {
		            successful.add(recipe);
		        }
		    }
		}
		
		@Override
		public void undo() {
		    for(RecipeInsolator recipe : recipes) {
		    	InsolatorManager.addRecipe(
		                recipe.getEnergy(),
		                recipe.getPrimaryInput(),
		                recipe.getSecondaryInput(),
		                recipe.getPrimaryOutput(),
		                recipe.getSecondaryOutput(),
		                recipe.getSecondaryOutputChance());
		    }
		}
		
        @Override
        protected boolean equals(RecipeInsolator recipe, RecipeInsolator otherRecipe) {
            return ThermalHelper.equals(recipe, otherRecipe);
        }

        @Override
        public String getRecipeInfo(RecipeInsolator recipe) {
            return LogHelper.getStackDescription(recipe.getPrimaryOutput());
        }
	}
	
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////   

    @ZenMethod
    public static void refreshRecipes() {
        MineTweakerAPI.apply(new Refresh());
    }

    private static class Refresh implements IUndoableAction {

        public void apply() {
        	InsolatorManager.refreshRecipes();
        }

        public boolean canUndo() {
            return true;
        }

        public String describe() {
            return "Refreshing " + Insolator.name + " recipes";
        }

        public void undo() {
        }

        public String describeUndo() {
            return "Ignoring undo of " + Insolator.name + " recipe refresh";
        }

        public Object getOverrideKey() {
            return null;
        }
    }

}
