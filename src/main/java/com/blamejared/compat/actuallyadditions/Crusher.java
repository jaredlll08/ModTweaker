package com.blamejared.compat.actuallyadditions;

import com.blamejared.api.annotations.*;
import com.blamejared.mtlib.helpers.*;
import com.blamejared.mtlib.utils.*;
import de.ellpeck.actuallyadditions.api.ActuallyAdditionsAPI;
import de.ellpeck.actuallyadditions.api.recipe.CrusherRecipe;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.*;

import java.util.*;

@Handler("actuallyadditions")
@ZenClass("mods.actuallyadditions.Crusher")
public class Crusher {
    
    @ZenMethod
    @Document({"output", "input", "outputSecondary", "outputSecondaryChance"})
    public static void addRecipe(IItemStack output, IItemStack input, @Optional IItemStack outputSecondary, @Optional int outputSecondaryChance) {
        MineTweakerAPI.apply(new Add(Collections.singletonList(new CrusherRecipe(InputHelper.toStack(input), InputHelper.toStack(output), InputHelper.toStack(outputSecondary), outputSecondaryChance))));
    }
    
    @ZenMethod
    @Document({"output"})
    public static void removeRecipe(IItemStack output) {
        List<CrusherRecipe> recipes = new ArrayList<>();
        ActuallyAdditionsAPI.CRUSHER_RECIPES.forEach(recipe -> {
            if(output.matches(InputHelper.toIItemStack(recipe.outputOneStack))) {
                recipes.add(recipe);
            }
        });
        MineTweakerAPI.apply(new Remove(recipes));
    }
    
    private static class Add extends BaseListAddition<CrusherRecipe> {
        
        protected Add(List<CrusherRecipe> recipes) {
            super("Crusher", ActuallyAdditionsAPI.CRUSHER_RECIPES, recipes);
        }
        
        @Override
        public String getRecipeInfo(CrusherRecipe recipe) {
            return LogHelper.getStackDescription(recipe.inputStack);
        }
        
        @Override
        public String getJEICategory(CrusherRecipe recipe) {
            return "actuallyadditions.crushing";
        }
    }
    
    private static class Remove extends BaseListRemoval<CrusherRecipe> {
        
        protected Remove(List<CrusherRecipe> recipes) {
            super("Crusher", ActuallyAdditionsAPI.CRUSHER_RECIPES, recipes);
        }
        
        @Override
        public String getRecipeInfo(CrusherRecipe recipe) {
            return LogHelper.getStackDescription(recipe.outputOneStack);
        }
        
        @Override
        public String getJEICategory(CrusherRecipe recipe) {
            return "actuallyadditions.crushing";
        }
    }
    
}
