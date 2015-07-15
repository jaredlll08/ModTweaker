package modtweaker2.mods.forestry.handlers;

import static modtweaker2.helpers.InputHelper.toIItemStack;
import static modtweaker2.helpers.InputHelper.toStack;
import static modtweaker2.helpers.StackHelper.matches;

import java.util.LinkedList;
import java.util.List;

import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import modtweaker2.helpers.LogHelper;
import modtweaker2.utils.BaseListAddition;
import modtweaker2.utils.BaseListRemoval;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import forestry.factory.gadgets.MachineMoistener;
import forestry.factory.gadgets.MachineMoistener.Recipe;
import forestry.factory.gadgets.MachineMoistener.RecipeManager;

@ZenClass("mods.forestry.Moistener")
public class Moistener {
    
    public static final String name = "Forestry Moistener";
    
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@ZenMethod
	public static void addRecipe(int timePerItem, IItemStack resource, IItemStack product) {
		MineTweakerAPI.apply(new Add(new Recipe(toStack(resource), toStack(product), timePerItem)));

	}

	private static class Add extends BaseListAddition<Recipe> {
		public Add(Recipe recipe) {
			super(Moistener.name, MachineMoistener.RecipeManager.recipes);
			recipes.add(recipe);
		}

		@Override
		public String getRecipeInfo(Recipe recipe) {
			return LogHelper.getStackDescription(recipe.product);
		}
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@ZenMethod
	public static void removeRecipe(IIngredient output) {
	    List<Recipe> recipes = new LinkedList<Recipe>();
	    
        for (Recipe recipe : RecipeManager.recipes) {
            if (recipe != null && recipe.product != null && matches(output, toIItemStack(recipe.product))) {
                recipes.add(recipe);
            }
        }
	    
        if(!recipes.isEmpty()) {
            MineTweakerAPI.apply(new Remove(recipes));
        } else {
            LogHelper.logWarning(String.format("No %s Recipe found for %s. Command ignored!", Moistener.name, output.toString()));
        }
	}

	private static class Remove extends BaseListRemoval<Recipe> {
		public Remove(List<Recipe> recipes) {
			super(Moistener.name, RecipeManager.recipes, recipes);
		}

        @Override
        public String getRecipeInfo(Recipe recipe) {
            return LogHelper.getStackDescription(recipe.product);
        }
	}
}
