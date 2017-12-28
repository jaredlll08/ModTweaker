package com.blamejared.compat.forestry;

import com.blamejared.ModTweaker;
import com.blamejared.mtlib.helpers.InputHelper;
import com.blamejared.mtlib.utils.BaseAddForestry;
import com.blamejared.mtlib.utils.BaseRemoveForestry;
import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.liquid.ILiquidStack;
import forestry.api.recipes.ICarpenterRecipe;
import forestry.api.recipes.RecipeManagers;
import forestry.core.recipes.ShapedRecipeCustom;
import forestry.factory.recipes.CarpenterRecipe;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.Arrays;

import static com.blamejared.mtlib.helpers.InputHelper.*;

@ZenClass("mods.forestry.Carpenter")
@ModOnly("forestry")
@ZenRegister
public class Carpenter {
    
    public static final String name = "Forestry Carpenter";
    
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    /**
     * Adds shaped recipe to Carpenter
     *
     * @param output        recipe product
     * @param ingredients   required ingredients
     * @param packagingTime amount of ticks per crafting operation
     *                      * @param fluidInput      required mB of fluid (optional)
     *                      * @param box             required box in top slot (optional)
     */
    @ZenMethod
    public static void addRecipe(IItemStack output, IIngredient[][] ingredients, int packagingTime, @Optional ILiquidStack fluidInput, @Optional IItemStack box) {
        ModTweaker.LATE_ADDITIONS.add(new Add(new CarpenterRecipe(packagingTime, toFluid(fluidInput), toStack(box), new ShapedRecipeCustom(toStack(output), toShapedObjects(ingredients)))));
    }
    
    private static IItemStack[][] transform(IItemStack[] arr, int N) {
        int M = (arr.length + N - 1) / N;
        IItemStack[][] mat = new IItemStack[M][];
        int start = 0;
        for(int r = 0; r < M; r++) {
            int L = Math.min(N, arr.length - start);
            mat[r] = Arrays.copyOfRange(arr, start, start + L);
            start += L;
        }
        return mat;
    }
    
    
    private static class Add extends BaseAddForestry<ICarpenterRecipe> {
        
        protected Add(ICarpenterRecipe recipe) {
            super(Carpenter.name, RecipeManagers.carpenterManager, recipe);
        }
        
        @Override
        protected String getRecipeInfo() {
            return recipe.getCraftingGridRecipe().getOutput().getDisplayName();
        }
    }
    
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    /**
     * Removes recipe from Carpenter
     *
     * @param output = recipe result
     *               * @param fluidInput = required type of fluid (optional)
     */
    @ZenMethod
    public static void removeRecipe(IItemStack output, @Optional ILiquidStack fluidInput) {
        ModTweaker.LATE_REMOVALS.add(new Remove(output, fluidInput));
    }
    
    private static class Remove extends BaseRemoveForestry<ICarpenterRecipe> {
        
        private ItemStack output;
        private FluidStack fluidInput;
        
        public Remove(IItemStack output, ILiquidStack fluidInput) {
            super(Carpenter.name, RecipeManagers.carpenterManager);
            this.output = InputHelper.toStack(output);
            this.fluidInput = toFluid(fluidInput);
        }
        
        @Override
        public boolean checkIsRecipe(ICarpenterRecipe recipe) {
            if(recipe.getCraftingGridRecipe().getOutput().isItemEqual(output)) {
                if(fluidInput == null) {
                    return true;
                } else {
                    return fluidInput.isFluidEqual(recipe.getFluidResource());
                }
            }
            
            return false;
        }
        
        @Override
        protected String getRecipeInfo() {
            return output.getDisplayName();
        }
    }
}
