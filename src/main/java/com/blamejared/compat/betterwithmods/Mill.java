package com.blamejared.compat.betterwithmods;

import betterwithmods.common.registry.bulk.manager.MillManager;
import betterwithmods.common.registry.bulk.recipes.MillRecipe;
import com.blamejared.ModTweaker;
import com.blamejared.compat.betterwithmods.util.*;
import com.blamejared.mtlib.helpers.InputHelper;
import crafttweaker.annotations.*;
import crafttweaker.api.item.*;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.*;


@ZenClass("mods.betterwithmods.Mill")
@ModOnly("betterwithmods")
@ZenRegister
public class Mill {
    
    @ZenMethod
    public static void add(IItemStack output, @NotNull IIngredient[] inputs, @Optional IItemStack secondaryOutput) {
        MillRecipe r = new MillRecipe(0, InputHelper.toStack(output), InputHelper.toStack(secondaryOutput), InputHelper.toObjects(inputs));
        ModTweaker.LATE_ADDITIONS.add(new BulkAdd("Set Mill Recipe", MillManager.getInstance(), r));
    }
    
    @ZenMethod
    @Deprecated
    public static void add(IItemStack output, IItemStack secondaryOutput, @NotNull IIngredient[] inputs) {
        add(output, inputs, secondaryOutput);
    }
    
    @ZenMethod
    public static void remove(IItemStack output) {
        ModTweaker.LATE_REMOVALS.add(new BulkRemove("Remove Mill Recipe", MillManager.getInstance(), InputHelper.toStack(output), ItemStack.EMPTY));
    }
    
    @ZenMethod
    public static void remove(IItemStack output, IIngredient[] inputs) {
        ModTweaker.LATE_REMOVALS.add(new BulkRemove("Remove Mill Recipe", MillManager.getInstance(), InputHelper.toStack(output), ItemStack.EMPTY, InputHelper.toObjects(inputs)));
    }
}
