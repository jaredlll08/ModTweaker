package modtweaker2.mods.botania.handlers;

import static modtweaker2.helpers.InputHelper.toIItemStack;
import static modtweaker2.helpers.InputHelper.toObjects;
import static modtweaker2.helpers.InputHelper.toStack;
import static modtweaker2.helpers.StackHelper.matches;

import java.util.LinkedList;
import java.util.List;

import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import modtweaker2.helpers.InputHelper;
import modtweaker2.helpers.LogHelper;
import modtweaker2.utils.BaseListAddition;
import modtweaker2.utils.BaseListRemoval;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.recipe.RecipeRuneAltar;

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
            return InputHelper.getStackDescription(recipe.getOutput());
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
        if(!recipes.isEmpty()) {
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
            return InputHelper.getStackDescription(recipe.getOutput());
        }
    }
}
