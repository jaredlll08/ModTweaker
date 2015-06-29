package modtweaker2.mods.thaumcraft.handlers;

import static modtweaker2.helpers.InputHelper.toIItemStack;
import static modtweaker2.helpers.StackHelper.matches;

import java.util.LinkedList;
import java.util.List;

import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import modtweaker2.helpers.InputHelper;
import modtweaker2.helpers.LogHelper;
import modtweaker2.utils.BaseListAddition;
import modtweaker2.utils.BaseListRemoval;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import thaumcraft.api.internal.WeightedRandomLoot;

@ZenClass("mods.thaumcraft.Loot")
public class Loot {
    
    public static final String name = "Thaumcraft LootBag";
    
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@ZenMethod
	public static void addCommonLoot(IItemStack stack, int weight) {
		MineTweakerAPI.apply(new Add(WeightedRandomLoot.lootBagCommon, new WeightedRandomLoot(InputHelper.toStack(stack), weight)));
	}

	@ZenMethod
	public static void addUncommonLoot(IItemStack stack, int weight) {
		MineTweakerAPI.apply(new Add(WeightedRandomLoot.lootBagUncommon, new WeightedRandomLoot(InputHelper.toStack(stack), weight)));
	}

	@ZenMethod
	public static void addRareLoot(IItemStack stack, int weight) {
		MineTweakerAPI.apply(new Add(WeightedRandomLoot.lootBagRare, new WeightedRandomLoot(InputHelper.toStack(stack), weight)));
	}

	public static class Add extends BaseListAddition<WeightedRandomLoot> {
		public Add(List<WeightedRandomLoot> list, WeightedRandomLoot recipe) {
			super(Loot.name, list);
			recipes.add(recipe);
		}
		
        @Override
        protected String getRecipeInfo(WeightedRandomLoot recipe) {
            return InputHelper.getStackDescription(recipe.item);
        }
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@ZenMethod
	public static void removeCommonLoot(IIngredient stack) {
	    removeLoot(WeightedRandomLoot.lootBagCommon, stack);
	}

	@ZenMethod
	public static void removeUncommonLoot(IIngredient stack) {
	    removeLoot(WeightedRandomLoot.lootBagUncommon, stack);
	}

	@ZenMethod
	public static void removeRareLoot(IIngredient stack) {
		removeLoot(WeightedRandomLoot.lootBagRare, stack);
	}
	
	public static void removeLoot(List<WeightedRandomLoot> list, IIngredient ingredient) {
	    List<WeightedRandomLoot> recipes = new LinkedList<WeightedRandomLoot>();
	    
	    for (WeightedRandomLoot loot : list) {
	        if (matches(ingredient, toIItemStack(loot.item))) {
	            recipes.add(loot);
	        }
	    }
	    
	    if(!recipes.isEmpty()) {
	        MineTweakerAPI.apply(new Remove(list, recipes));
	    } else {
	        LogHelper.logWarning(String.format("No %s Recipe found for %s. Command ignored.", Loot.name, ingredient.toString()));
	    }
	}

	public static class Remove extends BaseListRemoval<WeightedRandomLoot> {
		public Remove(List<WeightedRandomLoot> list, List<WeightedRandomLoot> recipes) {
			super(Loot.name, list, recipes);
		}
		
		@Override
		protected String getRecipeInfo(WeightedRandomLoot recipe) {
		    return InputHelper.getStackDescription(recipe.item);
		}
	}
}
