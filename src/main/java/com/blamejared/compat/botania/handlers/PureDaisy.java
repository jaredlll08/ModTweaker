package com.blamejared.compat.botania.handlers;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.blamejared.ModTweaker;
import com.blamejared.mtlib.helpers.InputHelper;
import com.blamejared.mtlib.helpers.LogHelper;
import com.blamejared.mtlib.helpers.StackHelper;
import com.blamejared.mtlib.utils.BaseListAddition;
import com.blamejared.mtlib.utils.BaseListRemoval;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.recipe.RecipePureDaisy;

@ZenClass("mods.botania.PureDaisy")
@ModOnly("botania")
@ZenRegister
public class PureDaisy {
    
    public static final String name = "Botania PureDaisy";
    
    
    @ZenMethod
    public static void addRecipe(IIngredient blockInput, IItemStack blockOutput, @Optional int time) {
        if(blockInput == null || blockOutput == null) {
            LogHelper.logError(String.format("Required parameters missing for %s Recipe.", name));
            return;
        }
        
        Object input = InputHelper.toObject(blockInput);
        
        if(input == null || (input instanceof ItemStack && !InputHelper.isABlock((ItemStack) input))) {
            LogHelper.logError(String.format("Input must be a block or an oredict entry."));
            return;
        }
        
        if(input instanceof ItemStack)
            input = Block.getBlockFromItem(((ItemStack) input).getItem());
        ItemStack output = InputHelper.toStack(blockOutput);
        
        RecipePureDaisy recipe = new RecipePureDaisy(input, Block.getBlockFromItem(output.getItem()).getDefaultState(), time);
        
        CraftTweakerAPI.apply(new Add(recipe));
    }
    
    private static class Add extends BaseListAddition<RecipePureDaisy> {
        
        public Add(RecipePureDaisy recipe) {
            super(PureDaisy.name, BotaniaAPI.pureDaisyRecipes);
            recipes.add(recipe);
        }
        
        @Override
        protected String getRecipeInfo(RecipePureDaisy recipe) {
            return LogHelper.getStackDescription(new ItemStack(recipe.getOutputState().getBlock(), 1));
        }
    }
    
    
    @ZenMethod
    public static void removeRecipe(IIngredient output) {
        ModTweaker.LATE_REMOVALS.add(new Remove(output));
    }
    
    private static class Remove extends BaseListRemoval<RecipePureDaisy> {
        
        final IIngredient output;
        
        public Remove(IIngredient output) {
            super(PureDaisy.name, BotaniaAPI.pureDaisyRecipes, Collections.emptyList());
            this.output = output;
        }
        
        @Override
        protected String getRecipeInfo(RecipePureDaisy recipe) {
            return LogHelper.getStackDescription(new ItemStack(recipe.getOutputState().getBlock(), 1));
        }
        
        @Override
        public void apply() {
            List<RecipePureDaisy> recipes = new LinkedList<>();
            
            for(RecipePureDaisy recipe : BotaniaAPI.pureDaisyRecipes) {
                IItemStack out = InputHelper.toIItemStack(new ItemStack(recipe.getOutputState().getBlock(), 1));
                
                if(StackHelper.matches(output, out)) {
                    recipes.add(recipe);
                }
            }
            
            if(!recipes.isEmpty()) {
                this.recipes.addAll(recipes);
                super.apply();
            } else {
                LogHelper.logWarning(String.format("No %s Recipe found for %s. Command ignored!", PureDaisy.name, output.toString()));
            }
            CraftTweakerAPI.getLogger().logInfo(super.describe());
        }
        
        @Override
        public String describe() {
            return "Attempting to remove Pure Daisy recipe for " + output.getItems();
        }
    }
}
