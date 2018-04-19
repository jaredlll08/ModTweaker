package com.blamejared.compat.betterwithmods;


import betterwithmods.common.BWRegistry;
import betterwithmods.common.registry.bulk.manager.CraftingManagerBulk;
import betterwithmods.common.registry.bulk.recipes.CookingPotRecipe;
import com.blamejared.compat.betterwithmods.base.bulkrecipes.CookingPotBuilder;
import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import stanhebben.zenscript.annotations.NotNull;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.function.Supplier;

@ZenClass("mods.betterwithmods.Crucible")
@ModOnly("betterwithmods")
@ZenRegister
public class Crucible {
    public static CookingPotBuilder INSTANCE = new CookingPotBuilder(() -> BWRegistry.CRUCIBLE, "Crucible");

    @ZenMethod
    public static CookingPotBuilder builder() {
        return INSTANCE;
    }

    @ZenMethod
    public static void addStoked(IIngredient[] inputs, IItemStack[] outputs) {
        INSTANCE.buildRecipe(inputs, outputs).setHeat(CookingPotBuilder.STOKED).build();
    }

    @ZenMethod
    public static void addUnstoked(IIngredient[] inputs, IItemStack[] outputs) {
        INSTANCE.buildRecipe(inputs, outputs).setHeat(CookingPotBuilder.UNSTOKED).build();
    }

    @Deprecated
    @ZenMethod
    public static void add(IItemStack output, IItemStack secondaryOutput, @NotNull IIngredient[] inputs) {
        addUnstoked(inputs, new IItemStack[]{output, secondaryOutput});
    }

    @Deprecated
    @ZenMethod
    public static void add(IItemStack output, @NotNull IIngredient[] inputs) {
        addUnstoked(inputs, new IItemStack[]{output});
    }

    @ZenMethod
    public static void remove(IItemStack[] output) {
        INSTANCE.removeRecipe(output);
    }

    @ZenMethod
    public static void removeAll() {
        builder().removeAll();
    }
}
