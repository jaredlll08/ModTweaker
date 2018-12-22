package com.blamejared.compat.actuallyaddition;

import com.blamejared.ModTweaker;
import com.blamejared.mtlib.helpers.*;
import com.blamejared.mtlib.utils.*;
import crafttweaker.annotations.*;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import de.ellpeck.actuallyadditions.api.ActuallyAdditionsAPI;
import de.ellpeck.actuallyadditions.api.recipe.CrusherRecipe;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.*;

import java.util.*;

@ZenClass("mods.actuallyadditions.Crusher")
@ModOnly("actuallyadditions")
@ZenRegister
public class Crusher {
    
    @ZenMethod
    public static void addRecipe(IItemStack output, IIngredient input, @Optional IItemStack outputSecondary, @Optional int outputSecondaryChance) {
        ModTweaker.LATE_ADDITIONS.add(new Add(Collections.singletonList(new CrusherRecipe(CraftTweakerMC.getIngredient(input), InputHelper.toStack(output), InputHelper.toStack(outputSecondary), outputSecondaryChance))));
    }
    
    @ZenMethod
    public static void removeRecipe(IItemStack output) {
        ModTweaker.LATE_REMOVALS.add(new Remove(output));
    }
    
    private static class Add extends BaseListAddition<CrusherRecipe> {
        
        protected Add(List<CrusherRecipe> recipes) {
            super("Crusher", ActuallyAdditionsAPI.CRUSHER_RECIPES, recipes);
        }
        
        @Override
        public String getRecipeInfo(CrusherRecipe recipe) {
            return LogHelper.getStackDescription(recipe.getInput());
        }
        
    }
    
    private static class Remove extends BaseListRemoval<CrusherRecipe> {
        
        private IItemStack output;
        
        protected Remove(IItemStack output) {
            super("Crusher", ActuallyAdditionsAPI.CRUSHER_RECIPES);
            this.output = output;
        }
        
        @Override
        public void apply() {
            ActuallyAdditionsAPI.CRUSHER_RECIPES.forEach(recipe -> {
                if(output.matches(InputHelper.toIItemStack(recipe.getOutputOne()))) {
                    recipes.add(recipe);
                }
            });
            super.apply();
        }
        
        @Override
        public String getRecipeInfo(CrusherRecipe recipe) {
            return LogHelper.getStackDescription(recipe.getOutputOne());
        }
        
    }
    
}