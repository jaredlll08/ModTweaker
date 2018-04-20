package com.blamejared.compat.betterwithmods;

import betterwithmods.common.BWMRecipes;
import betterwithmods.common.BWRegistry;
import com.blamejared.compat.betterwithmods.base.blockrecipes.TurntableBuilder;
import com.blamejared.mtlib.helpers.LogHelper;
import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;


@ZenClass("mods.betterwithmods.Turntable")
@ModOnly("betterwithmods")
@ZenRegister
public class Turntable {

    public static TurntableBuilder INSTANCE = new TurntableBuilder(() -> BWRegistry.TURNTABLE, "Turntable");

    @ZenMethod
    public static TurntableBuilder builder() {
        return INSTANCE;
    }

    @ZenMethod
    public static void add(IIngredient inputBlock, IItemStack[] additionalOutput) {
        builder().buildRecipe(inputBlock, additionalOutput).build();
    }

    @ZenMethod
    public static void add(IIngredient inputBlock, IItemStack productState, IItemStack[] additionalOutput) {
        builder().buildRecipe(inputBlock, additionalOutput).setProductState(productState).build();
    }

    @ZenMethod
    public static void remove(IItemStack input) {
        INSTANCE.removeRecipe(input);
    }

    @ZenMethod
    public static void removeByProductState(IItemStack output) {
        INSTANCE.removeRecipe(BWMRecipes.getStateFromStack(CraftTweakerMC.getItemStack(output)));
    }

    @ZenMethod
    public static void removeAll() {
        builder().removeAll();
    }
}
