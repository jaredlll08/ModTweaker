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
import modtweaker2.helpers.InputHelper;
import modtweaker2.helpers.LogHelper;
import modtweaker2.helpers.ReflectionHelper;
import modtweaker2.mods.thermalexpansion.ThermalHelper;
import modtweaker2.utils.BaseListAddition;
import modtweaker2.utils.BaseListRemoval;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import cofh.thermalexpansion.util.crafting.PulverizerManager;
import cofh.thermalexpansion.util.crafting.PulverizerManager.RecipePulverizer;

@ZenClass("mods.thermalexpansion.Pulverizer")
public class Pulverizer {
    
    public static final String name = "Thermal Expansion Pulverizer";
    
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
	@ZenMethod
	public static void addRecipe(int energy, IItemStack input, IItemStack output) {
		addRecipe(energy, input, output, null, 0);
	}

	@ZenMethod
	public static void addRecipe(int energy, IItemStack input, IItemStack output, IItemStack secondary, int secondaryChance) {
       if(input == null || output == null) {
            LogHelper.logError(String.format("Required parameters missing for %s Recipe.", name));
            return;
        }
	    
        if(PulverizerManager.recipeExists(toStack(input))) {
            LogHelper.logWarning(String.format("Duplicate %s Recipe found for %s. Command ignored!", name, InputHelper.getStackDescription(toStack(input))));
            return;
        }
        
	    MineTweakerAPI.apply(new Add(ReflectionHelper.getInstance(ThermalHelper.pulverizerRecipe, toStack(input), toStack(output), toStack(secondary), secondaryChance, energy)));
	}

	private static class Add extends BaseListAddition<RecipePulverizer> {
		public Add(RecipePulverizer recipe) {
		    super(Pulverizer.name, null);
		    recipes.add(recipe);
		}

		public void apply() {
		    for(RecipePulverizer recipe : recipes) {
		        boolean applied = PulverizerManager.addRecipe(
		                recipe.getEnergy(),
		                recipe.getInput(),
		                recipe.getPrimaryOutput(),
		                recipe.getSecondaryOutput(),
		                recipe.getSecondaryOutputChance());
		        
		        if(applied) {
		            successful.add(recipe);
		        }
		    }
		}

		public void undo() {
		    for(RecipePulverizer recipe : successful) {
		        PulverizerManager.removeRecipe(recipe.getInput());
		    }
		}
		
		@Override
		protected boolean equals(RecipePulverizer recipe, RecipePulverizer otherRecipe) {
		    return ThermalHelper.equals(recipe, otherRecipe);
		}

		@Override
		protected String getRecipeInfo(RecipePulverizer recipe) {
		    return InputHelper.getStackDescription(recipe.getInput());
		}
	}

	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@ZenMethod
	public static void removeRecipe(IIngredient input) {
	    List<RecipePulverizer> recipes = new LinkedList<RecipePulverizer>();
	    RecipePulverizer[] list = PulverizerManager.getRecipeList();
	    
	    for(RecipePulverizer recipe : list) {
	        if(recipe != null && recipe.getInput() != null && matches(input, toIItemStack(recipe.getInput()))) {
	            recipes.add(recipe);
	        }
	    }
	    
	    if(!recipes.isEmpty()) {
	        MineTweakerAPI.apply(new Remove(recipes));
	    } else {
	        LogHelper.logWarning(String.format("No %s Recipe found for %s.", name, input.toString()));
	    }
	    
	}

	private static class Remove extends BaseListRemoval<RecipePulverizer> {
		public Remove(List<RecipePulverizer> recipes) {
			super(Pulverizer.name, null, recipes);
		}

		public void apply() {
		    for(RecipePulverizer recipe : recipes) {
		        boolean removed = PulverizerManager.removeRecipe(recipe.getInput());
		        
		        if(removed) {
		            successful.add(recipe);
		        }
		    }
		}

		public void undo() {
		    for(RecipePulverizer recipe : successful) {
		        PulverizerManager.addRecipe(
		                recipe.getEnergy(),
		                recipe.getInput(),
		                recipe.getPrimaryOutput(),
		                recipe.getSecondaryOutput(),
		                recipe.getSecondaryOutputChance());
		    }
		}
		
        @Override
        protected boolean equals(RecipePulverizer recipe, RecipePulverizer otherRecipe) {
            return ThermalHelper.equals(recipe, otherRecipe);
        }

        @Override
        protected String getRecipeInfo(RecipePulverizer recipe) {
            return InputHelper.getStackDescription(recipe.getInput());
        }
	}
	
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////   

    @ZenMethod
    public static void refreshRecipes() {
        MineTweakerAPI.apply(new Refresh());
    }

    private static class Refresh implements IUndoableAction {

        public void apply() {
            PulverizerManager.refreshRecipes();
        }

        public boolean canUndo() {
            return true;
        }

        public String describe() {
            return "Refreshing " + Pulverizer.name + " recipes";
        }

        public void undo() {
        }

        public String describeUndo() {
            return "Ignoring undo of " + Pulverizer.name + " recipe refresh";
        }

        public Object getOverrideKey() {
            return null;
        }
    }
}
