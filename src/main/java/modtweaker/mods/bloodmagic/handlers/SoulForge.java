package modtweaker.mods.bloodmagic.handlers;

import WayofTime.bloodmagic.api.registry.TartaricForgeRecipeRegistry;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import modtweaker.helpers.LogHelper;
import modtweaker.mods.bloodmagic.BloodMagicHelper;
import modtweaker.utils.BaseListAddition;
import modtweaker.utils.BaseListRemoval;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import WayofTime.bloodmagic.api.recipe.TartaricForgeRecipe;

import static modtweaker.helpers.InputHelper.*;
import static modtweaker.helpers.StackHelper.matches;

import java.util.LinkedList;
import java.util.List;

@ZenClass("mods.bloodmagic.SoulForge")
public class SoulForge
{
    protected static final String name = "BloodMagic Soul Forge";

    @ZenMethod
    public static void addRecipe(IItemStack output, double minimumSouls, double drain, IIngredient[] input)
    {
        MineTweakerAPI.apply(new Add(new TartaricForgeRecipe(toStack(output), minimumSouls, drain, toObjects(input)), BloodMagicHelper.soulForgeList));
    }

    private static class Add extends BaseListAddition<TartaricForgeRecipe> {
        public Add(TartaricForgeRecipe recipe, List<TartaricForgeRecipe> list) {
            super(SoulForge.name, list);
            this.recipes.add(recipe);
        }

        @Override
        public String getRecipeInfo(TartaricForgeRecipe recipe)
        {
            return LogHelper.getStackDescription(recipe.getRecipeOutput());
        }
    }

    @ZenMethod
    public static void removeRecipe(IIngredient output)
    {
        remove(output, BloodMagicHelper.soulForgeList);
    }

    public static void remove(IIngredient output, List<TartaricForgeRecipe> list) {
        if (output == null) {
            LogHelper.logError(String.format("Required parameters missing for %s Recipe.", name));
            return;
        }

        List<TartaricForgeRecipe> recipes = new LinkedList<TartaricForgeRecipe>();

        for(TartaricForgeRecipe recipe : list)
        {
            if(matches(output, toIItemStack(recipe.getRecipeOutput())))
                recipes.add(recipe);
        }

        if(!recipes.isEmpty())
        {
            MineTweakerAPI.apply(new Remove(list, recipes));
        }
        else
        {
            LogHelper.logWarning(String.format("No %s Recipe found for output %s. Command ignored!", SoulForge.name, output.toString()));
        }

    }

    private static class Remove extends BaseListRemoval<TartaricForgeRecipe> {
        public Remove(List<TartaricForgeRecipe> list, List<TartaricForgeRecipe> recipes) {
            super(SoulForge.name, list, recipes);
        }

        @Override
        protected String getRecipeInfo(TartaricForgeRecipe recipe) {
            return LogHelper.getStackDescription(recipe.getRecipeOutput());
        }
    }
}
