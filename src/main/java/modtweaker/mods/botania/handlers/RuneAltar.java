package modtweaker.mods.botania.handlers;

import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import modtweaker.helpers.LogHelper;
import modtweaker.utils.BaseListAddition;
import modtweaker.utils.BaseListRemoval;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.recipe.RecipeRuneAltar;

import java.util.LinkedList;
import java.util.List;

import static modtweaker.helpers.InputHelper.*;
import static modtweaker.helpers.StackHelper.matches;

@ZenClass("mods.botania.RuneAltar")
public class RuneAltar {

    protected static final String name = "Botania Rune Altar";

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @ZenMethod
    public static void addRecipe(IItemStack output, IIngredient[] input, int mana) {
        MineTweakerAPI.apply(new Add(new RecipeRuneAltar(toStack(output), mana, toObjects(input))));
    }

    private static class Add extends BaseListAddition<RecipeRuneAltar> {
        public Add(RecipeRuneAltar recipe) {
            super(RuneAltar.name, BotaniaAPI.runeAltarRecipes);

            recipes.add(recipe);
        }

        @Override
        public String getRecipeInfo(RecipeRuneAltar recipe) {
            return LogHelper.getStackDescription(recipe.getOutput());
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @ZenMethod
    public static void removeRecipe(IIngredient output) {
        // Get list of existing recipes, matching with parameter
        List<RecipeRuneAltar> recipes = new LinkedList<RecipeRuneAltar>();

        for (RecipeRuneAltar r : BotaniaAPI.runeAltarRecipes) {
            if (r != null && r.getOutput() != null && matches(output, toIItemStack(r.getOutput()))) {
                recipes.add(r);
            }
        }

        // Check if we found the recipes and apply the action
        if (!recipes.isEmpty()) {
            MineTweakerAPI.apply(new Remove(recipes));
        } else {
            LogHelper.logWarning(String.format("No %s Recipe found for %s. Command ignored!", ElvenTrade.name, output.toString()));
        }
    }

    private static class Remove extends BaseListRemoval<RecipeRuneAltar> {
        public Remove(List<RecipeRuneAltar> recipes) {
            super(RuneAltar.name, BotaniaAPI.runeAltarRecipes, recipes);
        }

        @Override
        public String getRecipeInfo(RecipeRuneAltar recipe) {
            return LogHelper.getStackDescription(recipe.getOutput());
        }
    }
}
