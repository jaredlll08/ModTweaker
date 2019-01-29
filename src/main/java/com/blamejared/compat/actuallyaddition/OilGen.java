package com.blamejared.compat.actuallyaddition;

import com.blamejared.ModTweaker;
import com.blamejared.mtlib.utils.*;
import crafttweaker.annotations.*;
import crafttweaker.api.liquid.ILiquidStack;
import de.ellpeck.actuallyadditions.api.ActuallyAdditionsAPI;
import de.ellpeck.actuallyadditions.api.recipe.*;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.*;

import java.util.*;

@ZenClass("mods.actuallyadditions.OilGen")
@ModOnly("actuallyadditions")
@ZenRegister
public class OilGen {
    
    @ZenMethod
    public static void addRecipe(ILiquidStack fluid, int genAmount, @Optional int genTime) {
        if(genTime == 0) {
            genTime = 100;
        }
        ModTweaker.LATE_ADDITIONS.add(new Add(Collections.singletonList(new OilGenRecipe(fluid.getName(), genAmount, genTime))));
    }
    
    @ZenMethod
    public static void removeRecipe(ILiquidStack output) {
        ModTweaker.LATE_REMOVALS.add(new Remove(output.getName()));
    }
    
    private static class Add extends BaseListAddition<OilGenRecipe> {
        
        protected Add(List<OilGenRecipe> recipes) {
            super("OilGen", ActuallyAdditionsAPI.OIL_GENERATOR_RECIPES, recipes);
        }
        
        @Override
        public String getRecipeInfo(OilGenRecipe recipe) {
            return recipe.fluidName;
        }
        
    }
    
    private static class Remove extends BaseListRemoval<OilGenRecipe> {
        
        private String output;
        
        protected Remove(String output) {
            super("OilGen", ActuallyAdditionsAPI.OIL_GENERATOR_RECIPES);
            this.output = output;
        }
        
        @Override
        public void apply() {
            ActuallyAdditionsAPI.OIL_GENERATOR_RECIPES.forEach(recipe -> {
                if(recipe.fluidName.equals(output)) {
                    recipes.add(recipe);
                }
            });
            super.apply();
        }
        
        @Override
        public String getRecipeInfo(OilGenRecipe recipe) {
            return recipe.fluidName;
        }
        
    }
    
}