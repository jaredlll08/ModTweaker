package modtweaker2.mods.forestry.handlers;

import static modtweaker2.helpers.InputHelper.toIItemStack;
import static modtweaker2.helpers.InputHelper.toStack;
import static modtweaker2.helpers.StackHelper.matches;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import minetweaker.api.item.WeightedItemStack;
import modtweaker2.helpers.LogHelper;
import modtweaker2.utils.BaseListAddition;
import modtweaker2.utils.BaseListRemoval;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import forestry.api.recipes.ICentrifugeRecipe;
import forestry.factory.gadgets.MachineCentrifuge.CentrifugeRecipe;
import forestry.factory.gadgets.MachineCentrifuge.RecipeManager;


@ZenClass("mods.forestry.Centrifuge")
public class Centrifuge {

    public static final String name = "Forestry Centrifuge";
    
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
	/**
	 * Adds a recipe for the Centrifuge
	 * 
	 * @param output List of items to produce with associated chance
	 * @param ingredient item input
	 * @param timePerItem time per item to process
	 */
	@ZenMethod
	public static void addRecipe(WeightedItemStack[] output, IItemStack ingredient, int timePerItem) {
		Map<ItemStack, Float> products = new HashMap<ItemStack, Float>();
		for (WeightedItemStack product : output) {
			products.put(toStack(product.getStack()), product.getChance());
		}
		MineTweakerAPI.apply(new Add(new CentrifugeRecipe(timePerItem, toStack(ingredient), products)));
	}

	@ZenMethod
	@Deprecated
	public static void addRecipe(int timePerItem, IItemStack itemInput, IItemStack[] output, int[] chances) {
		Map<ItemStack, Float> products = new HashMap<ItemStack, Float>();
		int i = 0;
		for (IItemStack product : output) {
			products.put(toStack(product), ((float) chances[i] / 100));
			i++;
		}
		MineTweakerAPI.apply(new Add(new CentrifugeRecipe(timePerItem, toStack(itemInput), products)));
	}

    private static class Add extends BaseListAddition<ICentrifugeRecipe> {

    	public Add(ICentrifugeRecipe recipe) {
            super(Centrifuge.name, RecipeManager.recipes);
            recipes.add(recipe);
        }
        
        @Override
        protected String getRecipeInfo(ICentrifugeRecipe recipe) {
            return LogHelper.getStackDescription(recipe.getInput());
        }
    }
    
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Removes a recipe for the Centrifuge
	 * 
	 * @param ingredient item input
	 */
	@ZenMethod
	public static void removeRecipe(IIngredient input) {
        List<ICentrifugeRecipe> recipes = new LinkedList<ICentrifugeRecipe>();
        
        for(ICentrifugeRecipe recipe : RecipeManager.recipes) {
            if(recipe != null && matches(input, toIItemStack(recipe.getInput()))) {
                recipes.add(recipe);
            }
        }
        
        if(!recipes.isEmpty()) {
            MineTweakerAPI.apply(new Remove(recipes));            
        } else {
            LogHelper.logWarning(String.format("No %s Recipe found for %s. Command ignored!", Centrifuge.name, input.toString()));
        }
	}

	private static class Remove extends BaseListRemoval<ICentrifugeRecipe> {

		public Remove(List<ICentrifugeRecipe> recipes) {
			super(Centrifuge.name, RecipeManager.recipes, recipes);
		}

		@Override
		protected String getRecipeInfo(ICentrifugeRecipe recipe) {
		    return LogHelper.getStackDescription(recipe.getInput());
		}
	}
}
