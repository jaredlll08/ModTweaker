package com.blamejared.compat.betterwithmods;


import betterwithmods.common.registry.bulk.manager.CrucibleManager;
import betterwithmods.common.registry.bulk.recipes.CrucibleRecipe;
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

@ZenClass("mods.betterwithmods.Crucible")
@Handler("betterwithmods")
public class Crucible {

    @ZenMethod
    public static void add(IItemStack output, @Optional IItemStack secondaryOutput, @NotNull IIngredient[] inputs) {
        CrucibleRecipe r = new CrucibleRecipe(InputHelper.toStack(output), InputHelper.toStack(secondaryOutput), InputHelper.toObjects(inputs));
        CraftTweakerAPI.apply(new BulkAdd("Set Crucible Recipe", CrucibleManager.getInstance(), r));
    }

    @ZenMethod
    public static void remove(IItemStack output) {
        CraftTweakerAPI.apply(new BulkRemove("Set Crucible Recipe", CrucibleManager.getInstance(), InputHelper.toStack(output), ItemStack.EMPTY));
    }

    @ZenMethod
    public static void remove(IItemStack output, IItemStack secondary, IIngredient[] inputs) {
        CraftTweakerAPI.apply(new BulkRemove("Remove Crucible Recipe", CrucibleManager.getInstance(), InputHelper.toStack(output), secondary != null ? InputHelper.toStack(secondary) : ItemStack.EMPTY, InputHelper.toObjects(inputs)));
    }
}
