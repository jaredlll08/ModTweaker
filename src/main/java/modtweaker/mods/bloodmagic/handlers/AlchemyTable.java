package modtweaker.mods.bloodmagic.handlers;

import WayofTime.bloodmagic.api.recipe.AlchemyTableRecipe;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import modtweaker.helpers.LogHelper;
import modtweaker.mods.bloodmagic.BloodMagicHelper;
import modtweaker.utils.BaseListAddition;
import modtweaker.utils.BaseListRemoval;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import static modtweaker.helpers.InputHelper.*;
import static modtweaker.helpers.StackHelper.matches;

import java.util.LinkedList;
import java.util.List;

@ZenClass("mods.bloodmagic.AlchemyTable")
public class AlchemyTable
{
    protected static final String name = "Blood Magic Alchemy Table";

    @ZenMethod
    public static void addRecipe(IItemStack output, int lpDrained, int ticksRequired, int tierRequired, IIngredient[] input)
    {
        if (output == null) {
            LogHelper.logError(String.format("Required parameters missing for %s Recipe.", name));
            return;
        }
        else if(lpDrained < 0)
        {
            LogHelper.logWarning(String.format("LP drained can't be below 0 (%d) for %s Recipe", lpDrained, name));
            return;
        }
        else if(ticksRequired < 0)
        {
            LogHelper.logWarning(String.format("Ticks required can't be below 0 (%d) for %s Recipe", ticksRequired, name));
            return;
        }
        else if(tierRequired < 1)
        {
            LogHelper.logWarning(String.format("Tier required can't be below 1 (%d) for %s Recipe", tierRequired, name));
            return;
        }

        MineTweakerAPI.apply(new Add(new AlchemyTableRecipe(toStack(output), lpDrained, ticksRequired, tierRequired, toObjects(input)), BloodMagicHelper.alchemyTableList));
    }

    private static class Add extends BaseListAddition<AlchemyTableRecipe> {
        public Add(AlchemyTableRecipe recipe, List<AlchemyTableRecipe> list) {
            super(AlchemyTable.name, list);
            this.recipes.add(recipe);
        }

        @Override
        public String getRecipeInfo(AlchemyTableRecipe recipe)
        {
            return LogHelper.getStackDescription(recipe.getRecipeOutput(null));
        }
    }

    @ZenMethod
    public static void removeRecipe(IIngredient output)
    {
        remove(output, BloodMagicHelper.alchemyTableList);
    }

    public static void remove(IIngredient output, List<AlchemyTableRecipe> list) {
        if (output == null) {
            LogHelper.logError(String.format("Required parameters missing for %s Recipe.", name));
            return;
        }

        List<AlchemyTableRecipe> recipes = new LinkedList<>();

        for(AlchemyTableRecipe recipe : list)
        {
            if(matches(output, toIItemStack(recipe.getRecipeOutput(null))))
                recipes.add(recipe);
        }

        if(!recipes.isEmpty())
        {
            MineTweakerAPI.apply(new Remove(list, recipes));
        }
        else
        {
            LogHelper.logWarning(String.format("No %s Recipe found for output %s. Command ignored!", AlchemyTable.name, output.toString()));
        }

    }

    private static class Remove extends BaseListRemoval<AlchemyTableRecipe> {
        public Remove(List<AlchemyTableRecipe> list, List<AlchemyTableRecipe> recipes) {
            super(AlchemyTable.name, list, recipes);
        }

        @Override
        protected String getRecipeInfo(AlchemyTableRecipe recipe) {
            return LogHelper.getStackDescription(recipe.getRecipeOutput(null));
        }
    }
}
