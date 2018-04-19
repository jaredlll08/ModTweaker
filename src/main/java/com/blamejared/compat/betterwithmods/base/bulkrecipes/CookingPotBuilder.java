package com.blamejared.compat.betterwithmods.base.bulkrecipes;

import betterwithmods.common.registry.bulk.manager.CraftingManagerBulk;
import betterwithmods.common.registry.bulk.recipes.CookingPotRecipe;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.function.Supplier;

public class CookingPotBuilder extends BulkRecipeBuilder<CookingPotRecipe> {
    public static final int UNSTOKED = 1;
    public static final int STOKED = 2;

    private int heat = 1;
    private boolean ignoreHeat = false;

    public CookingPotBuilder(Supplier<CraftingManagerBulk<CookingPotRecipe>> registry, String name) {
        super(registry, name);
    }

    @ZenMethod
    public void build() {
        addRecipe(new CookingPotRecipe(inputs, outputs, heat).setPriority(priority).setIgnoreHeat(ignoreHeat));
    }

    @ZenMethod
    public CookingPotBuilder setHeat(int heat) {
        this.heat = heat;
        return this;
    }

    @ZenMethod
    public CookingPotBuilder setIgnoreHeat(boolean ignoreHeat) {
        this.ignoreHeat = ignoreHeat;
        return this;
    }

    @ZenMethod
    public CookingPotBuilder setPriority(int priority) {
        this.priority = priority;
        return this;
    }

    @ZenMethod
    public CookingPotBuilder buildRecipe(IIngredient[] inputs, IItemStack[] outputs) {
        _buildRecipe(inputs, outputs);
        return this;
    }


}
