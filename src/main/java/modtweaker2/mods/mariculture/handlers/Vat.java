package modtweaker2.mods.mariculture.handlers;

import static modtweaker2.helpers.InputHelper.toFluid;
import static modtweaker2.helpers.InputHelper.toIItemStack;
import static modtweaker2.helpers.InputHelper.toILiquidStack;
import static modtweaker2.helpers.InputHelper.toStack;
import static modtweaker2.helpers.StackHelper.matches;

import java.util.LinkedList;
import java.util.List;

import mariculture.api.core.MaricultureHandlers;
import mariculture.api.core.RecipeVat;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import minetweaker.api.liquid.ILiquidStack;
import modtweaker2.helpers.InputHelper;
import modtweaker2.helpers.LogHelper;
import modtweaker2.utils.BaseListAddition;
import modtweaker2.utils.BaseListRemoval;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.mariculture.Vat")
public class Vat {
    
    public static final String name = "Mariculture Vat";
    
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
	// Adding a Mariculture Vat recipe
	@ZenMethod
	public static void addRecipe(ILiquidStack fluid1, ILiquidStack fluid2, ILiquidStack outputFluid, int time) {
		addRecipe(fluid1, fluid2, null, outputFluid, null, time);
	}

	@ZenMethod
	public static void addRecipe(ILiquidStack fluid1, ILiquidStack fluid2, IItemStack outputItem, int time) {
		addRecipe(fluid1, fluid2, null, null, outputItem, time);
	}

	@ZenMethod
	public static void addRecipe(ILiquidStack fluid1, ILiquidStack fluid2, ILiquidStack outputFluid, IItemStack outputItem, int time) {
		addRecipe(fluid1, fluid2, null, outputFluid, outputItem, time);
	}

	@ZenMethod
	public static void addRecipe(ILiquidStack fluid1, IItemStack input, ILiquidStack outputFluid, int time) {
		addRecipe(fluid1, null, input, outputFluid, null, time);
	}

	@ZenMethod
	public static void addRecipe(ILiquidStack fluid1, IItemStack input, IItemStack outputItem, int time) {
		addRecipe(fluid1, null, input, null, outputItem, time);
	}

	@ZenMethod
	public static void addRecipe(ILiquidStack fluid1, IItemStack input, ILiquidStack outputFluid, IItemStack outputItem, int time) {
		addRecipe(fluid1, null, input, outputFluid, outputItem, time);
	}

	@ZenMethod
	public static void addRecipe(ILiquidStack fluid1, ILiquidStack fluid2, IItemStack input, ILiquidStack outputFluid, int time) {
		addRecipe(fluid1, fluid2, input, outputFluid, null, time);
	}

	@ZenMethod
	public static void addRecipe(ILiquidStack fluid1, ILiquidStack fluid2, IItemStack input, IItemStack outputItem, int time) {
		addRecipe(fluid1, fluid2, input, null, outputItem, time);
	}

	@ZenMethod
	public static void addRecipe(ILiquidStack fluid1, ILiquidStack fluid2, IItemStack input, ILiquidStack outputFluid, IItemStack outputItem, int time) {
		MineTweakerAPI.apply(new Add(new RecipeVat(toStack(input), toFluid(fluid1), toFluid(fluid2), toFluid(outputFluid), toStack(outputItem), time)));
	}

	// Passes the list to the base list implementation, and adds the recipe
	private static class Add extends BaseListAddition<RecipeVat> {
		public Add(RecipeVat recipe) {
			super("Mariculture Vat", MaricultureHandlers.vat.getRecipes());
			recipes.add(recipe);
		}

		@Override
		public String getRecipeInfo(RecipeVat recipe) {
		    if(recipe.outputItem != null) {
		        return InputHelper.getStackDescription(recipe.outputItem);
		    } else {
		        return InputHelper.getStackDescription(recipe.outputFluid);
		    }
		}
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	// Removing a Mariculture Vat recipe
	@ZenMethod
	public static void removeRecipe(IIngredient ingredient) {
	    List<RecipeVat> recipes = new LinkedList<RecipeVat>();
	    
	    for(RecipeVat r : MaricultureHandlers.vat.getRecipes()) {
	        if(r != null) {
	            if(r.outputItem != null && matches(ingredient, toIItemStack(r.outputItem))) {
	                recipes.add(r);
	            }
	            else if(r.outputFluid != null && matches(ingredient, toILiquidStack(r.outputFluid))) {
                    recipes.add(r);
                }
	        }
	    }
	    
	    if(!recipes.isEmpty()) {
	        MineTweakerAPI.apply(new Remove(recipes));
	    } else {
	        LogHelper.logWarning(String.format("No %s Recipes found for %s. Command ignored!", Vat.name, ingredient.toString()));
	    }
	}

	@ZenMethod
	public static void removeRecipe(IIngredient outputItem, IIngredient outputFluid) {
	    List<RecipeVat> recipes = new LinkedList<RecipeVat>();
	    
        for (RecipeVat r : MaricultureHandlers.vat.getRecipes()) {
            if (r != null) {
                if (r.outputItem != null && matches(outputItem, toIItemStack(r.outputItem))) {
                    if (r.outputFluid != null && matches(outputFluid, toILiquidStack(r.outputFluid))) {
                        recipes.add(r);
                    }
                }
            }
        }
	
        if(!recipes.isEmpty()) {
            MineTweakerAPI.apply(new Remove(recipes));
        } else {
            LogHelper.logWarning(String.format("No %s Recipes found for %s and %s. Command ignored!", Vat.name, outputItem.toString(), outputFluid.toString()));
        }
	}

	// Removes a recipe, apply is never the same for anything, so will always
	// need to override it
	private static class Remove extends BaseListRemoval<RecipeVat> {
		public Remove(List<RecipeVat> recipes) {
			super("Mariculture Vat", MaricultureHandlers.vat.getRecipes(), recipes);
		}

        @Override
        public String getRecipeInfo(RecipeVat recipe) {
            if(recipe.outputItem != null) {
                return InputHelper.getStackDescription(recipe.outputItem);
            } else {
                return InputHelper.getStackDescription(recipe.outputFluid);
            }
        }
	}
}
