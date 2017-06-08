package com.blamejared.compat.forestry.util;

import com.blamejared.mtlib.helpers.LogHelper;
import com.blamejared.mtlib.utils.BaseListRemoval;
import forestry.api.recipes.*;
import minetweaker.MineTweakerAPI;

import java.util.*;

public abstract class ForestryListRemoval<T extends IForestryRecipe, C extends ICraftingProvider<T>> extends BaseListRemoval<T> {
    
    private final C craftingProvider;
    
    public ForestryListRemoval(String name, C craftingProvider, List<T> recipes) {
        super(name, new ArrayList<T>(craftingProvider.recipes()), recipes);
        this.craftingProvider = craftingProvider;
    }
    
    @Override
    protected abstract String getRecipeInfo(T recipe);
    
    @Override
    public void apply() {
        for(T recipe : recipes) {
            if(recipe != null) {
                if(craftingProvider.removeRecipe(recipe)) {
                    successful.add(recipe);
                    MineTweakerAPI.getIjeiRecipeRegistry().removeRecipe(recipe, getJEICategory(recipe));
                } else {
                    LogHelper.logError(String.format("Error removing %s Recipe for %s", name, getRecipeInfo(recipe)));
                }
            } else {
                LogHelper.logError(String.format("Error removing %s Recipe: null object", name));
            }
        }
    }
    
    @Override
    public final void undo() {
        for(T recipe : successful) {
            if(recipe != null) {
                if(!craftingProvider.addRecipe(recipe)) {
                    LogHelper.logError(String.format("Error restoring %s Recipe for %s", name, getRecipeInfo(recipe)));
                } else {
                    MineTweakerAPI.getIjeiRecipeRegistry().addRecipe(recipe, getJEICategory(recipe));
                }
            } else {
                LogHelper.logError(String.format("Error restoring %s Recipe: null object", name));
            }
        }
    }
    
}