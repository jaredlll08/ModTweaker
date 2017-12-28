package com.blamejared.compat.forestry;

import com.blamejared.ModTweaker;
import com.blamejared.mtlib.helpers.LogHelper;
import com.blamejared.mtlib.utils.BaseAddForestry;
import com.blamejared.mtlib.utils.BaseRemoveForestry;
import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.item.WeightedItemStack;
import forestry.api.recipes.ICentrifugeRecipe;
import forestry.api.recipes.RecipeManagers;
import forestry.factory.recipes.CentrifugeRecipe;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.HashMap;
import java.util.Map;

import static com.blamejared.mtlib.helpers.InputHelper.toIItemStack;
import static com.blamejared.mtlib.helpers.InputHelper.toStack;
import static com.blamejared.mtlib.helpers.StackHelper.matches;


@ZenClass("mods.forestry.Centrifuge")
@ModOnly("forestry")
@ZenRegister
public class Centrifuge {
    
    public static final String name = "Forestry Centrifuge";
    
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    /**
     * Adds recipe to Centrifuge
     *
     * @param output        recipe product
     * @param ingredients   required ingredients
     * @param packagingTime amount of ticks per crafting operation
     */
    @ZenMethod
    public static void addRecipe(WeightedItemStack[] output, IItemStack ingredients, int packagingTime) {
        Map<ItemStack, Float> products = new HashMap<>();
        for(WeightedItemStack product : output) {
            products.put(toStack(product.getStack()), product.getChance());
        }
        ModTweaker.LATE_ADDITIONS.add(new Add(new CentrifugeRecipe(packagingTime, toStack(ingredients), products)));
    }
    
    private static class Add extends BaseAddForestry<ICentrifugeRecipe> {
        
        public Add(ICentrifugeRecipe recipe) {
            super(Centrifuge.name, RecipeManagers.centrifugeManager, recipe);
        }
        
        @Override
        protected String getRecipeInfo() {
            return LogHelper.getStackDescription(recipe.getInput());
        }
    }
    
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    /**
     * Removes a recipe for the Centrifuge
     *
     * @param input type of item in input
     */
    @ZenMethod
    public static void removeRecipe(IIngredient input) {
        ModTweaker.LATE_REMOVALS.add(new Remove(input));
    }
    
    private static class Remove extends BaseRemoveForestry<ICentrifugeRecipe> {
        
        private IIngredient input;
        
        public Remove(IIngredient input) {
            super(Centrifuge.name, RecipeManagers.centrifugeManager);
            this.input = input;
        }
        
        @Override
        protected String getRecipeInfo() {
            return input.toString();
        }
        
        @Override
        public boolean checkIsRecipe(ICentrifugeRecipe recipe) {
            return recipe != null && matches(input, toIItemStack(recipe.getInput()));
        }
    }
}
