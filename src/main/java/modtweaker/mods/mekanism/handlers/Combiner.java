package modtweaker.mods.mekanism.handlers;

import static modtweaker.helpers.InputHelper.toStack;
import mekanism.api.AdvancedInput;
import mekanism.api.gas.GasRegistry;
import mekanism.common.recipe.RecipeHandler.Recipe;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import modtweaker.mods.mekanism.util.AddMekanismRecipe;
import modtweaker.mods.mekanism.util.RemoveMekanismRecipe;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.mekanism.Combiner")
public class Combiner {
    @ZenMethod
    public static void addRecipe(IItemStack input, IItemStack output) {
        AdvancedInput aInput = new AdvancedInput(toStack(input), GasRegistry.getGas("liquidStone"));
        MineTweakerAPI.apply(new AddMekanismRecipe("COMBINER", Recipe.COMBINER.get(), aInput, toStack(output)));
    }

    @ZenMethod
    public static void removeRecipe(IItemStack output) {
        MineTweakerAPI.apply(new RemoveMekanismRecipe("COMBINER", Recipe.COMBINER.get(), toStack(output)));
    }
}
