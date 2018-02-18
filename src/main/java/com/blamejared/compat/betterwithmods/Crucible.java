package com.blamejared.compat.betterwithmods;


import betterwithmods.common.registry.bulk.manager.CrucibleManager;
import betterwithmods.common.registry.bulk.recipes.CrucibleRecipe;
import com.blamejared.ModTweaker;
import com.blamejared.compat.betterwithmods.util.*;
import com.blamejared.mtlib.helpers.InputHelper;
import crafttweaker.annotations.*;
import crafttweaker.api.item.*;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.*;

@ZenClass("mods.betterwithmods.Crucible")
@ModOnly("betterwithmods")
@ZenRegister
public class Crucible {
    
    @ZenMethod
    public static void add(IItemStack output, @NotNull IIngredient[] inputs, @Optional IItemStack secondaryOutput) {
        CrucibleRecipe r = new CrucibleRecipe(InputHelper.toStack(output), InputHelper.toStack(secondaryOutput), InputHelper.toObjects(inputs));
        ModTweaker.LATE_ADDITIONS.add(new BulkAdd("Set Crucible Recipe", CrucibleManager.getInstance(), r));
    }
    
    @Deprecated
    @ZenMethod
    public static void add(IItemStack output, @Optional IItemStack secondaryOutput, @NotNull IIngredient[] inputs) {
        add(output, inputs, secondaryOutput);
    }
    
    @ZenMethod
    public static void remove(IItemStack output) {
        ModTweaker.LATE_REMOVALS.add(new BulkRemove("Set Crucible Recipe", CrucibleManager.getInstance(), InputHelper.toStack(output), ItemStack.EMPTY));
    }
    
    @ZenMethod
    public static void remove(IItemStack output, IItemStack secondary, IIngredient[] inputs) {
        ModTweaker.LATE_REMOVALS.add(new BulkRemove("Remove Crucible Recipe", CrucibleManager.getInstance(), InputHelper.toStack(output), secondary != null ? InputHelper.toStack(secondary) : ItemStack.EMPTY, InputHelper.toObjects(inputs)));
    }
}
