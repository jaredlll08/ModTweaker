package modtweaker2.mods.mekanism.handlers;

import static modtweaker2.helpers.InputHelper.toStack;
import static modtweaker2.mods.mekanism.MekanismHelper.toGas;
import mekanism.common.recipe.RecipeHandler.Recipe;
import mekanism.common.recipe.machines.OxidationRecipe;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import modtweaker2.mods.mekanism.Mekanism;
import modtweaker2.mods.mekanism.gas.IGasStack;
import modtweaker2.mods.mekanism.util.AddMekanismRecipe;
import modtweaker2.mods.mekanism.util.RemoveMekanismRecipe;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.mekanism.chemical.Oxidizer")
public class ChemicalOxidizer {
    @ZenMethod
    public static void addRecipe(IItemStack input, IGasStack output) {
        if (Mekanism.v7)
        {
            MineTweakerAPI.apply(new AddMekanismRecipe("CHEMICAL_OXIDIZER", Recipe.CHEMICAL_OXIDIZER.get(), toStack(input), toGas(output)));
        }
        else
        {
            OxidationRecipe recipe = new OxidationRecipe(toStack(input), toGas(output));
            MineTweakerAPI.apply(new AddMekanismRecipe("CHEMICAL_OXIDIZER", Recipe.CHEMICAL_OXIDIZER.get(), recipe.getInput(), recipe));
        }
    }

    @ZenMethod
    public static void removeRecipe(IGasStack output) {
        if (!Mekanism.v7) throw new UnsupportedOperationException("Function not added to v8 compatibility yet");
        MineTweakerAPI.apply(new RemoveMekanismRecipe("CHEMICAL_OXIDIZER", Recipe.CHEMICAL_OXIDIZER.get(), toGas(output)));
    }
}
