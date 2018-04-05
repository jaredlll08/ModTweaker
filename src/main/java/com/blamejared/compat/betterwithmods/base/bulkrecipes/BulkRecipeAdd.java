package com.blamejared.compat.betterwithmods.base.bulkrecipes;

import betterwithmods.common.registry.bulk.manager.CraftingManagerBulk;
import betterwithmods.common.registry.bulk.recipes.BulkRecipe;
import com.blamejared.mtlib.utils.BaseListAddition;
import com.google.common.collect.Lists;
import net.minecraft.item.ItemStack;

import java.util.function.Supplier;
import java.util.stream.Collectors;

public class BulkRecipeAdd<T extends BulkRecipe> extends BaseListAddition<T> {
    
    public BulkRecipeAdd(String name, Supplier<CraftingManagerBulk<T>> recipes, T recipe) {
        super(name, recipes.get().getRecipes(), Lists.newArrayList(recipe));
    }
    
    @Override
    protected String getRecipeInfo(T recipe) {
        return recipe.getOutputs().stream().map(ItemStack::getDisplayName).collect(Collectors.joining(","));
    }
    
}