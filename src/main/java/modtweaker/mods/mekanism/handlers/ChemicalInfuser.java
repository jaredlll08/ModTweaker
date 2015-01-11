package modtweaker.mods.mekanism.handlers;

import static modtweaker.mods.mekanism.MekanismHelper.toGas;
import mekanism.api.ChemicalPair;
import mekanism.common.recipe.RecipeHandler.Recipe;
import minetweaker.MineTweakerAPI;
import modtweaker.mods.mekanism.gas.IGasStack;
import modtweaker.mods.mekanism.util.AddMekanismRecipe;
import modtweaker.mods.mekanism.util.RemoveMekanismRecipe;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.mekanism.chemical.Infuser")
public class ChemicalInfuser {
    @ZenMethod
    public static void addRecipe(IGasStack left, IGasStack right, IGasStack out) {
        ChemicalPair pair = new ChemicalPair(toGas(left), toGas(right));
        MineTweakerAPI.apply(new AddMekanismRecipe("CHEMICAL_INFUSER", Recipe.CHEMICAL_INFUSER.get(), pair, toGas(out)));
    }

    @ZenMethod
    public static void removeRecipe(IGasStack output) {
        MineTweakerAPI.apply(new RemoveMekanismRecipe("CHEMICAL_INFUSER", Recipe.CHEMICAL_INFUSER.get(), toGas(output)));
    }
}
