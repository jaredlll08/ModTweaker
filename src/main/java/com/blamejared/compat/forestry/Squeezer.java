package com.blamejared.compat.forestry;

import com.blamejared.api.annotations.Handler;
import com.blamejared.compat.forestry.util.*;
import com.blamejared.mtlib.helpers.LogHelper;
import forestry.api.recipes.*;
import forestry.factory.recipes.SqueezerRecipe;
import forestry.factory.recipes.jei.squeezer.SqueezerRecipeWrapper;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.*;
import minetweaker.api.liquid.ILiquidStack;
import minetweaker.mc1112.item.MCItemStack;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.*;

import java.util.*;

import static com.blamejared.mtlib.helpers.InputHelper.*;
import static com.blamejared.mtlib.helpers.StackHelper.matches;

@ZenClass("mods.forestry.Squeezer")
@Handler("forestry")
public class Squeezer {
    
    public static final String name = "Forestry Squeezer";
    
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    /**
     * Adds recipe to Squeezer
     *
     * @param fluidOutput recipe fluid amount
     * @param ingredients recipe ingredients
     * @param timePerItem time per crafting operation
     *                    * @param itemOutput recipe output (optional)
     */
    @ZenMethod
    public static void addRecipe(ILiquidStack fluidOutput, IItemStack[] ingredients, int timePerItem, @Optional WeightedItemStack itemOutput) {
        if(itemOutput == null) {
            itemOutput = new WeightedItemStack(new MCItemStack(new ItemStack(Blocks.AIR)), 0);
        }
        NonNullList<ItemStack> list = NonNullList.withSize(ingredients.length, ItemStack.EMPTY);
        for(int i = 0; i < toStacks(ingredients).length; i++) {
            list.set(i, toStack(ingredients[i]));
        }
        MineTweakerAPI.apply(new Add(new SqueezerRecipe(timePerItem, list, toFluid(fluidOutput), toStack(itemOutput.getStack()), itemOutput.getChance())));
    }
    
    private static class Add extends ForestryListAddition<ISqueezerRecipe> {
        
        public Add(ISqueezerRecipe recipe) {
            super(Squeezer.name, RecipeManagers.squeezerManager);
            recipes.add(recipe);
            MineTweakerAPI.getIjeiRecipeRegistry().addRecipe(new SqueezerRecipeWrapper(recipe));
        }
        
        @Override
        public String getRecipeInfo(ISqueezerRecipe recipe) {
            return LogHelper.getStackDescription(recipe.getFluidOutput());
        }
        
        @Override
        public String getJEICategory(ISqueezerRecipe recipe) {
            return "forestry.squeezer";
        }
    }
    
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    /**
     * Removes a recipe for the Centrifuge
     *
     * @param liquid liquid output
     *               * @param ingredients list of ingredients (optional)
     */
    @ZenMethod
    public static void removeRecipe(IIngredient liquid, @Optional IIngredient[] ingredients) {
        List<ISqueezerRecipe> recipes = new LinkedList<ISqueezerRecipe>();
        
        for(ISqueezerRecipe r : RecipeManagers.squeezerManager.recipes()) {
            if(r != null && r.getFluidOutput() != null && matches(liquid, toILiquidStack(r.getFluidOutput()))) {
                // optional check for ingredients
                if(ingredients != null) {
                    boolean matched = false;
                    for(int i = 0; i < ingredients.length; i++) {
                        if(matches(ingredients[i], toIItemStack(r.getResources().get(i))))
                            matched = true;
                        else {
                            matched = false;
                            // if one ingredients doesn't match abort all further checks
                            break;
                        }
                    }
                    // if some ingredient doesn't match, the last one is false
                    if(matched)
                        recipes.add(r);
                } else {
                    recipes.add(r);
                }
            }
        }
        
        if(!recipes.isEmpty()) {
            MineTweakerAPI.apply(new Remove(recipes));
        } else {
            LogHelper.logWarning(String.format("No %s Recipe found for %s. Command ignored!", Squeezer.name, LogHelper.getStackDescription(liquid)));
        }
    }
    
    private static class Remove extends ForestryListRemoval<ISqueezerRecipe, ISqueezerManager> {
        
        public Remove(List<ISqueezerRecipe> recipes) {
            super(Squeezer.name, RecipeManagers.squeezerManager, recipes);
        }
        
        @Override
        public String getRecipeInfo(ISqueezerRecipe recipe) {
            return LogHelper.getStackDescription(recipe.getFluidOutput());
        }
        
        @Override
        public String getJEICategory(ISqueezerRecipe recipe) {
            return "forestry.squeezer";
        }
    }
}