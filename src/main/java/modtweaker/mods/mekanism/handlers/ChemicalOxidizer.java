package modtweaker.mods.mekanism.handlers;

import static modtweaker.helpers.InputHelper.toStack;
import static modtweaker.mods.mekanism.MekanismHelper.toGas;
import mekanism.common.recipe.RecipeHandler.Recipe;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import modtweaker.mods.mekanism.gas.IGasStack;
import modtweaker.mods.mekanism.util.AddMekanismRecipe;
import modtweaker.mods.mekanism.util.RemoveMekanismRecipe;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.mekanism.chemical.Oxidizer")
public class ChemicalOxidizer {
    @ZenMethod
    public static void addRecipe(IItemStack input, IGasStack output) {
        MineTweakerAPI.apply(new AddMekanismRecipe("CHEMICAL_OXIDIZER", Recipe.CHEMICAL_OXIDIZER.get(), toStack(input), toGas(output)));
    }

    @ZenMethod
    public static void removeRecipe(IGasStack output) {
        MineTweakerAPI.apply(new RemoveMekanismRecipe("CHEMICAL_OXIDIZER", Recipe.CHEMICAL_OXIDIZER.get(), toGas(output)));
    }
}
