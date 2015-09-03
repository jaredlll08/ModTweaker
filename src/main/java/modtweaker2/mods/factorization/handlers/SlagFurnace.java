package modtweaker2.mods.factorization.handlers;

import static modtweaker2.helpers.InputHelper.toIItemStack;
import static modtweaker2.helpers.InputHelper.toStack;
import static modtweaker2.helpers.StackHelper.matches;

import java.util.LinkedList;
import java.util.List;

import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import modtweaker2.helpers.LogHelper;
import modtweaker2.helpers.ReflectionHelper;
import modtweaker2.mods.factorization.FactorizationHelper;
import modtweaker2.utils.BaseListAddition;
import modtweaker2.utils.BaseListRemoval;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.factorization.SlagFurnace")
public class SlagFurnace {
    
    public static final String name = "Factorization Slag Furnace";
    
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
	@ZenMethod
	public static void addRecipe(IItemStack input, IItemStack output1, double chance1, IItemStack output2, double chance2) {
		Object recipe = FactorizationHelper.getSlagFurnaceRecipe(toStack(input), (float) chance1, toStack(output1), (float) chance2, toStack(output2));
		MineTweakerAPI.apply(new Add(toStack(input), recipe));
	}

	private static class Add extends BaseListAddition<Object> {
	    @SuppressWarnings("unchecked")
		public Add(ItemStack input, Object recipe) {
			super(SlagFurnace.name, FactorizationHelper.slag);
			recipes.add(recipe);
		}

		@Override
		public String getRecipeInfo(Object recipe) {
			return LogHelper.getStackDescription((ItemStack) ReflectionHelper.getObject(recipe, "input"));
		}
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@ZenMethod
	public static void removeRecipe(IIngredient input) {
	    List<Object> recipes = new LinkedList<Object>();
	    
	    for(Object r : FactorizationHelper.slag) {
	        if(r != null) {
	            ItemStack in = (ItemStack) ReflectionHelper.getObject(r, "input");
	            
	            if(matches(input, toIItemStack(in))) {
	                recipes.add(r);
	            }
	        }
	    }
	    
	    if(!recipes.isEmpty()) {
	        MineTweakerAPI.apply(new Remove(recipes));
	    } else {
	        LogHelper.logWarning(String.format("No %s Recipe found for %s. Command ignored!", SlagFurnace.name, input));
	    }
	}

	private static class Remove extends BaseListRemoval<Object> {
		@SuppressWarnings("unchecked")
        public Remove(List<Object> recipes) {
			super(SlagFurnace.name, FactorizationHelper.slag, recipes);
		}

        @Override
        public String getRecipeInfo(Object recipe) {
            return LogHelper.getStackDescription((ItemStack) ReflectionHelper.getObject(recipe, "input"));
        }
	}
}
