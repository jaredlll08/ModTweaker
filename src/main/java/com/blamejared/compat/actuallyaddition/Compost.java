package com.blamejared.compat.actuallyaddition;

import com.blamejared.ModTweaker;
import com.blamejared.api.annotations.*;
import com.blamejared.mtlib.helpers.*;
import com.blamejared.mtlib.utils.*;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.*;
import crafttweaker.api.item.IItemStack;
import de.ellpeck.actuallyadditions.api.ActuallyAdditionsAPI;
import de.ellpeck.actuallyadditions.api.recipe.CompostRecipe;
import net.minecraft.block.Block;
import stanhebben.zenscript.annotations.*;

import java.util.*;

@ZenClass("mods.actuallyadditions.Compost")
@ModOnly("actuallyadditions")
@ZenRegister
public class Compost {
    
    @ZenMethod
    public static void addRecipe(IItemStack output, IItemStack outputDisplay, IItemStack input, IItemStack inputDisplay) {
        if(!InputHelper.isABlock(outputDisplay) || !InputHelper.isABlock(inputDisplay)) {
            CraftTweakerAPI.logError("outputDisplay or InputDisplay is not a block!");
            return;
        }
        ModTweaker.LATE_ADDITIONS.add(new Add(Collections.singletonList(new CompostRecipe(InputHelper.toStack(input), Block.getBlockFromItem(InputHelper.toStack(inputDisplay).getItem()), InputHelper.toStack(output), Block.getBlockFromItem(InputHelper.toStack(outputDisplay).getItem())))));
    }
    
    @ZenMethod
    public static void removeRecipe(IItemStack output) {
        
        ModTweaker.LATE_REMOVALS.add(new Remove(output));
    }
    
    private static class Add extends BaseListAddition<CompostRecipe> {
        
        protected Add(List<CompostRecipe> recipes) {
            super("Compost", ActuallyAdditionsAPI.COMPOST_RECIPES, recipes);
        }
        
        @Override
        public String getRecipeInfo(CompostRecipe recipe) {
            return LogHelper.getStackDescription(recipe.output);
        }
    }
    
    private static class Remove extends BaseListRemoval<CompostRecipe> {
        
        private IItemStack output;
        
        protected Remove(IItemStack output) {
            super("Compost", ActuallyAdditionsAPI.COMPOST_RECIPES);
            this.output = output;
        }
        
        @Override
        public void apply() {
            for(CompostRecipe recipe : ActuallyAdditionsAPI.COMPOST_RECIPES) {
                System.out.println(output);
                if(output.matches(InputHelper.toIItemStack(recipe.output))) {
                    recipes.add(recipe);
                }
            }
            super.apply();
        }
        
        @Override
        public String getRecipeInfo(CompostRecipe recipe) {
            return LogHelper.getStackDescription(recipe.output);
        }
    }
}