package com.blamejared.compat.betterwithmods;

import betterwithmods.common.registry.blockmeta.managers.TurntableManager;
import betterwithmods.common.registry.blockmeta.recipe.TurntableRecipe;
import betterwithmods.module.compat.jei.category.TurntableRecipeCategory;
import com.blamejared.ModTweaker;
import com.blamejared.api.annotations.Handler;
import com.blamejared.compat.betterwithmods.util.BMAdd;
import com.blamejared.mtlib.helpers.InputHelper;
import com.blamejared.mtlib.helpers.LogHelper;
import com.blamejared.mtlib.utils.BaseListRemoval;
import com.blamejared.mtlib.utils.BaseUndoable;
import com.google.common.collect.Lists;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.*;
import crafttweaker.api.item.IItemStack;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.List;


@ZenClass("mods.betterwithmods.Turntable")
@ModOnly("betterwithmods")
@ZenRegister
public class Turntable {
    
    @ZenMethod
    public static void add(IItemStack inputBlock, IItemStack outputBlock, IItemStack[] additionalOutput) {
        if(InputHelper.isABlock(inputBlock))
            LogHelper.logError("Input must be a block", new IllegalArgumentException());
        ModTweaker.LATE_ADDITIONS.add(new Add(InputHelper.toStack(inputBlock), InputHelper.toStack(outputBlock), Lists.newArrayList(InputHelper.toStacks(additionalOutput))));
    }
    
    public static class Add extends BMAdd {
        
        public Add(ItemStack input, ItemStack output, List<ItemStack> scraps) {
            super(TurntableRecipeCategory.UID, TurntableManager.INSTANCE, Lists.newArrayList(new TurntableRecipe(input, output, scraps)));
        }
    }
    
    @ZenMethod
    public static void remove(IItemStack inputBlock) {
        if(!InputHelper.isABlock(inputBlock))
            LogHelper.logError("Input must be a block", new IllegalArgumentException());
        ModTweaker.LATE_REMOVALS.add(new Remove(InputHelper.toStack(inputBlock)));
    }
    
    public static class Remove extends BaseListRemoval<TurntableRecipe> {
        
        protected Remove(ItemStack input) {
            super("Remove Turntable Recipe", TurntableManager.INSTANCE.getRecipes(), TurntableManager.INSTANCE.removeTurntableRecipe(input));
        }
        
        @Override
        protected String getRecipeInfo(TurntableRecipe recipe) {
            return recipe.getStack().getDisplayName();
        }
    }
    
    
}
