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
import stanhebben.zenscript.annotations.Optional;
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
	public static void addRecipe(int energy, IItemStack input, IItemStack input2, IItemStack output, @Optional IItemStack output2, @Optional int chance) {
        if(input == null || input2 == null || output == null) {
            LogHelper.logError(String.format("Required parameters missing for %s Recipe.", name));
            return;
        }
        
        if(SmelterManager.recipeExists(toStack(input), toStack(input2))) {
            LogHelper.logWarning(String.format("Duplicate %s Recipe found for %s and %s. Command ignored!", name, LogHelper.getStackDescription(toStack(input)), LogHelper.getStackDescription(toStack(input2))));
            return;
        }
        
        RecipeSmelter recipe = ReflectionHelper.getInstance(ThermalHelper.smelterRecipe, toStack(input), toStack(input2), toStack(output), toStack(output2), chance, energy);
        
        if(recipe != null) {
            MineTweakerAPI.apply(new Add(recipe));
        } else {
            LogHelper.logError(String.format("Error while creating instance for %s recipe.", name));
        }
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
		protected boolean equals(RecipeSmelter recipe, RecipeSmelter otherRecipe) {
		    return ThermalHelper.equals(recipe, otherRecipe);
		}

		@Override
		public String getRecipeInfo(RecipeSmelter recipe) {
		    return LogHelper.getStackDescription(recipe.getPrimaryOutput());
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
        protected boolean equals(RecipeSmelter recipe, RecipeSmelter otherRecipe) {
            return ThermalHelper.equals(recipe, otherRecipe);
        }

        @Override
        public String getRecipeInfo(RecipeSmelter recipe) {
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
            SmelterManager.refreshRecipes();
        }

        public boolean canUndo() {
            return true;
        }

        public String describe() {
            return "Refreshing " + Smelter.name + " recipes";
        }

        public void undo() {
        }

        public String describeUndo() {
            return "Ignoring undo of " + Smelter.name + " recipe refresh";
        }

        public Object getOverrideKey() {
            return null;
        }
    }
}
