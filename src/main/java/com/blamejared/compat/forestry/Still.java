package com.blamejared.compat.forestry;

import com.blamejared.ModTweaker;
import com.blamejared.mtlib.helpers.LogHelper;
import com.blamejared.mtlib.utils.BaseAddForestry;
import com.blamejared.mtlib.utils.BaseRemoveForestry;
import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.liquid.ILiquidStack;
import forestry.api.recipes.IStillRecipe;
import forestry.api.recipes.RecipeManagers;
import forestry.factory.recipes.StillRecipe;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import static com.blamejared.mtlib.helpers.InputHelper.toFluid;
import static com.blamejared.mtlib.helpers.InputHelper.toILiquidStack;
import static com.blamejared.mtlib.helpers.StackHelper.matches;

@ZenClass("mods.forestry.Still")
@ModOnly("forestry")
@ZenRegister
public class Still {
    
    public static final String name = "Forestry Still";
    
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    /**
     * Adds recipe to Still
     *
     * @param fluidOutput recipe fluid amount
     * @param fluidInput  recipe fluid input
     * @param timePerUnit time per crafting operation
     */
    @ZenMethod
    public static void addRecipe(ILiquidStack fluidOutput, ILiquidStack fluidInput, int timePerUnit) {
        ModTweaker.LATE_ADDITIONS.add(new Add(new StillRecipe(timePerUnit, toFluid(fluidInput), toFluid(fluidOutput))));
    }
    
    private static class Add extends BaseAddForestry<IStillRecipe> {
        
        public Add(IStillRecipe recipe) {
            super("Forestry Still", RecipeManagers.stillManager, recipe);
        }
        
        @Override
        public String getRecipeInfo() {
            return LogHelper.getStackDescription(recipe.getOutput());
        }
    }
    
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    /**
     * Removes recipe from Still
     *
     * @param output = liquid output
     *               * @param fluidInput = liquid input (optional)
     */
    @ZenMethod
    public static void removeRecipe(ILiquidStack output, @Optional ILiquidStack fluidInput) {
        ModTweaker.LATE_REMOVALS.add(new Remove(output, fluidInput));
    }
    
    private static class Remove extends BaseRemoveForestry<IStillRecipe> {
        
        private final ILiquidStack output;
        private final ILiquidStack fluidInput;
        
        public Remove(ILiquidStack output, ILiquidStack fluidInput) {
            super(Still.name, RecipeManagers.stillManager);
            this.output = output;
            this.fluidInput = fluidInput;
        }
        
        @Override
        public String getRecipeInfo() {
            return fluidInput != null ? fluidInput.getDisplayName() + " --> " : "" + output.getDisplayName();
        }
        
        @Override
        public boolean checkIsRecipe(IStillRecipe r) {
            if(r != null && matches(output, toILiquidStack(r.getOutput()))) {
                if(fluidInput != null) {
                    return matches(fluidInput, toILiquidStack(r.getInput()));
                } else
                    return true;
            }
            return false;
        }
    }
}
