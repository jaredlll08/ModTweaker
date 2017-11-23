package com.blamejared.compat.botania.handlers;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.blamejared.mtlib.helpers.InputHelper;
import com.blamejared.mtlib.helpers.LogHelper;
import com.blamejared.mtlib.helpers.StringHelper;
import com.blamejared.mtlib.utils.BaseListAddition;
import com.blamejared.mtlib.utils.BaseListRemoval;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.recipe.RecipeBrew;

@ZenClass("mods.botania.Brew")
@ModOnly("botania")
@ZenRegister
public class Brew {
    
    public static final String name = "Botania Brew";
    
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    @ZenMethod
    public static void addRecipe(IIngredient[] inputItems, String brewName) {
        if(inputItems == null || inputItems.length == 0 || brewName == null || brewName.length() == 0) {
            LogHelper.logError(String.format("Required parameters missing for %s Recipe.", name));
            return;
        }
        
        if(!BotaniaAPI.brewMap.containsKey(brewName)) {
            LogHelper.logError(String.format("Unknown brew name \"%s\" for %s recipe.", brewName, name));
        }
        
        RecipeBrew recipe = new RecipeBrew(BotaniaAPI.brewMap.get(brewName), InputHelper.toObjects(inputItems));
        
        CraftTweakerAPI.apply(new Add(recipe));
    }
    
    private static class Add extends BaseListAddition<RecipeBrew> {

        protected Add(RecipeBrew recipe) {
            super(Brew.name, BotaniaAPI.brewRecipes);
            recipes.add(recipe);
        }

        @Override
        protected String getRecipeInfo(RecipeBrew recipe) {
            return recipe.getBrew().getKey();
        }    
    }
    
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    @ZenMethod
    public static void removeRecipe(String brewName) {
        List<RecipeBrew> recipes = new LinkedList<RecipeBrew>();
        Matcher matcher = Pattern.compile(StringHelper.wildcardToRegex(brewName)).matcher("");
        
        for(RecipeBrew recipe : BotaniaAPI.brewRecipes) {
            matcher.reset(recipe.getBrew().getKey());
            if(matcher.matches()) {
                recipes.add(recipe);
            }
        }
        
        if(!recipes.isEmpty()) {
            CraftTweakerAPI.apply(new Remove(recipes));
        } else {
            LogHelper.logWarning(String.format("No %s recipe found for %s. Command ignored!", name, brewName));
        }
    }
    
    public static class Remove extends BaseListRemoval<RecipeBrew> {
        
        protected Remove(List<RecipeBrew> recipes) {
            super(Brew.name, BotaniaAPI.brewRecipes, recipes);
        }
        
        @Override
        protected String getRecipeInfo(RecipeBrew recipe) {
            return recipe.getBrew().getKey();
        }
    }
}
