package modtweaker2.mods.mariculture.handlers;

import static modtweaker2.helpers.InputHelper.toFluid;
import static modtweaker2.helpers.InputHelper.toIItemStack;
import static modtweaker2.helpers.InputHelper.toStack;
import static modtweaker2.helpers.StackHelper.matches;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import mariculture.api.core.FuelInfo;
import mariculture.api.core.MaricultureHandlers;
import mariculture.api.core.RecipeSmelter;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import minetweaker.api.liquid.ILiquidStack;
import modtweaker2.helpers.LogHelper;
import modtweaker2.mods.mariculture.MaricultureHelper;
import modtweaker2.utils.BaseListAddition;
import modtweaker2.utils.BaseListRemoval;
import modtweaker2.utils.BaseMapAddition;
import modtweaker2.utils.BaseMapRemoval;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.mariculture.Crucible")
public class Crucible {
    
    public static final String nameSmelting = "Mariculture Crucible (Smelting)";
    public static final String nameFuel = "Mariculture Crucible (Fuel)";

	/********************************************** Mariculture Crucible Recipes **********************************************/

	// Adding a Mariculture Crucible recipe
	@ZenMethod
	public static void addRecipe(int temp, IItemStack input, ILiquidStack fluid, @Optional IItemStack output, @Optional int chance) {
		ItemStack out = output != null ? toStack(output) : null;
		MineTweakerAPI.apply(new AddRecipe(new RecipeSmelter(toStack(input), null, temp, toFluid(fluid), out, chance)));
	}

	// Passes the list to the base list implementation, and adds the recipe
	private static class AddRecipe extends BaseListAddition<RecipeSmelter> {
		public AddRecipe(RecipeSmelter recipe) {
			super(Crucible.nameSmelting, MaricultureHandlers.crucible.getRecipes());
			recipes.add(recipe);
		}

		@Override
		public String getRecipeInfo(RecipeSmelter recipe) {
			return LogHelper.getStackDescription(recipe.input);
		}
	}

	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	// Removing a Mariculture Crucible recipe
	@ZenMethod
	public static void removeRecipe(IIngredient input) {
	    List<RecipeSmelter> recipes = new LinkedList<RecipeSmelter>();
	    
        for (RecipeSmelter r : MaricultureHandlers.crucible.getRecipes()) {
            if (r != null) {
                if (r.input != null && matches(input, toIItemStack(r.input))) {
                    recipes.add(r);
                }
            }
        }
        
        if(!recipes.isEmpty()) {
            MineTweakerAPI.apply(new RemoveRecipe(recipes));            
        } else {
            LogHelper.logWarning(String.format("No %s Recipe found for %s. Command ignored", Crucible.nameSmelting, input.toString()));
        }
		
	}

	private static class RemoveRecipe extends BaseListRemoval<RecipeSmelter> {
		public RemoveRecipe(List<RecipeSmelter> recipes) {
			super(Crucible.nameSmelting, MaricultureHandlers.crucible.getRecipes(), recipes);
		}
		
        @Override
        public String getRecipeInfo(RecipeSmelter recipe) {
            return LogHelper.getStackDescription(recipe.input);
        }
	}

	/********************************************** Crucible Fuels **********************************************/
	@ZenMethod
	public static void addFuel(IItemStack input, int max, int per, int time) {
		MineTweakerAPI.apply(new AddFuel(toStack(input), new FuelInfo(max, per, time)));
	}

	@ZenMethod
	public static void addFuel(ILiquidStack input, int max, int per, int time) {
		MineTweakerAPI.apply(new AddFuel(toFluid(input), new FuelInfo(max, per, time)));
	}

	@ZenMethod
	public static void addFuel(String input, int max, int per, int time) {
		MineTweakerAPI.apply(new AddFuel(input, new FuelInfo(max, per, time)));
	}

	// Passes the list to the base map implementation, and adds the recipe
	private static class AddFuel extends BaseMapAddition<Object, FuelInfo> {
		public AddFuel(Object o, FuelInfo info) {
			super(Crucible.nameFuel, MaricultureHelper.fuels);
			recipes.put(MaricultureHelper.getKey(o), info);
		}

        @Override
        public String getRecipeInfo(Entry<Object, FuelInfo> recipe) {
            return (String) recipe.getKey();
        }
	}

	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	@ZenMethod
	public static void removeFuel(IItemStack fuel) {
	    removeFuelEntry(fuel);
	}

	@ZenMethod
	public static void removeFuel(ILiquidStack fuel) {
	    removeFuelEntry(fuel);
	}

	@ZenMethod
	public static void removeFuel(String fuel) {
	    removeFuelEntry(fuel);
	}
	
	public static void removeFuelEntry(Object o) {
	    String key = MaricultureHelper.getKey(o);
	    Map<Object, FuelInfo> recipes = new HashMap<Object, FuelInfo>();
	    
	    for(Entry<Object, FuelInfo> entry : MaricultureHelper.fuels.entrySet()) {
	        if(key.matches((String)entry.getKey())) {
	            recipes.put(key, entry.getValue());
	        }
	    }
	    
	    if(!recipes.isEmpty()) {
	        MineTweakerAPI.apply(new RemoveFuel(recipes));
	    }
	}

	// Removes a recipe, will always remove the key, so all should be good
	private static class RemoveFuel extends BaseMapRemoval<Object, FuelInfo> {
		public RemoveFuel(Map<Object, FuelInfo> recipes) {
			super(Crucible.nameFuel, MaricultureHelper.fuels, recipes);
		}

        @Override
        public String getRecipeInfo(Entry<Object, FuelInfo> recipe) {
            return (String) recipe.getKey();
        }
	}
}
