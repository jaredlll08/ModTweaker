package com.blamejared.compat.actuallyadditions;

import com.blamejared.api.annotations.*;
import com.blamejared.mtlib.helpers.*;
import com.blamejared.mtlib.utils.*;
import de.ellpeck.actuallyadditions.api.ActuallyAdditionsAPI;
import de.ellpeck.actuallyadditions.api.recipe.EmpowererRecipe;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.*;

import java.util.*;

@ZenClass("mods.actuallyadditions.Empowerer")
@Handler("actuallyadditions")
public class Empowerer {
    
    @ZenMethod
    @Document({"output", "input", "modifier1", "modifier2", "modifier3", "modifier4", "energyPerStand", "time", "particleColourArray"})
    public static void addRecipe(IItemStack output, IItemStack input, IItemStack modifier1, IItemStack modifier2, IItemStack modifier3, IItemStack modifier4, int energyPerStand, int time, @Optional float[] particleColourArray) {
        if(particleColourArray == null || particleColourArray.length == 0) {
            particleColourArray = new float[]{0, 0, 0};
        }
        MineTweakerAPI.apply(new Add(Collections.singletonList(new EmpowererRecipe(InputHelper.toStack(input), InputHelper.toStack(output), InputHelper.toStack(modifier1), InputHelper.toStack(modifier2), InputHelper.toStack(modifier3), InputHelper.toStack(modifier4), energyPerStand, time, particleColourArray))));
    }
    
    @ZenMethod
    @Document({"output"})
    public static void removeRecipe(IItemStack output) {
        List<EmpowererRecipe> recipes = new ArrayList<>();
        for(EmpowererRecipe recipe : ActuallyAdditionsAPI.EMPOWERER_RECIPES) {
            if(output.matches(InputHelper.toIItemStack(recipe.output))) {
                recipes.add(recipe);
            }
        }
        MineTweakerAPI.apply(new Remove(recipes));
    }
    
    private static class Add extends BaseListAddition<EmpowererRecipe> {
        
        protected Add(List<EmpowererRecipe> recipes) {
            super("Empowerer", ActuallyAdditionsAPI.EMPOWERER_RECIPES, recipes);
        }
        
        @Override
        protected String getRecipeInfo(EmpowererRecipe recipe) {
            return LogHelper.getStackDescription(recipe.output);
        }
        
        @Override
        public String getJEICategory(EmpowererRecipe recipe) {
            return "actuallyadditions.empowerer";
        }
    }
    
    public static class Remove extends BaseListRemoval<EmpowererRecipe> {
        
        protected Remove(List<EmpowererRecipe> recipes) {
            super("Empowerer", ActuallyAdditionsAPI.EMPOWERER_RECIPES, recipes);
        }
        
        @Override
        protected String getRecipeInfo(EmpowererRecipe recipe) {
            return LogHelper.getStackDescription(recipe.output);
        }
        
        @Override
        public String getJEICategory(EmpowererRecipe recipe) {
            return "actuallyadditions.empowerer";
        }
    }
    
}
