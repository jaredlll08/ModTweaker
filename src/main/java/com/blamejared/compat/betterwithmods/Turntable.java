package com.blamejared.compat.betterwithmods;

import betterwithmods.common.BWRegistry;
import com.blamejared.compat.betterwithmods.base.blockrecipes.TurntableBuilder;
import com.blamejared.mtlib.helpers.LogHelper;
import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;


@ZenClass("mods.betterwithmods.Turntable")
@ModOnly("betterwithmods")
@ZenRegister
public class Turntable {

    public static TurntableBuilder INSTANCE = new TurntableBuilder(BWRegistry.TURNTABLE, "Turntable");

    @ZenMethod
    public static TurntableBuilder builder() {
        return INSTANCE;
    }

    @ZenMethod
    public static void add(IItemStack inputBlock, IItemStack outputBlock, IItemStack[] additionalOutput) {
        builder().buildRecipe(inputBlock, additionalOutput).setProductState(outputBlock);
    }


    @Deprecated
    @ZenMethod
    public static void remove(IItemStack input) {
        LogHelper.logError("This operation has been removed, use mods.betterwithmods.Saw.remove(IItemStack[] outputs)");
    }

    @ZenMethod
    public static void remove(IItemStack[] outputs) {
        INSTANCE.removeRecipe(outputs);
    }

}
