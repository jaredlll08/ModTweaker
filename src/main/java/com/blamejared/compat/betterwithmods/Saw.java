package com.blamejared.compat.betterwithmods;


import betterwithmods.common.registry.blockmeta.managers.SawManager;
import betterwithmods.common.registry.blockmeta.recipe.SawRecipe;
import com.blamejared.ModTweaker;
import com.blamejared.api.annotations.Handler;
import com.blamejared.compat.betterwithmods.util.BMAdd;
import com.blamejared.compat.betterwithmods.util.BMRemove;
import com.blamejared.mtlib.helpers.InputHelper;
import com.google.common.collect.Lists;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.*;
import crafttweaker.api.item.IItemStack;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.NotNull;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.Arrays;


@ZenClass("mods.betterwithmods.Saw")
@ModOnly("betterwithmods")
@ZenRegister
public class Saw {
    
    @ZenMethod
    public static void add(IItemStack[] output, @NotNull IItemStack input) {
        ItemStack stack = InputHelper.toStack(input);
        if(InputHelper.isABlock(stack)) {
            Block block = ((ItemBlock) stack.getItem()).getBlock();
            ItemStack[] outputs = InputHelper.toStacks(output);
            SawRecipe r = new SawRecipe(block, stack.getMetadata(), Arrays.asList(outputs));
            ModTweaker.LATE_ADDITIONS.add(new BMAdd("Set Saw Recipe", SawManager.INSTANCE, Lists.newArrayList(r)));
        }
    }
    
    @ZenMethod
    public static void remove(IItemStack input) {
        ModTweaker.LATE_REMOVALS.add(new BMRemove("Remove Saw Recipe", SawManager.INSTANCE, InputHelper.toStack(input)));
    }
    
}
