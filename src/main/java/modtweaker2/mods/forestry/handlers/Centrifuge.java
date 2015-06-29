package modtweaker2.mods.forestry.handlers;

import static modtweaker2.helpers.InputHelper.toIItemStack;
import static modtweaker2.helpers.InputHelper.toStack;
import static modtweaker2.helpers.StackHelper.matches;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import modtweaker2.helpers.InputHelper;
import modtweaker2.helpers.LogHelper;
import modtweaker2.utils.BaseListAddition;
import modtweaker2.utils.BaseListRemoval;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import forestry.factory.gadgets.MachineCentrifuge.Recipe;
import forestry.factory.gadgets.MachineCentrifuge.RecipeManager;


@ZenClass("mods.forestry.Centrifuge")
public class Centrifuge {

    public static final String name = "Forestry Centrifuge";
    
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
	@ZenMethod
	public static void addRecipe(int timePerItem, IItemStack itemInput, IItemStack[] output, int[] chances) {
		HashMap<ItemStack, Integer> products = new HashMap<ItemStack, Integer>();
		// products.put(toStack(output[0]), chances[0]);
		int i = 0;
		for (IItemStack product : output) {
			products.put(toStack(product), chances[i]);
			i++;
		}
		MineTweakerAPI.apply(new Add(new Recipe(timePerItem, toStack(itemInput), products)));
	}

    private static class Add extends BaseListAddition<Recipe> {

        public Add(Recipe recipe) {
            super(Centrifuge.name, RecipeManager.recipes);
            recipes.add(recipe);
        }
        
        @Override
        protected String getRecipeInfo(Recipe recipe) {
            return InputHelper.getStackDescription(recipe.resource);
        }
    }
    
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @ZenMethod
	public static void removeRecipe(IIngredient input) {
        List<Recipe> recipes = new LinkedList<Recipe>();
        
        for(Recipe recipe : RecipeManager.recipes) {
            if(recipe != null && matches(input, toIItemStack(recipe.resource))) {
                recipes.add(recipe);
            }
        }
        
        if(!recipes.isEmpty()) {
            MineTweakerAPI.apply(new Remove(recipes));            
        } else {
            LogHelper.logWarning(String.format("No %s Recipe found for %s. Command ignored!", Centrifuge.name, input.toString()));
        }
	}

	private static class Remove extends BaseListRemoval<Recipe> {

		public Remove(List<Recipe> recipes) {
			super(Centrifuge.name, RecipeManager.recipes, recipes);
		}

		@Override
		protected String getRecipeInfo(Recipe recipe) {
		    return InputHelper.getStackDescription(recipe.resource);
		}
	}
}
