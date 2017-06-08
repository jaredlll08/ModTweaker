package com.blamejared.compat.forestry;

import com.blamejared.api.annotations.*;
import com.blamejared.compat.forestry.util.*;
import com.blamejared.mtlib.helpers.LogHelper;
import forestry.api.recipes.*;
import forestry.core.recipes.ShapedRecipeCustom;
import forestry.factory.recipes.CarpenterRecipe;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.*;
import minetweaker.api.liquid.ILiquidStack;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.*;

import java.util.*;

import static com.blamejared.mtlib.helpers.InputHelper.*;
import static com.blamejared.mtlib.helpers.StackHelper.matches;

@ZenClass("mods.forestry.Carpenter")
@Handler("forestry")
public class Carpenter {
    
    @ZenMethod
    @Document({"output", "ingredients", "packagingTime", "fluidInput", "box"})
    public static void addRecipe(IItemStack output, IIngredient[][] ingredients, int packagingTime, @Optional ILiquidStack fluidInput, @Optional IItemStack box) {
        MineTweakerAPI.apply(new Add(new CarpenterRecipe(packagingTime, toFluid(fluidInput), toStack(box), new ShapedRecipeCustom(toStack(output), toShapedObjects(ingredients)))));
    }
    
    @ZenMethod
    @Document({"output", "fluidInput"})
    public static void removeRecipe(IIngredient output, @Optional IIngredient fluidInput) {
        List<ICarpenterRecipe> recipes = new LinkedList<>();
        
        for(ICarpenterRecipe recipe : RecipeManagers.carpenterManager.recipes()) {
            if(recipe != null) {
                ItemStack recipeResult = recipe.getCraftingGridRecipe().getOutput();
                if(recipeResult != null && matches(output, toIItemStack(recipeResult))) {
                    if(fluidInput != null) {
                        if(matches(fluidInput, toILiquidStack(recipe.getFluidResource())))
                            recipes.add(recipe);
                    } else {
                        recipes.add(recipe);
                    }
                }
            }
        }
        
        if(!recipes.isEmpty()) {
            MineTweakerAPI.apply(new Remove(recipes));
        } else {
            LogHelper.logWarning(String.format("No %s Recipe found for %s. Command ignored!", "Carpenter", output.toString()));
        }
    }
    
    private static class Add extends ForestryListAddition<ICarpenterRecipe> {
        
        protected Add(ICarpenterRecipe recipe) {
            super("Carpenter", RecipeManagers.carpenterManager);
            recipes.add(recipe);
        }
        
        @Override
        protected String getRecipeInfo(ICarpenterRecipe recipe) {
            return LogHelper.getStackDescription(recipe.getCraftingGridRecipe().getOutput());
        }
        
        @Override
        public String getJEICategory(ICarpenterRecipe recipe) {
            return "forestry.carpenter";
        }
    }
    
    private static class Remove extends ForestryListRemoval<ICarpenterRecipe, ICarpenterManager> {
        
        public Remove(List<ICarpenterRecipe> recipes) {
            super("Carpenter", RecipeManagers.carpenterManager, recipes);
        }
        
        @Override
        protected String getRecipeInfo(ICarpenterRecipe recipe) {
            return LogHelper.getStackDescription(recipe.getCraftingGridRecipe().getOutput());
        }
        
        @Override
        public String getJEICategory(ICarpenterRecipe recipe) {
            return "forestry.carpenter";
        }
    }
}
