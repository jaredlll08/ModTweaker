package com.blamejared.compat.betterwithmods.util;


import betterwithmods.common.registry.bulk.manager.CraftingManagerBulk;
import betterwithmods.common.registry.bulk.recipes.BulkRecipe;
import com.blamejared.mtlib.utils.BaseListRemoval;
import net.minecraft.item.ItemStack;


public class BulkRemove<T extends BulkRecipe> extends BaseListRemoval<T> {
    
    public BulkRemove(String name, CraftingManagerBulk<T> recipes, ItemStack output, ItemStack secondary, Object... inputs) {
        super(name, recipes.getRecipes(), recipes.findRecipeForRemoval(output, secondary, inputs));
    }
    
    @Override
    protected String getRecipeInfo(BulkRecipe recipe) {
        return recipe.getOutput().getDisplayName();
    }
    
}