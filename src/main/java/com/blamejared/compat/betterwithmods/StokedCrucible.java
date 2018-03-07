package com.blamejared.compat.betterwithmods;


import betterwithmods.common.registry.bulk.manager.StokedCrucibleManager;
import betterwithmods.common.registry.bulk.recipes.StokedCrucibleRecipe;
import com.blamejared.ModTweaker;
import com.blamejared.compat.betterwithmods.util.*;
import com.blamejared.mtlib.helpers.InputHelper;
import crafttweaker.annotations.*;
import crafttweaker.api.item.*;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.*;

@ZenClass("mods.betterwithmods.StokedCrucible")
@ModOnly("betterwithmods")
@ZenRegister
public class StokedCrucible {
    
    @ZenMethod
    public static void add(IItemStack output, @NotNull IIngredient[] inputs, @Optional IItemStack secondaryOutput) {
        StokedCrucibleRecipe r = new StokedCrucibleRecipe(InputHelper.toStack(output), InputHelper.toStack(secondaryOutput), InputHelper.toObjects(inputs));
        ModTweaker.LATE_ADDITIONS.add(new BulkAdd("Set Stoked Crucible Recipe", StokedCrucibleManager.getInstance(), r));
    }
    
    @ZenMethod
    @Deprecated
    public static void add(IItemStack output, IItemStack secondaryOutput, @NotNull IIngredient[] inputs) {
        add(output, inputs, secondaryOutput);
    }
    
    @ZenMethod
    public static void remove(IItemStack output) {
        ModTweaker.LATE_REMOVALS.add(new BulkRemove("Remove Stoked Crucible Recipe", StokedCrucibleManager.getInstance(), InputHelper.toStack(output), ItemStack.EMPTY));
    }
    
    @ZenMethod
    public static void remove(IItemStack output, IItemStack secondary) {
        ModTweaker.LATE_REMOVALS.add(new BulkRemove("Remove Stoked Crucible Recipe", StokedCrucibleManager.getInstance(), InputHelper.toStack(output), secondary != null ? InputHelper.toStack(secondary) : ItemStack.EMPTY));
    }
    
    @ZenMethod
    public static void remove(IItemStack output, @NotNull IIngredient[] inputs, IItemStack secondary) {
        ModTweaker.LATE_REMOVALS.add(new BulkRemove("Remove Stoked Crucible Recipe", StokedCrucibleManager.getInstance(), InputHelper.toStack(output), secondary != null ? InputHelper.toStack(secondary) : ItemStack.EMPTY, InputHelper.toObjects(inputs)));
    }
    
    @ZenMethod
    @Deprecated
    public static void remove(IItemStack output, IItemStack secondary, @NotNull IIngredient[] inputs) {
        remove(output, inputs, secondary);
    }
}
