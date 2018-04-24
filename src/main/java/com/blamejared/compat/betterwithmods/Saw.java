package com.blamejared.compat.betterwithmods;


import betterwithmods.common.BWRegistry;
import com.blamejared.compat.betterwithmods.base.blockrecipes.SawBuilder;
import com.blamejared.mtlib.helpers.LogHelper;
import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import stanhebben.zenscript.annotations.NotNull;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import javax.annotation.Nonnull;


@ZenClass("mods.betterwithmods.Saw")
@ModOnly("betterwithmods")
@ZenRegister
public class Saw {

    public static SawBuilder INSTANCE = new SawBuilder(() -> BWRegistry.WOOD_SAW, "Saw");

    @ZenMethod
    public static SawBuilder builder() {
        return INSTANCE;
    }

    @ZenMethod
    public static void add(@Nonnull IIngredient input, IItemStack[] output) {
        builder().buildRecipe(input, output).build();
    }

    @Deprecated
    @ZenMethod
    public static void add(IItemStack[] output, @NotNull IIngredient input) {
        add(input, output);
    }

    @ZenMethod
    public static void remove(IItemStack input) {
        INSTANCE.removeRecipe(input);
    }

    @ZenMethod
    public static void remove(IItemStack[] outputs) {
        INSTANCE.removeRecipe(outputs);
    }

    @ZenMethod
    public static void removeAll() {
        builder().removeAll();
    }
}
