package modtweaker2.mods.mekanism.handlers;

import static modtweaker2.helpers.InputHelper.toStack;
import mekanism.common.recipe.RecipeHandler.Recipe;
import mekanism.common.recipe.machines.EnrichmentRecipe;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import modtweaker2.mods.mekanism.Mekanism;
import modtweaker2.mods.mekanism.util.AddMekanismRecipe;
import modtweaker2.mods.mekanism.util.RemoveMekanismRecipe;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.mekanism.Enrichment")
public class Enrichment {
    @ZenMethod
    public static void addRecipe(IItemStack input, IItemStack output) {
        if (Mekanism.v7)
            MineTweakerAPI.apply(new AddMekanismRecipe("ENRICHMENT_CHAMBER", Recipe.ENRICHMENT_CHAMBER.get(), toStack(input), toStack(output)));
        else
        {
            EnrichmentRecipe recipe = new EnrichmentRecipe(toStack(input), toStack(output));
            MineTweakerAPI.apply(new AddMekanismRecipe("ENRICHMENT_CHAMBER", Recipe.ENRICHMENT_CHAMBER.get(), recipe.getInput(), recipe));
        }
    }

    @ZenMethod
    public static void removeRecipe(IItemStack input) {
        if (!Mekanism.v7) throw new UnsupportedOperationException("Function not added to v8 compatibility yet");
        MineTweakerAPI.apply(new RemoveMekanismRecipe("ENRICHMENT_CHAMBER", Recipe.ENRICHMENT_CHAMBER.get(), toStack(input)));
    }
}
