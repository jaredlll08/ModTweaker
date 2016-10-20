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
import vazkii.botania.api.recipe.RecipeElvenTrade;

import java.util.LinkedList;
import java.util.List;

import static modtweaker.helpers.InputHelper.toObjects;
import static modtweaker.helpers.InputHelper.toStacks;
import static modtweaker.helpers.StackHelper.matches;

@ZenClass("mods.botania.ElvenTrade")
public class ElvenTrade {
    
    protected static final String name = "Botania Eleven Trade";
    
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    @ZenMethod
    public static void addRecipe(IItemStack[] outputs, IIngredient[] input) {
        MineTweakerAPI.apply(new Add(new RecipeElvenTrade(toStacks(outputs), toObjects(input))));
    }

    private static class Add extends BaseListAddition<RecipeElvenTrade> {
        public Add(RecipeElvenTrade recipe) {
            super(ElvenTrade.name, BotaniaAPI.elvenTradeRecipes);
            recipes.add(recipe);
        }

        @Override
        public String getRecipeInfo(RecipeElvenTrade recipe) {
            return LogHelper.getStackDescription(recipe.getOutputs());
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @ZenMethod
    public static void removeRecipe(IIngredient output) {
        // Get list of existing recipes, matching with parameter
        LinkedList<RecipeElvenTrade> recipes = new LinkedList<RecipeElvenTrade>();
        
        for(RecipeElvenTrade entry : BotaniaAPI.elvenTradeRecipes) {
            if(entry != null && entry.getOutputs() != null && matches(output, toStacks((IIngredient[]) entry.getOutputs().toArray()))) {
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
            //TODO update logging
            return LogHelper.getStackDescription(recipe.getOutputs());
        }
    }
}
