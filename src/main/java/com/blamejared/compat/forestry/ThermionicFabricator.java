package com.blamejared.compat.forestry;

import com.blamejared.ModTweaker;
import com.blamejared.mtlib.helpers.LogHelper;
import com.blamejared.mtlib.utils.BaseAddForestry;
import com.blamejared.mtlib.utils.BaseRemoveForestry;
import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.liquid.ILiquidStack;
import forestry.api.recipes.IFabricatorRecipe;
import forestry.api.recipes.IFabricatorSmeltingRecipe;
import forestry.api.recipes.RecipeManagers;
import forestry.core.recipes.ShapedRecipeCustom;
import forestry.factory.recipes.FabricatorRecipe;
import forestry.factory.recipes.FabricatorSmeltingRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import static com.blamejared.mtlib.helpers.InputHelper.*;
import static com.blamejared.mtlib.helpers.StackHelper.matches;

@ZenClass("mods.forestry.ThermionicFabricator")
@ModOnly("forestry")
@ZenRegister
public class ThermionicFabricator {
    
    public static final String nameSmelting = "Forestry Thermionic Fabricator (Smelting)";
    public static final String nameCasting = "Forestry Thermionic Fabricator (Casting)";
    
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    /**
     * Adds smelting recipe to Thermionic Fabricator
     *
     * @param liquidStack  fluid that should be used as melting result, currently only "glass" is recommended
     * @param itemInput    recipe input input
     * @param meltingPoint point at where itemInput melts down
     */
    @ZenMethod
    public static void addSmelting(ILiquidStack liquidStack, IItemStack itemInput, int meltingPoint) {
        ModTweaker.LATE_ADDITIONS.add(new AddSmelting(new FabricatorSmeltingRecipe(toStack(itemInput), toFluid(liquidStack), meltingPoint)));
    }
    
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    /**
     * Adds casting recipe to Thermionic Fabricator
     *
     * @param output      recipe output item
     * @param ingredients list of input items
     * @param liquidStack fluid that should be used in the recipe, currently only "glass" is recommended
     *                    * @param plan            recipe plan item (optional)
     *                    * @param remainingItems  no idea(optional)
     */
    @ZenMethod
    public static void addCast(IItemStack output, IIngredient[][] ingredients, ILiquidStack liquidStack, @Optional IItemStack plan) {
        ShapedRecipeCustom patternRecipe = new ShapedRecipeCustom(toStack(output), toShapedObjects(ingredients));
        NonNullList<NonNullList<ItemStack>> ingredientsList = patternRecipe.getRawIngredients();
        
        IFabricatorRecipe recipe = new FabricatorRecipe(toStack(plan), toFluid(liquidStack), toStack(output), ingredientsList, patternRecipe.getOreDicts(), patternRecipe.getWidth(), patternRecipe.getHeight());
        ModTweaker.LATE_ADDITIONS.add(new AddCast(recipe));
    }
    
    /*
    Implements the actions to add a recipe
    Since the machine has two crafting Steps, this is a constructors for both
    */
    private static class AddSmelting extends BaseAddForestry<IFabricatorSmeltingRecipe> {
        
        public AddSmelting(IFabricatorSmeltingRecipe recipe) {
            super(ThermionicFabricator.nameSmelting, RecipeManagers.fabricatorSmeltingManager, recipe);
        }
        
        @Override
        public String getRecipeInfo() {
            return LogHelper.getStackDescription(recipe.getResource());
        }
    }
    
    private static class AddCast extends BaseAddForestry<IFabricatorRecipe> {
        
        public AddCast(IFabricatorRecipe recipe) {
            super(ThermionicFabricator.nameCasting, RecipeManagers.fabricatorManager, recipe);
        }
        
        @Override
        public String getRecipeInfo() {
            return LogHelper.getStackDescription(recipe.getRecipeOutput());
        }
    }
    
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    /**
     * Removes smelting recipe from Thermionic Fabricator
     *
     * @param itemInput = item input
     */
    @ZenMethod
    public static void removeSmelting(IIngredient itemInput) {
        ModTweaker.LATE_REMOVALS.add(new RemoveSmelting(itemInput));
    }
    
    /**
     * Removes casting recipe from Thermionic Fabricator
     *
     * @param product = recipe result
     */
    @ZenMethod
    public static void removeCast(IIngredient product) {
        ModTweaker.LATE_REMOVALS.add(new RemoveCasts(product));
    }
    
    private static class RemoveSmelting extends BaseRemoveForestry<IFabricatorSmeltingRecipe> {
        
        private IIngredient itemInput;
        
        public RemoveSmelting(IIngredient itemInput) {
            super(ThermionicFabricator.nameSmelting, RecipeManagers.fabricatorSmeltingManager);
            this.itemInput = itemInput;
        }
        
        @Override
        public String getRecipeInfo() {
            return itemInput.toString();
        }
        
        @Override
        public boolean checkIsRecipe(IFabricatorSmeltingRecipe recipe) {
            return recipe != null && matches(itemInput, toIItemStack(recipe.getResource()));
        }
    }
    
    private static class RemoveCasts extends BaseRemoveForestry<IFabricatorRecipe> {
        
        private IIngredient product;
        
        public RemoveCasts(IIngredient product) {
            super(ThermionicFabricator.nameCasting, RecipeManagers.fabricatorManager);
            this.product = product;
        }
        
        @Override
        public String getRecipeInfo() {
            return product.toString();
        }
        
        @Override
        public boolean checkIsRecipe(IFabricatorRecipe recipe) {
            return recipe != null && matches(product, toIItemStack(recipe.getRecipeOutput()));
        }
    }
}
