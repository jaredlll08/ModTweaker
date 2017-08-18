package com.blamejared.compat.betterwithmods.util;

import betterwithmods.common.registry.blockmeta.managers.BlockMetaManager;
import betterwithmods.common.registry.blockmeta.recipe.BlockMetaRecipe;
import com.blamejared.mtlib.utils.BaseUndoable;
import net.minecraft.item.ItemStack;

public class BMRemove extends BaseUndoable {

    private final BlockMetaManager recipes;
    private final ItemStack input;

    public BMRemove(String name, BlockMetaManager recipes, ItemStack input) {
        super(name);
        this.recipes = recipes;
        this.input = input;
    }

    protected String getRecipeInfo(BlockMetaRecipe recipe) {
        return recipe.getStack().getDisplayName();
    }

    @Override
    public void apply() {
        recipes.removeRecipes(input);
    }
}