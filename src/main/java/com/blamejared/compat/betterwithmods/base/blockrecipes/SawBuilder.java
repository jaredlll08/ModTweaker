package com.blamejared.compat.betterwithmods.base.blockrecipes;

import betterwithmods.common.registry.block.managers.CraftingManagerBlock;
import betterwithmods.common.registry.block.recipe.SawRecipe;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.function.Supplier;

public class SawBuilder extends BlockRecipeBuilder<SawRecipe> {

    public SawBuilder(Supplier<CraftingManagerBlock<SawRecipe>> registry, String name) {
        super(registry, name);
    }

    @ZenMethod
    public SawBuilder buildRecipe(IIngredient input, IItemStack[] outputs) {
        _buildRecipe(input,outputs);
        return this;
    }

    @Override
    public void build() {
        addRecipe(new SawRecipe(input,outputs));
    }

    @ZenMethod
    @Override
    public void removeAll() {
        super.removeAll();
    }
}
