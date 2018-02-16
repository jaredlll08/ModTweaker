package com.blamejared.compat.betterwithmods;


import betterwithmods.common.registry.bulk.manager.CauldronManager;
import betterwithmods.common.registry.bulk.recipes.CauldronRecipe;
import com.blamejared.ModTweaker;
import com.blamejared.compat.betterwithmods.util.BulkAdd;
import com.blamejared.compat.betterwithmods.util.BulkRemove;
import com.blamejared.mtlib.helpers.InputHelper;
import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.NotNull;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.betterwithmods.Cauldron")
@ModOnly("betterwithmods")
@ZenRegister
public class Cauldron {
    
    @ZenMethod
    public static void add(IItemStack output,  @NotNull IIngredient[] inputs, @Optional IItemStack secondaryOutput) {
        CauldronRecipe r = new CauldronRecipe(InputHelper.toStack(output), InputHelper.toStack(secondaryOutput), InputHelper.toObjects(inputs));
        ModTweaker.LATE_ADDITIONS.add(new BulkAdd("Set Cauldron Recipe", CauldronManager.getInstance(), r));
    }
    
    @ZenMethod
    public static void remove(IItemStack output) {
        ModTweaker.LATE_REMOVALS.add(new BulkRemove("Set Cauldron Recipe", CauldronManager.getInstance(), InputHelper.toStack(output), ItemStack.EMPTY));
    }
    
    @ZenMethod
    public static void remove(IItemStack output, @Optional IItemStack secondary, IIngredient[] inputs) {
        ModTweaker.LATE_REMOVALS.add(new BulkRemove("Remove Cauldron Recipe", CauldronManager.getInstance(), InputHelper.toStack(output), secondary != null ? InputHelper.toStack(secondary) : ItemStack.EMPTY, InputHelper.toObjects(inputs)));
    }
    
}
