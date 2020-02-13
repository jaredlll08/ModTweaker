package com.blamejared.compat.actuallyaddition;

import com.blamejared.ModTweaker;
import com.blamejared.mtlib.helpers.InputHelper;
import com.blamejared.mtlib.helpers.LogHelper;
import com.blamejared.mtlib.utils.BaseListAddition;
import com.blamejared.mtlib.utils.BaseListRemoval;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import de.ellpeck.actuallyadditions.api.ActuallyAdditionsAPI;
import de.ellpeck.actuallyadditions.api.recipe.CompostRecipe;
import net.minecraft.block.Block;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.Collections;
import java.util.List;

@ZenClass("mods.actuallyadditions.Compost")
@ModOnly("actuallyadditions")
@ZenRegister
public class Compost {
    
    @ZenMethod
    public static void addRecipe(IItemStack output, IItemStack outputDisplay, IIngredient input, IItemStack inputDisplay) {
        if(!InputHelper.isABlock(outputDisplay) || !InputHelper.isABlock(inputDisplay)) {
            CraftTweakerAPI.logError("outputDisplay or InputDisplay is not a block!");
            return;
        }
        ModTweaker.LATE_ADDITIONS.add(new Add(Collections.singletonList(new CompostRecipe(CraftTweakerMC.getIngredient(input), Block.getBlockFromItem(InputHelper.toStack(inputDisplay).getItem()).getStateFromMeta(inputDisplay.getMetadata()), InputHelper.toStack(output), Block.getBlockFromItem(InputHelper.toStack(outputDisplay).getItem()).getStateFromMeta(outputDisplay.getMetadata())))));
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
            return LogHelper.getStackDescription(recipe.getOutput());
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
                if(output.matches(InputHelper.toIItemStack(recipe.getOutput()))) {
                    recipes.add(recipe);
                }
            }
            super.apply();
        }
        
        @Override
        public String getRecipeInfo(CompostRecipe recipe) {
            return LogHelper.getStackDescription(recipe.getOutput());
        }
    }
}