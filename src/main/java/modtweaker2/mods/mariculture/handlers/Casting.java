package modtweaker2.mods.mariculture.handlers;

import static modtweaker2.helpers.InputHelper.toFluid;
import static modtweaker2.helpers.InputHelper.toIItemStack;
import static modtweaker2.helpers.InputHelper.toILiquidStack;
import static modtweaker2.helpers.InputHelper.toStack;
import static modtweaker2.helpers.StackHelper.matches;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import mariculture.api.core.MaricultureHandlers;
import mariculture.api.core.RecipeCasting;
import mariculture.api.core.RecipeCasting.RecipeNuggetCasting;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import minetweaker.api.liquid.ILiquidStack;
import modtweaker2.helpers.LogHelper;
import modtweaker2.mods.mariculture.MaricultureHelper;
import modtweaker2.utils.BaseMapAddition;
import modtweaker2.utils.BaseMapRemoval;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.mariculture.Casting")
public class Casting {
    
    public static final String name = "Mariculture Casting";
    
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    @ZenMethod
    public static void addNuggetRecipe(ILiquidStack input, IItemStack output) {
        MineTweakerAPI.apply(new Add(new RecipeNuggetCasting(toFluid(input), toStack(output)), MaricultureHandlers.casting.getNuggetRecipes()));
    }

    @ZenMethod
    public static void addIngotRecipe(ILiquidStack input, IItemStack output) {
        MineTweakerAPI.apply(new Add(new RecipeNuggetCasting(toFluid(input), toStack(output)), MaricultureHandlers.casting.getIngotRecipes()));
    }

    @ZenMethod
    public static void addBlockRecipe(ILiquidStack input, IItemStack output) {
        MineTweakerAPI.apply(new Add(new RecipeNuggetCasting(toFluid(input), toStack(output)), MaricultureHandlers.casting.getBlockRecipes()));
    }

    private static class Add extends BaseMapAddition<String, RecipeCasting> {
        public Add(RecipeCasting recipe, HashMap<String, RecipeCasting> map) {
            super(Casting.name, map);
            recipes.put(MaricultureHelper.getKey(recipe.fluid), recipe);
        }

        @Override
        public String getRecipeInfo(Entry<String, RecipeCasting> recipe) {
            return LogHelper.getStackDescription(recipe.getValue().output);
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @ZenMethod
    public static void removeNuggetRecipe(IIngredient ingredient) {
        removeRecipe(ingredient, MaricultureHandlers.casting.getNuggetRecipes());
    }

    @ZenMethod
    public static void removeIngotRecipe(IIngredient ingredient) {
        removeRecipe(ingredient, MaricultureHandlers.casting.getIngotRecipes());
    }

    @ZenMethod
    public static void removeBlockRecipe(IIngredient ingredient) {
        removeRecipe(ingredient, MaricultureHandlers.casting.getBlockRecipes());
    }
    
    public static void removeRecipe(IIngredient ingredient, HashMap<String, RecipeCasting> map) {
        Map<String, RecipeCasting> recipes = new HashMap<String, RecipeCasting>();
        
        for(Entry<String, RecipeCasting> recipe : map.entrySet()) {
            if(recipe != null && recipe.getValue() != null) {
                if(recipe.getValue().fluid != null && matches(ingredient, toILiquidStack(recipe.getValue().fluid))) {
                    recipes.put(recipe.getKey(), recipe.getValue());
                }
                
                if(recipe.getValue().output != null && matches(ingredient, toIItemStack(recipe.getValue().output))) {
                    recipes.put(recipe.getKey(), recipe.getValue());
                }
            }
        }
        
        if(!recipes.isEmpty()) {
            MineTweakerAPI.apply(new Remove(map, recipes));
        }
    }

    private static class Remove extends BaseMapRemoval<String, RecipeCasting> {
        public Remove(Map<String, RecipeCasting> map, Map<String, RecipeCasting> recipes) {
            super(Casting.name, map, recipes);
        }

        @Override
        public String getRecipeInfo(Entry<String, RecipeCasting> recipe) {
            return LogHelper.getStackDescription(recipe.getValue().output);
        }
    }
}
