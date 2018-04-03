package com.blamejared.compat.betterwithmods.base.blockrecipes;

import betterwithmods.common.registry.block.managers.CraftingManagerBlock;
import betterwithmods.common.registry.block.recipe.SawRecipe;
import stanhebben.zenscript.annotations.ZenMethod;

public class SawBuilder extends BlockRecipeBuilder<SawRecipe> {

    public SawBuilder(CraftingManagerBlock<SawRecipe> registry, String name) {
        super(registry, name);
    }

    @ZenMethod
    @Override
    public void build() {
        addRecipe(new SawRecipe(input,outputs));
    }
}
