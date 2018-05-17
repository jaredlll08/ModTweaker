package com.blamejared.compat.betterwithmods;

import betterwithmods.common.registry.block.recipe.BlockIngredient;
import betterwithmods.common.registry.heat.BWMHeatRegistry;
import com.blamejared.mtlib.helpers.InputHelper;
import com.blamejared.mtlib.helpers.LogHelper;
import com.blamejared.mtlib.utils.BaseAction;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.betterwithmods.HeatRegistry")
@ModOnly("betterwithmods")
@ZenRegister
public class HeatRegistry {

    @ZenMethod
    public static void addHeatSource(crafttweaker.api.block.IBlockState state, int heat) {
        CraftTweakerAPI.apply(new AddHeatSource(CraftTweakerMC.getBlockState(state), heat));
    }

    @ZenMethod
    public static void addHeatSource(IItemStack stack, int heat) {
        if (InputHelper.isABlock(stack)) {
            Block block = CraftTweakerMC.getBlock(stack);
            IBlockState state = block.getStateFromMeta(stack.getMetadata());
            CraftTweakerAPI.apply(new AddHeatSource(state, heat));
        } else {
            LogHelper.logError(String.format("%s input must create a valid BlockState", stack.getDisplayName()), new IllegalArgumentException(String.format("%s input must create a valid BlockState", stack.getDisplayName())));
        }
    }

    public static class AddHeatSource extends BaseAction {

        private IBlockState state;
        private int heat;

        AddHeatSource(IBlockState state, int heat) {
            super("Add Heat");
            this.state = state;
            this.heat = heat;
        }

        @Override
        public void apply() {
            BWMHeatRegistry.addHeatSource(new BlockIngredient(new ItemStack(state.getBlock())), heat);
        }
    }
}
