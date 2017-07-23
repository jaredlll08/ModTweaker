package com.blamejared.compat.betterwithmods;

import betterwithmods.common.BWMRecipes;
import betterwithmods.module.hardcore.HCPiles;
import com.blamejared.ModTweaker;
import com.blamejared.api.annotations.Handler;
import com.blamejared.mtlib.helpers.InputHelper;
import com.blamejared.mtlib.utils.BaseMapAddition;
import com.google.common.collect.Maps;
import crafttweaker.api.item.IItemStack;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.HashMap;
import java.util.Map;

@ZenClass("mods.betterwithmods.Piles")
@Handler("betterwithmods")
public class Piles {

    @ZenMethod
    public static void addPile(IItemStack block, IItemStack stack) {
        ItemStack a = InputHelper.toStack(block);
        IBlockState state;
        if (InputHelper.isABlock(block)) {
            state = BWMRecipes.getStateFromStack(a);
            HashMap<IBlockState, ItemStack> map = Maps.newHashMap();
            map.put(state, InputHelper.toStack(stack));
            ModTweaker.LATE_ADDITIONS.add(new AddPile(map));
        }
    }

    public static class AddPile extends BaseMapAddition<IBlockState, ItemStack> {
        public AddPile(HashMap<IBlockState, ItemStack> map) {
            super("piles", HCPiles.blockStateToPile, map);
        }

        @Override
        protected String getRecipeInfo(Map.Entry<IBlockState, ItemStack> recipe) {
            return recipe.getKey().getBlock().getLocalizedName();
        }
    }
}
