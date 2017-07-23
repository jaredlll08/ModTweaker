package com.blamejared.compat.betterwithmods.util;

import betterwithmods.common.registry.blockmeta.managers.BlockMetaManager;
import betterwithmods.common.registry.blockmeta.recipe.BlockMetaRecipe;
import com.blamejared.mtlib.utils.BaseListRemoval;
import net.minecraft.item.ItemStack;

public class BMRemove extends BaseListRemoval<BlockMetaRecipe> {
    public BMRemove(String name, BlockMetaManager recipes, ItemStack input) {
        super(name, recipes.getRecipes(), recipes.removeRecipes(input));
    }

    @Override
    protected String getRecipeInfo(BlockMetaRecipe recipe) {
        return recipe.getStack().getDisplayName();
    }

}