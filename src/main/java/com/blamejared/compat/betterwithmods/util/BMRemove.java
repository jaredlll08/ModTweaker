package com.blamejared.compat.betterwithmods.util;

import betterwithmods.common.registry.blockmeta.managers.BlockMetaManager;
import betterwithmods.common.registry.blockmeta.recipe.BlockMetaRecipe;
import com.blamejared.mtlib.helpers.LogHelper;
import com.blamejared.mtlib.utils.BaseUndoable;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.item.ItemStack;

import java.util.*;

public class BMRemove extends BaseUndoable {

    private final BlockMetaManager recipes;
    private final ItemStack input;

    public BMRemove(String name, BlockMetaManager recipes, ItemStack input) {
        super(name);
        this.recipes = recipes;
        this.input = input;
    }
    
    @Override
    protected String getRecipeInfo() {
        return LogHelper.getStackDescription(input);
    }
    
    @Override
    public void apply() {
        List<BlockMetaRecipe> removed = new ArrayList<>();
        for(BlockMetaRecipe o : (List<BlockMetaRecipe>) recipes.getRecipes()) {
            if(CraftTweakerMC.getIItemStack(input).matches(CraftTweakerMC.getIItemStack(o.getStack()))){
                removed.add(o);
            }
        }
        recipes.getRecipes().removeAll(removed);
    }
}