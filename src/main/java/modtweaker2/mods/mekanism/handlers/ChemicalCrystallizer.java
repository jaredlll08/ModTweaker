package modtweaker2.mods.mekanism.handlers;

import static modtweaker2.helpers.InputHelper.toStack;
import static modtweaker2.mods.mekanism.MekanismHelper.toGas;
import mekanism.common.recipe.RecipeHandler.Recipe;
import mekanism.common.recipe.machines.CrystallizerRecipe;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import modtweaker2.mods.mekanism.Mekanism;
import modtweaker2.mods.mekanism.gas.IGasStack;
import modtweaker2.mods.mekanism.util.AddMekanismRecipe;
import modtweaker2.mods.mekanism.util.RemoveMekanismRecipe;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.mekanism.chemical.Crystallizer")
public class ChemicalCrystallizer {
    @ZenMethod
    public static void addRecipe(IGasStack input, IItemStack output) {
        if (Mekanism.v7)
            MineTweakerAPI.apply(new AddMekanismRecipe("CHEMICAL_CRYSTALLIZER", Recipe.CHEMICAL_CRYSTALLIZER.get(), toGas(input), toStack(output)));
        else
        {
            CrystallizerRecipe recipe = new CrystallizerRecipe(toGas(input), toStack(output));
            MineTweakerAPI.apply(new AddMekanismRecipe("CHEMICAL_CRYSTALLIZER", Recipe.CHEMICAL_CRYSTALLIZER.get(), recipe.getInput(), recipe));
        }
    }

    @ZenMethod
    public static void removeRecipe(IItemStack output) {
        if (!Mekanism.v7) throw new UnsupportedOperationException("Function not added to v8 compatibility yet");
        MineTweakerAPI.apply(new RemoveMekanismRecipe("CHEMICAL_CRYSTALLIZER", Recipe.CHEMICAL_CRYSTALLIZER.get(), toStack(output)));
    }
}
