package com.blamejared.compat.forestry;

import com.blamejared.mtlib.utils.BaseUndoable;
import forestry.api.recipes.ICraftingProvider;
import forestry.api.recipes.IForestryRecipe;

public abstract class BaseAddForestry<R extends IForestryRecipe> extends BaseUndoable{
    private ICraftingProvider<R> provider;
    protected R recipe;

    BaseAddForestry(String name, ICraftingProvider<R> provider, R recipe) {
        super(name);
        this.provider = provider;
        this.recipe = recipe;
    }

    @Override
    public void apply() {
        provider.addRecipe(recipe);
    }
}
