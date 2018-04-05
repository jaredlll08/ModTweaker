package com.blamejared.compat.betterwithmods;

import betterwithmods.module.hardcore.needs.HCMovement;
import com.blamejared.ModTweaker;
import com.blamejared.mtlib.helpers.InputHelper;
import com.blamejared.mtlib.utils.BaseAction;
import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IItemStack;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.betterwithmods.Movement")
@ModOnly("betterwithmods")
@ZenRegister
public class Movement {
    
    @ZenMethod
    public static void set(IItemStack input, float speed) {
        ItemStack stack = InputHelper.toStack(input);
        if(InputHelper.isABlock(stack)) {
            Block block = ((ItemBlock) stack.getItem()).getBlock();
            IBlockState state = block.getStateFromMeta(stack.getMetadata());
            ModTweaker.LATE_ADDITIONS.add(new Set(state, speed));
        }
    }
    
    public static class Set extends BaseAction {
        
        private IBlockState state;
        private float speed;
        
        protected Set(IBlockState state, float speed) {
            super("Set Block HCMovement");
            this.state = state;
            this.speed = speed;
        }
        
        @Override
        protected String getRecipeInfo() {
            return state.toString() + "->" + speed;
        }
        
        @Override
        public void apply() {
            HCMovement.BLOCK_OVERRIDE_MOVEMENT.put(state, speed);
        }
    }
}
