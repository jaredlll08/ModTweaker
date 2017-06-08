package com.blamejared.compat.refinedstorage;

import com.blamejared.api.annotations.*;
import com.blamejared.mtlib.helpers.*;
import com.blamejared.mtlib.utils.BaseListAddition;
import com.raoulvdberge.refinedstorage.api.solderer.ISoldererRecipe;
import com.raoulvdberge.refinedstorage.apiimpl.API;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import stanhebben.zenscript.annotations.*;

import java.util.*;

@ZenClass("mods.refinedstorage.Solderer")
@Handler("refinedstorage")
public class Solderer {
    
    
    @ZenMethod
    @Document({"output", "time", "rows"})
    public static void addRecipe(IItemStack output, int time, IItemStack[] rows) {
        if(rows.length != 3) {
            MineTweakerAPI.logError("Invalid array length! There have to have 3 items in the array! Use null where applicable!");
            return;
        }
        MineTweakerAPI.apply(new Add(Collections.singletonList(API.instance().getSoldererRegistry().createSimpleRecipe(InputHelper.toStack(output), time, InputHelper.toStacks(rows)))));
    }
    
    @ZenMethod
    @Document({"output"})
    public static void removeRecipe(IItemStack output) {
        List<ISoldererRecipe> recipes = new ArrayList<>();
        for(ISoldererRecipe recipe : API.instance().getSoldererRegistry().getRecipes()) {
            if(output.matches(InputHelper.toIItemStack(recipe.getResult()))) {
                recipes.add(recipe);
            }
        }
        MineTweakerAPI.apply(new Remove(recipes));
    }
    
    private static class Add extends BaseListAddition<ISoldererRecipe> {
        
        protected Add(List<ISoldererRecipe> recipes) {
            super("Solderer", API.instance().getSoldererRegistry().getRecipes(), recipes);
        }
        
        @Override
        protected String getRecipeInfo(ISoldererRecipe recipe) {
            return LogHelper.getStackDescription(recipe.getResult());
        }
        
        @Override
        public String getJEICategory(ISoldererRecipe recipe) {
            return "refinedstorage.solderer";
        }
    }
    
    private static class Remove extends BaseListAddition<ISoldererRecipe> {
        
        protected Remove(List<ISoldererRecipe> recipes) {
            super("Solderer", API.instance().getSoldererRegistry().getRecipes(), recipes);
        }
        
        @Override
        protected String getRecipeInfo(ISoldererRecipe recipe) {
            return LogHelper.getStackDescription(recipe.getResult());
        }
        
        @Override
        public String getJEICategory(ISoldererRecipe recipe) {
            return "refinedstorage.solderer";
        }
    }
}
