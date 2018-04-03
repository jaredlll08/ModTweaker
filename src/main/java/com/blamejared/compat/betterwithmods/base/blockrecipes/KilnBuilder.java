package com.blamejared.compat.betterwithmods.base.blockrecipes;

import betterwithmods.common.registry.block.managers.CraftingManagerBlock;
import betterwithmods.common.registry.block.recipe.KilnRecipe;
import stanhebben.zenscript.annotations.ZenMethod;

public class KilnBuilder extends BlockRecipeBuilder<KilnRecipe> {
    private int heat = 2;

    public KilnBuilder(CraftingManagerBlock<KilnRecipe> registry, String name) {
        super(registry, name);
    }

    @ZenMethod
    public KilnBuilder setHeat(int heat) {
        this.heat = heat;
        return this;
    }

    @ZenMethod
    @Override
    public void build() {
        addRecipe(new KilnRecipe(input, outputs, heat));
    }
}
