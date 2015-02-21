package modtweaker2.mods.mekanism.handlers;

import static modtweaker2.helpers.InputHelper.toStack;
import mekanism.common.recipe.RecipeHandler.Recipe;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import modtweaker2.mods.mekanism.util.AddMekanismRecipe;
import modtweaker2.mods.mekanism.util.RemoveMekanismRecipe;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.mekanism.Crusher")
public class Crusher {
    @ZenMethod
    public static void addRecipe(IItemStack input, IItemStack output) {
        MineTweakerAPI.apply(new AddMekanismRecipe("CRUSHER", Recipe.CRUSHER.get(), toStack(input), toStack(output)));
    }

    @ZenMethod
    public static void removeRecipe(IItemStack output) {
        MineTweakerAPI.apply(new RemoveMekanismRecipe("CRUSHER", Recipe.CRUSHER.get(), toStack(output)));
    }
}
