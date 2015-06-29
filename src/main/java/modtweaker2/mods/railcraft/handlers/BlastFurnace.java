package modtweaker2.mods.railcraft.handlers;

import static modtweaker2.helpers.InputHelper.toIItemStack;
import static modtweaker2.helpers.InputHelper.toStack;
import static modtweaker2.helpers.StackHelper.matches;

import java.util.LinkedList;
import java.util.List;

import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import mods.railcraft.api.crafting.IBlastFurnaceRecipe;
import modtweaker2.helpers.InputHelper;
import modtweaker2.helpers.LogHelper;
import modtweaker2.mods.railcraft.RailcraftHelper;
import modtweaker2.utils.BaseListAddition;
import modtweaker2.utils.BaseListRemoval;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.railcraft.BlastFurnace")
public class BlastFurnace {
    
    public static final String name = "Railcraft Blast Furnace";
    
	@ZenMethod
	public static void addRecipe(IItemStack input, boolean matchDamage, boolean matchNBT, int cookTime, IItemStack output) {
		MineTweakerAPI.apply(new Add(RailcraftHelper.getBlastFurnaceRecipe(toStack(input), matchDamage, matchNBT, cookTime, toStack(output))));
	}

	private static class Add extends BaseListAddition<IBlastFurnaceRecipe> {
		public Add(IBlastFurnaceRecipe recipe) {
			super(BlastFurnace.name, (List<IBlastFurnaceRecipe>)RailcraftHelper.furnace);
			recipes.add(recipe);
		}

		@Override
		public String getRecipeInfo(IBlastFurnaceRecipe recipe) {
			return InputHelper.getStackDescription(recipe.getOutput());
		}
	}

	@ZenMethod
	public static void removeRecipe(IIngredient output) {
	    List<IBlastFurnaceRecipe> recipes = new LinkedList<IBlastFurnaceRecipe>();
	    
        for (IBlastFurnaceRecipe r : RailcraftHelper.furnace) {
            if (r != null && r.getOutput() != null && matches(output, toIItemStack(r.getOutput()))) {
                recipes.add(r);
            }
        }
	    
        if(!recipes.isEmpty()) {
            MineTweakerAPI.apply(new Remove(recipes));
        } else {
            LogHelper.logWarning(String.format("No %s Recipe found for %s. Command ignored!", BlastFurnace.name, output.toString()));
        }
	}

	private static class Remove extends BaseListRemoval<IBlastFurnaceRecipe> {
		public Remove(List<IBlastFurnaceRecipe> recipes) {
			super(BlastFurnace.name, (List<IBlastFurnaceRecipe>)RailcraftHelper.furnace, recipes);
		}

        @Override
        public String getRecipeInfo(IBlastFurnaceRecipe recipe) {
            return InputHelper.getStackDescription(recipe.getOutput());
        }
	}
}
