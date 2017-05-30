package com.blamejared.compat.actuallyadditions;

import com.blamejared.api.annotations.*;
import com.blamejared.mtlib.helpers.*;
import com.blamejared.mtlib.utils.*;
import de.ellpeck.actuallyadditions.api.ActuallyAdditionsAPI;
import de.ellpeck.actuallyadditions.api.recipe.LensConversionRecipe;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import stanhebben.zenscript.annotations.*;

import java.util.*;

@ZenClass("mods.actuallyadditions.AtomicReconstructor")
@Handler("actuallyadditions")
public class AtomicReconstructor {
    
    @ZenMethod
    @Document({"output", "input", "energyUsed"})
    public static void addRecipe(IItemStack output, IItemStack input, int energyUsed) {
        MineTweakerAPI.apply(new Add(Collections.singletonList(new LensConversionRecipe(InputHelper.toStack(input), InputHelper.toStack(output), energyUsed, ActuallyAdditionsAPI.lensDefaultConversion))));
    }
    
    @ZenMethod
    @Document({"output"})
    public static void removeRecipe(IItemStack output) {
        List<LensConversionRecipe> recipes = new ArrayList<>();
        ActuallyAdditionsAPI.RECONSTRUCTOR_LENS_CONVERSION_RECIPES.stream().filter(recipe -> output.matches(InputHelper.toIItemStack(recipe.outputStack))).forEach(recipes::add);
        MineTweakerAPI.apply(new Remove(recipes));
    }
    
    private static class Add extends BaseListAddition<LensConversionRecipe> {
        
        protected Add(List<LensConversionRecipe> recipies) {
            super("Atomic Reconstructor", ActuallyAdditionsAPI.RECONSTRUCTOR_LENS_CONVERSION_RECIPES, recipies);
        }
        
        @Override
        protected String getRecipeInfo(LensConversionRecipe recipe) {
            return LogHelper.getStackDescription(recipe.outputStack);
        }
        
        @Override
        public String getJEICategory(LensConversionRecipe recipe) {
            return "actuallyadditions.reconstructor";
        }
    }
    
    private static class Remove extends BaseListRemoval<LensConversionRecipe> {
        
        protected Remove(List<LensConversionRecipe> recipies) {
            super("Atomic Reconstructor", ActuallyAdditionsAPI.RECONSTRUCTOR_LENS_CONVERSION_RECIPES, recipies);
        }
        
        @Override
        protected String getRecipeInfo(LensConversionRecipe recipe) {
            return LogHelper.getStackDescription(recipe.outputStack);
        }
        
        @Override
        public String getJEICategory(LensConversionRecipe recipe) {
            return "actuallyadditions.reconstructor";
        }
    }
    
}
