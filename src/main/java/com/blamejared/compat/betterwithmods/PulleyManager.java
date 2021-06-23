package com.blamejared.compat.betterwithmods;

import betterwithmods.common.registry.PulleyStructureManager;
import com.blamejared.mtlib.utils.BaseAction;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.block.state.IBlockState;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.betterwithmods.PulleyManager")
@ModOnly("betterwithmods")
@ZenRegister
public class PulleyManager {

    @ZenMethod
    public static void addPulleyBlock(crafttweaker.api.block.IBlockState state) {
        CraftTweakerAPI.apply(new AddPulleyBlock(CraftTweakerMC.getBlockState(state)));
    }

    public static class AddPulleyBlock extends BaseAction {

        private IBlockState state;

        AddPulleyBlock(IBlockState state) {
            super("Add Pulley Block");
            this.state = state;
        }

        @Override
        public void apply() {
            PulleyStructureManager.registerPulleyBlock(state);
        }
    }
}
