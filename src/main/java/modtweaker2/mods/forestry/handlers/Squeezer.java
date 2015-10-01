package modtweaker2.mods.forestry.handlers;

import static modtweaker2.helpers.InputHelper.getFluid;
import static modtweaker2.helpers.InputHelper.toFluid;
import static modtweaker2.helpers.InputHelper.toILiquidStack;
import static modtweaker2.helpers.InputHelper.toIItemStack;
import static modtweaker2.helpers.InputHelper.toStack;
import static modtweaker2.helpers.InputHelper.toStacks;
import static modtweaker2.helpers.StackHelper.matches;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import minetweaker.api.item.WeightedItemStack;
import minetweaker.api.liquid.ILiquidStack;
import modtweaker2.helpers.LogHelper;
import modtweaker2.utils.BaseListAddition;
import modtweaker2.utils.BaseListRemoval;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import forestry.factory.tiles.TileSqueezer;
import forestry.factory.tiles.TileSqueezer.RecipeManager;
import forestry.factory.recipes.ISqueezerRecipe;
import forestry.factory.recipes.SqueezerContainerRecipe;
import forestry.factory.recipes.SqueezerRecipe;

@ZenClass("mods.forestry.Squeezer")
public class Squeezer {
    
    public static final String name = "Forestry Squeezer";
    
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Adds a recipe with additional item output
	 * 
	 * @param fluidOutput recipe fluid amount
	 * @param itemOutput recipe output
	 * @param ingredients recipe ingredients
	 * @param timePerItem time per crafting operation
	 */
	@ZenMethod
	public static void addRecipe(ILiquidStack fluidOutput, WeightedItemStack itemOutput, IItemStack[] ingredients, int timePerItem) {
		MineTweakerAPI.apply(new Add( new SqueezerRecipe(timePerItem, toStacks(ingredients), toFluid(fluidOutput), toStack(itemOutput.getStack()), (int) itemOutput.getPercent()) ));
		
	}
	
	
	
	private static class Add extends BaseListAddition<ISqueezerRecipe> {
		public Add(ISqueezerRecipe recipe) {
			super(Squeezer.name, TileSqueezer.RecipeManager.recipes);
			recipes.add(recipe);
		}

		@Override
		public String getRecipeInfo(ISqueezerRecipe recipe) {
			return LogHelper.getStackDescription(recipe.getFluidOutput());
		}
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Removes a recipe for the Centrifuge
	 * 
	 * @param liquid liquid output
	 * @param ingredients list of ingredients
	 */
	@ZenMethod
	public static void removeRecipe(IIngredient liquid, @Optional IIngredient[] ingredients) {
	    List<ISqueezerRecipe> recipes = new LinkedList<ISqueezerRecipe>();
	 
        for (ISqueezerRecipe r : RecipeManager.recipes) {
            if (r != null && r.getFluidOutput() != null && matches(liquid, toILiquidStack(r.getFluidOutput()))) {
            	// optional check for ingredients
            	if (ingredients != null) {
            		boolean matched = false;
            		for (int i = 0; i < ingredients.length; i++) {
            			if ( matches(ingredients[i], toIItemStack(r.getResources()[i])) )
            				matched = true;
            			else {
            				matched = false;
            				// if one ingredients doesn't match abort all further checks
            				break;
            			}
					}
            		// if some ingredient doesn't match, the last one is false
           			if (matched)
						recipes.add((SqueezerRecipe) r);
            	} else {
                    recipes.add((SqueezerRecipe) r);
            	}
            }
        }
        
        if(!recipes.isEmpty()) {
            MineTweakerAPI.apply(new Remove(recipes));
        } else {
            LogHelper.logWarning(String.format("No %s Recipe found for %s. Command ignored!", Squeezer.name, liquid.toString()));
        }
		    
	}

	private static class Remove extends BaseListRemoval<ISqueezerRecipe> {
		public Remove(List<ISqueezerRecipe> recipes) {
			super(Squeezer.name, TileSqueezer.RecipeManager.recipes, recipes);

		}

		@Override
		public void apply() {

			super.apply();
		}
		
        @Override
        public String getRecipeInfo(ISqueezerRecipe recipe) {
            return LogHelper.getStackDescription(recipe.getFluidOutput());
        }
	}
}
