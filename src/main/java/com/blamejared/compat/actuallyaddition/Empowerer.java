package com.blamejared.compat.actuallyaddition;

import com.blamejared.ModTweaker;
import com.blamejared.mtlib.helpers.InputHelper;
import com.blamejared.mtlib.helpers.LogHelper;
import com.blamejared.mtlib.utils.BaseListAddition;
import com.blamejared.mtlib.utils.BaseListRemoval;
import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IItemStack;
import de.ellpeck.actuallyadditions.api.ActuallyAdditionsAPI;
import de.ellpeck.actuallyadditions.api.recipe.EmpowererRecipe;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.Collections;
import java.util.List;

@ZenClass("mods.actuallyadditions.Empowerer")
@ModOnly("actuallyadditions")
@ZenRegister
public class Empowerer {
    
    @ZenMethod
    public static void addRecipe(IItemStack output, IItemStack input, IItemStack modifier1, IItemStack modifier2, IItemStack modifier3, IItemStack modifier4, int energyPerStand, int time, @Optional float[] particleColourArray) {
        if(particleColourArray == null || particleColourArray.length == 0) {
            particleColourArray = new float[]{0, 0, 0};
        }
        ModTweaker.LATE_ADDITIONS.add(new Add(Collections.singletonList(new EmpowererRecipe(InputHelper.toStack(input), InputHelper.toStack(output), InputHelper.toStack(modifier1), InputHelper.toStack(modifier2), InputHelper.toStack(modifier3), InputHelper.toStack(modifier4), energyPerStand, time, particleColourArray))));
    }
    
    @ZenMethod
    public static void removeRecipe(IItemStack output) {
        ModTweaker.LATE_REMOVALS.add(new Remove(output));
    }
    
    private static class Add extends BaseListAddition<EmpowererRecipe> {
        
        protected Add(List<EmpowererRecipe> recipes) {
            super("Empowerer", ActuallyAdditionsAPI.EMPOWERER_RECIPES, recipes);
        }
        
        @Override
        protected String getRecipeInfo(EmpowererRecipe recipe) {
            return LogHelper.getStackDescription(recipe.output);
        }
        
    }
    
    public static class Remove extends BaseListRemoval<EmpowererRecipe> {
        
        private IItemStack output;
        
        protected Remove(IItemStack output) {
            super("Empowerer", ActuallyAdditionsAPI.EMPOWERER_RECIPES);
            this.output = output;
        }
        
        @Override
        public void apply() {
            for(EmpowererRecipe recipe : ActuallyAdditionsAPI.EMPOWERER_RECIPES) {
                if(output.matches(InputHelper.toIItemStack(recipe.output))) {
                    recipes.add(recipe);
                }
            }
            super.apply();
        }
        
        @Override
        protected String getRecipeInfo(EmpowererRecipe recipe) {
            return LogHelper.getStackDescription(recipe.output);
        }
        
    }
    
}