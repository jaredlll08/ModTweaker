package com.blamejared.compat.betterwithmods.base.blockrecipes;

import betterwithmods.common.registry.block.managers.CraftingManagerBlock;
import betterwithmods.common.registry.block.recipe.BlockRecipe;
import com.blamejared.mtlib.utils.BaseListAddition;
import com.google.common.collect.Lists;
import net.minecraft.item.ItemStack;

import java.util.stream.Collectors;

public class BlockRecipeAdd<T extends BlockRecipe> extends BaseListAddition<T> {

    public BlockRecipeAdd(String name, CraftingManagerBlock<T> recipes, T recipe) {
        super(name, recipes.getRecipes(), Lists.newArrayList(recipe));
    }

    @Override
    protected String getRecipeInfo(T recipe) {
        return recipe.getOutputs().stream().map(ItemStack::getDisplayName).collect(Collectors.joining(","));
    }

}
