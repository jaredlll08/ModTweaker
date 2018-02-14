package com.blamejared.compat.betterwithmods.util;


import betterwithmods.common.registry.bulk.manager.CraftingManagerBulk;
import betterwithmods.common.registry.bulk.recipes.BulkRecipe;
import com.blamejared.mtlib.helpers.LogHelper;
import com.blamejared.mtlib.utils.BaseAction;
import net.minecraft.item.ItemStack;


public class BulkRemove<T extends BulkRecipe> extends BaseAction {
    
    private CraftingManagerBulk<T> manager;
    private final ItemStack output;
    private final ItemStack secondary;
    private final Object[] inputs;
    
    public BulkRemove(String name, CraftingManagerBulk<T> manager, ItemStack output, ItemStack secondary, Object... inputs) {
        super(name);
        this.manager = manager;
        this.output = output;
        this.secondary = secondary;
        this.inputs = inputs;
    }
    
    @Override
    public void apply() {
        for(T recipe : manager.findRecipeForRemoval(output, secondary, inputs)) {
            if(recipe != null) {
                if(!manager.getRecipes().remove(recipe)) {
                    LogHelper.logError(String.format("Error removing %s Recipe for %s", name, getRecipeInfo(recipe)));
                }
            } else {
                LogHelper.logError(String.format("Error removing %s Recipe: null object", name));
            }
        }
        
    }
    
    private String getRecipeInfo(T recipe) {
        return recipe.getOutput().getDisplayName();
    }
}