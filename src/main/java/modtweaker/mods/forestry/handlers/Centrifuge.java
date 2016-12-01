package modtweaker.mods.forestry.handlers;

import com.blamejared.mtlib.helpers.LogHelper;
import forestry.api.recipes.*;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.*;
import modtweaker.mods.forestry.*;
import modtweaker.mods.forestry.recipes.CentrifugeRecipe;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.*;

import java.util.*;

import static com.blamejared.mtlib.helpers.InputHelper.*;
import static com.blamejared.mtlib.helpers.StackHelper.matches;


@ZenClass("mods.forestry.Centrifuge")
public class Centrifuge {
	
	public static final String name = "Forestry Centrifuge";
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Adds recipe to Centrifuge
	 *
	 * @param output        recipe product
	 * @param ingredients   required ingredients
	 * @param packagingTime amount of ticks per crafting operation
	 */
	@ZenMethod
	public static void addRecipe(WeightedItemStack[] output, IItemStack ingredients, int packagingTime) {
		Map<ItemStack, Float> products = new HashMap<ItemStack, Float>();
		for(WeightedItemStack product : output) {
			products.put(toStack(product.getStack()), product.getChance());
		}
		MineTweakerAPI.apply(new Add(new CentrifugeRecipe(packagingTime, toStack(ingredients), products)));
	}
	
	private static class Add extends ForestryListAddition<ICentrifugeRecipe> {
		
		public Add(ICentrifugeRecipe recipe) {
			super(Centrifuge.name, ForestryHelper.centrifuge);
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
	 * @param input type of item in input
	 */
	@ZenMethod
	public static void removeRecipe(IIngredient input) {
		List<ICentrifugeRecipe> recipes = new LinkedList<ICentrifugeRecipe>();
		
		for(ICentrifugeRecipe recipe : RecipeManagers.centrifugeManager.recipes()) {
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
	
	private static class Remove extends ForestryListRemoval<ICentrifugeRecipe, ICentrifugeManager> {
		
		public Remove(List<ICentrifugeRecipe> recipes) {
			super(Centrifuge.name, RecipeManagers.centrifugeManager, recipes);
		}
		
		@Override
		protected String getRecipeInfo(ICentrifugeRecipe recipe) {
			return LogHelper.getStackDescription(recipe.getInput());
		}
	}
}
