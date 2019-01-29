package com.blamejared.compat.actuallyaddition;

import com.blamejared.ModTweaker;
import com.blamejared.mtlib.helpers.*;
import com.blamejared.mtlib.utils.*;
import crafttweaker.annotations.*;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import de.ellpeck.actuallyadditions.api.ActuallyAdditionsAPI;
import de.ellpeck.actuallyadditions.api.recipe.EmpowererRecipe;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.*;

import java.util.*;

@ZenClass("mods.actuallyadditions.Empowerer")
@ModOnly("actuallyadditions")
@ZenRegister
public class Empowerer {
    
    @ZenMethod
    public static void addRecipe(IItemStack output, IIngredient input, IIngredient modifier1, IIngredient modifier2, IIngredient modifier3, IIngredient modifier4, int energyPerStand, int time, @Optional float[] particleColourArray) {
        if(particleColourArray == null || particleColourArray.length == 0) {
            particleColourArray = new float[]{0, 0, 0};
        }
        ModTweaker.LATE_ADDITIONS.add(new Add(Collections.singletonList(new EmpowererRecipe(CraftTweakerMC.getIngredient(input), InputHelper.toStack(output), CraftTweakerMC.getIngredient(modifier1), CraftTweakerMC.getIngredient(modifier2), CraftTweakerMC.getIngredient(modifier3), CraftTweakerMC.getIngredient(modifier4), energyPerStand, time, particleColourArray))));
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
            return LogHelper.getStackDescription(recipe.getOutput());
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
                if(output.matches(InputHelper.toIItemStack(recipe.getOutput()))) {
                    recipes.add(recipe);
                }
            }
            super.apply();
        }
        
        @Override
        protected String getRecipeInfo(EmpowererRecipe recipe) {
            return LogHelper.getStackDescription(recipe.getOutput());
        }
        
    }
    
}