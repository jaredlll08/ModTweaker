package modtweaker.mods.mekanism.handlers;

import static modtweaker.helpers.InputHelper.toStack;
import mekanism.common.recipe.RecipeHandler.Recipe;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import modtweaker.mods.mekanism.util.AddMekanismRecipe;
import modtweaker.mods.mekanism.util.RemoveMekanismRecipe;
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
