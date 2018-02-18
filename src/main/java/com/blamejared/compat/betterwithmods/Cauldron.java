package com.blamejared.compat.betterwithmods;


import betterwithmods.common.registry.bulk.manager.CauldronManager;
import betterwithmods.common.registry.bulk.recipes.CauldronRecipe;
import com.blamejared.ModTweaker;
import com.blamejared.compat.betterwithmods.util.*;
import com.blamejared.mtlib.helpers.InputHelper;
import crafttweaker.annotations.*;
import crafttweaker.api.item.*;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.*;

@ZenClass("mods.betterwithmods.Cauldron")
@ModOnly("betterwithmods")
@ZenRegister
public class Cauldron {
    
    @ZenMethod
    public static void add(IItemStack output, @NotNull IIngredient[] inputs, @Optional IItemStack secondaryOutput) {
        CauldronRecipe r = new CauldronRecipe(InputHelper.toStack(output), InputHelper.toStack(secondaryOutput), InputHelper.toObjects(inputs));
        ModTweaker.LATE_ADDITIONS.add(new BulkAdd("Set Cauldron Recipe", CauldronManager.getInstance(), r));
    }
    
    @ZenMethod
    @Deprecated
    public static void add(IItemStack output, @Optional IItemStack secondaryOutput, @NotNull IIngredient[] inputs) {
        add(output, inputs, secondaryOutput);
    }
    
    @ZenMethod
    public static void remove(IItemStack output) {
        ModTweaker.LATE_REMOVALS.add(new BulkRemove("Set Cauldron Recipe", CauldronManager.getInstance(), InputHelper.toStack(output), ItemStack.EMPTY));
    }
    
    @ZenMethod
    @Deprecated
    public static void remove(IItemStack output, @Optional IItemStack secondary, IIngredient[] inputs) {
        remove(output, inputs, secondary);
    }
    
    @ZenMethod
    public static void remove(IItemStack output, IIngredient[] inputs, @Optional IItemStack secondary) {
        ModTweaker.LATE_REMOVALS.add(new BulkRemove("Remove Cauldron Recipe", CauldronManager.getInstance(), InputHelper.toStack(output), secondary != null ? InputHelper.toStack(secondary) : ItemStack.EMPTY, InputHelper.toObjects(inputs)));
    }
    
}
