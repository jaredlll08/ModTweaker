package modtweaker2.mods.mariculture.handlers;

import static modtweaker2.helpers.InputHelper.toIItemStack;
import static modtweaker2.helpers.InputHelper.toStack;
import static modtweaker2.helpers.StackHelper.matches;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import mariculture.api.core.IAnvilHandler.RecipeAnvil;
import mariculture.api.core.MaricultureHandlers;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import modtweaker2.helpers.LogHelper;
import modtweaker2.mods.mariculture.MaricultureHelper;
import modtweaker2.utils.BaseMapAddition;
import modtweaker2.utils.BaseMapRemoval;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.mariculture.Anvil")
public class Anvil {
    
    public static final String name = "Mariculture Anvil";
    
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @ZenMethod
    public static void addRecipe(IItemStack input, IItemStack output, int hits) {
        MineTweakerAPI.apply(new Add(new RecipeAnvil(toStack(input), toStack(output), hits)));
    }

    private static class Add extends BaseMapAddition<String, RecipeAnvil> {
        public Add(RecipeAnvil recipe) {
            super("Mariculture Anvil", MaricultureHandlers.anvil.getRecipes());
            recipes.put(MaricultureHelper.getKey(recipe.input), recipe);
        }

        @Override
        public String getRecipeInfo(Entry<String, RecipeAnvil> recipe) {
            return LogHelper.getStackDescription(recipe.getValue().output);
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //Removing a Mariculture Anvil recipe
    @ZenMethod
    public static void removeRecipe(IIngredient input) {
        Map<String, RecipeAnvil> recipes = new HashMap<String, RecipeAnvil>();
        
        for(Entry<String, RecipeAnvil> recipe : MaricultureHandlers.anvil.getRecipes().entrySet()) {
            if(recipe != null && recipe.getValue() != null && recipe.getValue().input != null && matches(input, toIItemStack(recipe.getValue().input))) {
                recipes.put(recipe.getKey(), recipe.getValue());
            }
        }
        
        if(!recipes.isEmpty()) {
            MineTweakerAPI.apply(new Remove(recipes));
        } else {
            LogHelper.logWarning(String.format("No %s Recipe found for %s. Command ignored!", Anvil.name, input.toString()));
        }
    }

    private static class Remove extends BaseMapRemoval<String, RecipeAnvil> {
        public Remove(Map<String, RecipeAnvil> recipes) {
            super("Mariculture Anvil", MaricultureHandlers.anvil.getRecipes(), recipes);
        }

        @Override
        public String getRecipeInfo(Entry<String, RecipeAnvil> recipe) {
            return LogHelper.getStackDescription(recipe.getValue().output);
        }
    }
}
