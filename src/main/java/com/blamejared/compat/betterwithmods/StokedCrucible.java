package com.blamejared.compat.betterwithmods;


import com.blamejared.compat.betterwithmods.base.bulkrecipes.CookingPotBuilder;
import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import stanhebben.zenscript.annotations.NotNull;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@Deprecated
@ZenClass("mods.betterwithmods.StokedCrucible")
@ModOnly("betterwithmods")
@ZenRegister
public class StokedCrucible {

    @Deprecated
    @ZenMethod
    public static void add(IItemStack output, IItemStack secondaryOutput, @NotNull IIngredient[] inputs) {
        Crucible.INSTANCE.buildRecipe(inputs,new IItemStack[]{output,secondaryOutput}).setHeat(CookingPotBuilder.STOKED).build();
    }

    @Deprecated
    @ZenMethod
    public static void add(IItemStack output, @NotNull IIngredient[] inputs) {
        Crucible.INSTANCE.buildRecipe(inputs,new IItemStack[]{output}).setHeat(CookingPotBuilder.STOKED).build();
    }

    @Deprecated
    @ZenMethod
    public static void remove(IItemStack[] output) {
        Crucible.remove(output);
    }

}
