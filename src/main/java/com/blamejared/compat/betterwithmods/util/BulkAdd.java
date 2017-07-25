package com.blamejared.compat.betterwithmods.util;

import betterwithmods.common.registry.bulk.manager.CraftingManagerBulk;
import betterwithmods.common.registry.bulk.recipes.BulkRecipe;
import com.blamejared.mtlib.utils.BaseListAddition;
import com.google.common.collect.Lists;

public class BulkAdd<T extends BulkRecipe> extends BaseListAddition<T> {
    
    public BulkAdd(String name, CraftingManagerBulk<T> recipes, T recipe) {
        super(name, recipes.getRecipes(), Lists.newArrayList(recipe));
    }
    
    @Override
    protected String getRecipeInfo(T recipe) {
        return recipe.getOutput().getDisplayName();
    }
    
}