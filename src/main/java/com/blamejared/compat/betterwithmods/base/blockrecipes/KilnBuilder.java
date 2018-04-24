package com.blamejared.compat.betterwithmods.base.blockrecipes;

import betterwithmods.common.registry.block.managers.CraftingManagerBlock;
import betterwithmods.common.registry.block.recipe.KilnRecipe;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.function.Supplier;

public class KilnBuilder extends BlockRecipeBuilder<KilnRecipe> {
    private int heat = 2;
    private boolean ignoreHeat;

    public KilnBuilder(Supplier<CraftingManagerBlock<KilnRecipe>> registry, String name) {
        super(registry, name);
    }

    @ZenMethod
    public KilnBuilder setHeat(int heat) {
        this.heat = heat;
        return this;
    }

    @ZenMethod
    public KilnBuilder setIgnoreHeat(boolean ignoreHeat) {
        this.ignoreHeat = ignoreHeat;
        return this;
    }

    @ZenMethod
    public KilnBuilder buildRecipe(IIngredient input, IItemStack[] outputs) {
        _buildRecipe(input, outputs);
        return this;
    }

    @Override
    public void build() {
        addRecipe(new KilnRecipe(input, outputs, heat).setIgnoreHeat(ignoreHeat));
    }
}
