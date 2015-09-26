package modtweaker2.mods.forestry.handlers;

import static modtweaker2.helpers.InputHelper.toIItemStack;
import static modtweaker2.helpers.InputHelper.toStack;
import static modtweaker2.helpers.StackHelper.matches;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.item.ItemStack;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import modtweaker2.helpers.LogHelper;
import modtweaker2.utils.BaseListAddition;
import modtweaker2.utils.BaseListRemoval;
import modtweaker2.utils.BaseMapAddition;
import modtweaker2.utils.BaseMapRemoval;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import forestry.api.fuels.FermenterFuel;
import forestry.api.fuels.FuelManager;
import forestry.api.fuels.MoistenerFuel;
import forestry.factory.gadgets.MachineMoistener;
import forestry.factory.gadgets.MachineMoistener.Recipe;
import forestry.factory.gadgets.MachineMoistener.RecipeManager;

@ZenClass("mods.forestry.Moistener")
public class Moistener {
    
    public static final String name = "Forestry Moistener";
    
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
	 * Adds a recipe for the Moistener
	 * 
	 * @param output recipe output
	 * @param resource organic item
	 * @param timePerItem time per item to process
	 */
	@ZenMethod
	public static void addRecipe(IItemStack product, IItemStack resource, int timePerItem) {
		MineTweakerAPI.apply(new Add(new Recipe(toStack(resource), toStack(product), timePerItem)));

	}
    
	@Deprecated
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

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Adds moistener fuel.  
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
			super(Moistener.name, FuelManager.moistenerResource);
			recipes.put(fuelEntry.item, fuelEntry);
		}
		
		@Override
		public String getRecipeInfo(Entry<ItemStack, MoistenerFuel> fuelEntry) {
			return LogHelper.getStackDescription(fuelEntry.getKey());
		}
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Removes a moistener fuel.  
	 * 
	 * @param moistenerItem Item that is a valid fuel for the moistener
	 */
    @ZenMethod
	public static void removeFuel(IIngredient moistenerItem) {
        Map<ItemStack, MoistenerFuel> fuelItems = new HashMap<ItemStack, MoistenerFuel>();
        
        for(Entry<ItemStack, MoistenerFuel> fuelItem : FuelManager.moistenerResource.entrySet()) {
            if(fuelItem != null && matches(moistenerItem, toIItemStack(fuelItem.getValue().item))) {
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
    		super(Moistener.name, FuelManager.moistenerResource, recipes);
    	}

    	@Override
    	public String getRecipeInfo(Entry<ItemStack, MoistenerFuel> fuelEntry) {
    		return LogHelper.getStackDescription(fuelEntry.getKey());
    	}
    }
}
