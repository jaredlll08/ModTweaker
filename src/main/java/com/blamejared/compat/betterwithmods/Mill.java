package com.blamejared.compat.betterwithmods;


import betterwithmods.common.BWRegistry;
import betterwithmods.common.registry.bulk.manager.CraftingManagerBulk;
import betterwithmods.common.registry.bulk.recipes.MillRecipe;
import com.blamejared.compat.betterwithmods.base.bulkrecipes.MillBuilder;
import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import stanhebben.zenscript.annotations.NotNull;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.function.Supplier;

@ZenClass("mods.betterwithmods.Mill")
@ModOnly("betterwithmods")
@ZenRegister
public class Mill {

    public static MillBuilder INSTANCE = new MillBuilder(() -> BWRegistry.MILLSTONE, "Mill");

    @ZenMethod
    public static MillBuilder builder() {
        return INSTANCE;
    }

    @ZenMethod
    public static void addRecipe(IIngredient[] inputs, IItemStack[] outputs) {
        INSTANCE.buildRecipe(inputs, outputs).build();
    }

    @Deprecated
    @ZenMethod
    public static void add(IItemStack output, IItemStack secondaryOutput, @NotNull IIngredient[] inputs) {
        addRecipe(inputs, new IItemStack[]{output, secondaryOutput});
    }

    @Deprecated
    @ZenMethod
    public static void add(IItemStack output, @NotNull IIngredient[] inputs) {
        addRecipe(inputs, new IItemStack[]{output});
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
