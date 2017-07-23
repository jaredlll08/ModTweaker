package com.blamejared.compat.betterwithmods;


import betterwithmods.common.registry.bulk.manager.StokedCauldronManager;
import betterwithmods.common.registry.bulk.recipes.StokedCauldronRecipe;
import com.blamejared.ModTweaker;
import com.blamejared.api.annotations.Handler;
import com.blamejared.compat.betterwithmods.util.BulkAdd;
import com.blamejared.compat.betterwithmods.util.BulkRemove;
import com.blamejared.mtlib.helpers.InputHelper;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.NotNull;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;


@ZenClass("mods.betterwithmods.StokedCauldron")
@Handler("betterwithmods")
public class StokedCauldron {

    @ZenMethod
    public static void add(IItemStack output, @Optional IItemStack secondaryOutput, @NotNull IIngredient[] inputs) {
        StokedCauldronRecipe r = new StokedCauldronRecipe(InputHelper.toStack(output), InputHelper.toStack(secondaryOutput),InputHelper.toObjects(inputs));
        ModTweaker.LATE_ADDITIONS.add(new BulkAdd("Set Stoked Cauldron Recipe", StokedCauldronManager.getInstance(),r));
    }

    @ZenMethod
    public static void remove(IItemStack output) {
        ModTweaker.LATE_ADDITIONS.add(new BulkRemove("Remove Stoked Cauldron Recipe", StokedCauldronManager.getInstance(),InputHelper.toStack(output), ItemStack.EMPTY));
    }

    @ZenMethod
    public static void remove(IItemStack output, IIngredient[] inputs) {
        ModTweaker.LATE_ADDITIONS.add(new BulkRemove("Remove Stoked Cauldron Recipe", StokedCauldronManager.getInstance(),InputHelper.toStack(output), ItemStack.EMPTY, InputHelper.toObjects(inputs)));
    }
}
