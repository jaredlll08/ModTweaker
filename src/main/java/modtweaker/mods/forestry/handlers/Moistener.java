package modtweaker.mods.forestry.handlers;

import com.blamejared.mtlib.helpers.LogHelper;
import com.blamejared.mtlib.utils.*;
import forestry.api.fuels.*;
import forestry.api.recipes.*;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.*;
import modtweaker.mods.forestry.*;
import modtweaker.mods.forestry.recipes.MoistenerRecipe;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.*;

import java.util.*;
import java.util.Map.Entry;

import static com.blamejared.mtlib.helpers.InputHelper.*;
import static com.blamejared.mtlib.helpers.StackHelper.matches;

@ZenClass("mods.forestry.Moistener")
public class Moistener {

	public static final String name = "Forestry Moistener";
	public static final String nameFuel = name + " (Fuel)";

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Adds recipe to Moistener
	 * 
	 * @param output 	    recipe product
	 * @param input    		required item
	 * @param packagingTime amount of ticks per crafting operation
	 */
	@ZenMethod
	public static void addRecipe(IItemStack output, IItemStack input, int packagingTime) {
		MineTweakerAPI.apply(new Add(new MoistenerRecipe(toStack(input), toStack(output), packagingTime)));
	}

	private static class Add extends ForestryListAddition<IMoistenerRecipe> {
		public Add(IMoistenerRecipe recipe) {
			super(Moistener.name, ForestryHelper.moistener);
			recipes.add(recipe);
		}

		@Override
		public String getRecipeInfo(IMoistenerRecipe recipe) {
			return LogHelper.getStackDescription(recipe.getProduct());
		}
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Removes recipe from Fermenter
	 *
	 * @param output recipe product
	 */
	@ZenMethod
	public static void removeRecipe(IIngredient output) {
		List<IMoistenerRecipe> recipes = new LinkedList<IMoistenerRecipe>();
		for (IMoistenerRecipe recipe : RecipeManagers.moistenerManager.recipes()) {
			if (recipe != null && recipe.getProduct() != null && matches(output, toIItemStack(recipe.getProduct()))) {
				recipes.add(recipe);
			}
		}

		if(!recipes.isEmpty()) {
			MineTweakerAPI.apply(new Remove(recipes));
		} else {
			LogHelper.logWarning(String.format("No %s Recipe found for %s. Command ignored!", Moistener.name, output.toString()));
		}
	}

	private static class Remove extends ForestryListRemoval<IMoistenerRecipe, IMoistenerManager> {
		public Remove(List<IMoistenerRecipe> recipes) {
			super(Moistener.name, RecipeManagers.moistenerManager, recipes);
		}

		@Override
		public String getRecipeInfo(IMoistenerRecipe recipe) {
			return LogHelper.getStackDescription(recipe.getProduct());
		}
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Adds Moistener fuel
	 * 
	 * @param item The item to use
	 * @param product The item that leaves the moistener's working slot (i.e. mouldy wheat, decayed wheat, mulch)
	 * @param moistenerValue How much this item contributes to the final product of the moistener (i.e. mycelium)
	 * @param stage What stage this product represents. Resources with lower stage value will be consumed first. (First Stage is 0)
	 */
	@ZenMethod
	public static void addFuel(IItemStack item, IItemStack product, int moistenerValue, int stage) {
		if(stage >= 0) {
			MineTweakerAPI.apply(new AddFuel(new MoistenerFuel(toStack(item), toStack(product), moistenerValue, stage)));
		} else {
			LogHelper.logWarning(String.format("No %s Recipe add for %s. Stage parameter must positive!", Moistener.name, item.toString()));
		}
	}

	private static class AddFuel extends BaseMapAddition<ItemStack, MoistenerFuel> {
		public AddFuel(MoistenerFuel fuelEntry) {
			super(Moistener.nameFuel, FuelManager.moistenerResource);
			recipes.put(fuelEntry.getItem(), fuelEntry);
		}
		
		@Override
		public String getRecipeInfo(Entry<ItemStack, MoistenerFuel> fuelEntry) {
			return LogHelper.getStackDescription(fuelEntry.getKey());
		}
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Removes Moistener fuel.
	 * 
	 * @param moistenerItem Item that is a valid fuel for the moistener
	 */
	@ZenMethod
	public static void removeFuel(IIngredient moistenerItem) {
		Map<ItemStack, MoistenerFuel> fuelItems = new HashMap<ItemStack, MoistenerFuel>();
		
		for(Entry<ItemStack, MoistenerFuel> fuelItem : FuelManager.moistenerResource.entrySet()) {
			if(fuelItem != null && matches(moistenerItem, toIItemStack(fuelItem.getValue().getItem()))) {
				fuelItems.put(fuelItem.getKey(), fuelItem.getValue());
			}
		}
		
		if(!fuelItems.isEmpty()) {
			MineTweakerAPI.apply(new RemoveFuel(fuelItems));
		} else {
			LogHelper.logWarning(String.format("No %s Recipe found for %s. Command ignored!", Moistener.name, moistenerItem.toString()));
		}
	}
	
	private static class RemoveFuel extends BaseMapRemoval<ItemStack, MoistenerFuel> {
		public RemoveFuel(Map<ItemStack, MoistenerFuel> recipes) {
			super(Moistener.nameFuel, FuelManager.moistenerResource, recipes);
		}
		
		@Override
		public String getRecipeInfo(Entry<ItemStack, MoistenerFuel> fuelEntry) {
			return LogHelper.getStackDescription(fuelEntry.getKey());
		}
	}
}
