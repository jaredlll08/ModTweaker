package com.blamejared.compat.betterwithmods;


import betterwithmods.common.registry.bulk.manager.StokedCrucibleManager;
import betterwithmods.common.registry.bulk.recipes.StokedCrucibleRecipe;
import com.blamejared.ModTweaker;
import com.blamejared.api.annotations.Handler;
import com.blamejared.compat.betterwithmods.util.BulkAdd;
import com.blamejared.compat.betterwithmods.util.BulkRemove;
import com.blamejared.mtlib.helpers.InputHelper;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.NotNull;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.betterwithmods.StokedCrucible")
@Handler("betterwithmods")
public class StokedCrucible {

    @ZenMethod
    public static void add(IItemStack output, @Optional IItemStack secondaryOutput, @NotNull IIngredient[] inputs) {
        StokedCrucibleRecipe r = new StokedCrucibleRecipe(InputHelper.toStack(output), InputHelper.toStack(secondaryOutput), InputHelper.toObjects(inputs));
        CraftTweakerAPI.apply(new BulkAdd("Set Stoked Crucible Recipe", StokedCrucibleManager.getInstance(), r));
    }

    @ZenMethod
    public static void remove(IItemStack output, @Optional IItemStack secondary) {
        CraftTweakerAPI.apply(new BulkRemove("Set Stoked Crucible Recipe", StokedCrucibleManager.getInstance(), InputHelper.toStack(output), secondary != null ? InputHelper.toStack(secondary) : ItemStack.EMPTY));
    }

    @ZenMethod
    public static void remove(IItemStack output, @Optional IItemStack secondary, @NotNull IIngredient[] inputs) {
        CraftTweakerAPI.apply(new BulkRemove("Remove Stoked Crucible Recipe", StokedCrucibleManager.getInstance(), InputHelper.toStack(output), secondary != null ? InputHelper.toStack(secondary) : ItemStack.EMPTY, InputHelper.toObjects(inputs)));
    }
}
