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
import vazkii.botania.api.recipe.RecipeElvenTrade;

@ZenClass("mods.botania.ElvenTrade")
public class ElvenTrade {
    
    protected static final String name = "Botania Eleven Trade";
    
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    @ZenMethod
    public static void addRecipe(IItemStack output, IIngredient[] input) {
        MineTweakerAPI.apply(new Add(new RecipeElvenTrade(toStack(output), toObjects(input))));
    }

    private static class Add extends BaseListAddition<RecipeElvenTrade> {
        public Add(RecipeElvenTrade recipe) {
            super(ElvenTrade.name, BotaniaAPI.elvenTradeRecipes);
            recipes.add(recipe);
        }

        @Override
        public String getRecipeInfo(RecipeElvenTrade recipe) {
            return InputHelper.getStackDescription(recipe.getOutput());
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @ZenMethod
    public static void removeRecipe(IIngredient output) {
        // Get list of existing recipes, matching with parameter
        LinkedList<RecipeElvenTrade> recipes = new LinkedList<RecipeElvenTrade>();
        
        for(RecipeElvenTrade entry : BotaniaAPI.elvenTradeRecipes) {
            if(entry != null && entry.getOutput() != null && matches(output, toIItemStack(entry.getOutput()))) {
                recipes.add(entry);
            }
        }
        
        // Check if we found the recipes and apply the action
        if(!recipes.isEmpty()) {
            MineTweakerAPI.apply(new Remove(recipes));
        } else {
            LogHelper.logWarning(String.format("No %s Recipe found for %s. Command ignored!", ElvenTrade.name, output.toString()));
        }
    }

    private static class Remove extends BaseListRemoval<RecipeElvenTrade> {
        public Remove(List<RecipeElvenTrade> recipes) {
            super(ElvenTrade.name, BotaniaAPI.elvenTradeRecipes, recipes);
        }

        @Override
        public String getRecipeInfo(RecipeElvenTrade recipe) {
            return InputHelper.getStackDescription(recipe.getOutput());
        }
    }
}
