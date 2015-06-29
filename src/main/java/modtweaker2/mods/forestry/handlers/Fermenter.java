package modtweaker2.mods.forestry.handlers;

import static modtweaker2.helpers.InputHelper.toFluid;
import static modtweaker2.helpers.InputHelper.toIItemStack;
import static modtweaker2.helpers.InputHelper.toILiquidStack;
import static modtweaker2.helpers.InputHelper.toStack;
import static modtweaker2.helpers.StackHelper.matches;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import minetweaker.api.liquid.ILiquidStack;
import modtweaker2.helpers.InputHelper;
import modtweaker2.helpers.LogHelper;
import modtweaker2.utils.BaseListAddition;
import modtweaker2.utils.BaseListRemoval;
import net.minecraftforge.fluids.Fluid;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import forestry.factory.gadgets.MachineFermenter.Recipe;
import forestry.factory.gadgets.MachineFermenter.RecipeManager;

@ZenClass("mods.forestry.Fermenter")
public class Fermenter {

    public static final String name = "Forestry Fermenter";
    
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
	@ZenMethod
	public static void addRecipe(IItemStack resource, ILiquidStack liquid, int fermentationValue, float modifier, ILiquidStack output) {
		MineTweakerAPI.apply(new Add(new Recipe(toStack(resource), fermentationValue, modifier, toFluid(output), toFluid(liquid))));
	}

	private static class Add extends BaseListAddition<Recipe> {
		public Add(Recipe recipe) {
			super(Fermenter.name, RecipeManager.recipes);
			recipes.add(recipe);
		}
		
		@Override
		public void apply() {
		    // add liquids to valid input / output
		    for(Recipe recipe : successful) {
		        RecipeManager.recipeFluidInputs.add(recipe.liquid.getFluid());
		        RecipeManager.recipeFluidOutputs.add(recipe.output.getFluid());
		    }
		    
		    super.apply();
		}
		
		@Override
		public void undo() {
		    super.undo();
		    
		    // Tidy up valid inputs
		    for(Iterator<Fluid> iter = RecipeManager.recipeFluidInputs.iterator(); iter.hasNext();) {
		        boolean found = false;
		        Fluid fluid = iter.next();
		        for(Recipe recipe : list) {
		            if(recipe != null && recipe.liquid != null && recipe.liquid.getFluid().equals(fluid)) {
		                found = true;
		            }
		        }
		        
                if(!found) {
                    iter.remove();
                }
		    }
		    
		    // Tidy up valid outputs
            for(Iterator<Fluid> iter = RecipeManager.recipeFluidOutputs.iterator(); iter.hasNext();) {
                boolean found = false;
                Fluid fluid = iter.next();
                for(Recipe recipe : list) {
                    if(recipe != null && recipe.output != null && recipe.output.getFluid().equals(fluid)) {
                        found = true;
                    }
                }
                
                if(!found) {
                    iter.remove();
                }
            }
		}

		@Override
		public String getRecipeInfo(Recipe recipe) {
			return InputHelper.getStackDescription(recipe.output);
		}
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@ZenMethod
	public static void removeRecipe(IIngredient input) {
	    List<Recipe> recipes = new LinkedList<Recipe>();
	    
	    for(Recipe recipe : RecipeManager.recipes) {
	        // check for input items
	        if(recipe != null && recipe.resource != null && matches(input, toIItemStack(recipe.resource))) {
	            recipes.add(recipe);
	        }
	        
	        // check for input liquids
            if(recipe != null && recipe.resource != null && matches(input, toILiquidStack(recipe.liquid))) {
                recipes.add(recipe);
            }	        
	    }
	    
	    if(!recipes.isEmpty()) {
	        MineTweakerAPI.apply(new Remove(recipes));
	    } else {
	        LogHelper.logWarning(String.format("No %s Recipe found for %s. Command ignored!", Fermenter.name, input.toString()));
	    }
	}

	private static class Remove extends BaseListRemoval<Recipe> {

		public Remove(List<Recipe> recipes) {
			super(Fermenter.name, RecipeManager.recipes, recipes);
		}
		
		@Override
		public void apply() {
		    super.apply();
		    
            // Tidy up valid inputs
            for(Iterator<Fluid> iter = RecipeManager.recipeFluidInputs.iterator(); iter.hasNext();) {
                boolean found = false;
                Fluid fluid = iter.next();
                for(Recipe recipe : list) {
                    if(recipe != null && recipe.liquid != null && recipe.liquid.getFluid().equals(fluid)) {
                        found = true;
                    }
                }
                
                if(!found) {
                    iter.remove();
                }
            }
            
            // Tidy up valid outputs
            for(Iterator<Fluid> iter = RecipeManager.recipeFluidOutputs.iterator(); iter.hasNext();) {
                boolean found = false;
                Fluid fluid = iter.next();
                for(Recipe recipe : list) {
                    if(recipe != null && recipe.output != null && recipe.output.getFluid().equals(fluid)) {
                        found = true;
                    }
                }
                
                if(!found) {
                    iter.remove();
                }
            }
		}
		
		@Override
		public void undo() {
            // add liquids to valid input / output
            for(Recipe recipe : successful) {
                RecipeManager.recipeFluidInputs.add(recipe.liquid.getFluid());
                RecipeManager.recipeFluidOutputs.add(recipe.output.getFluid());
            }
            
            super.undo();
		}
		
        @Override
        public String getRecipeInfo(Recipe recipe) {
            return InputHelper.getStackDescription(recipe.output);
        }
	}
}
