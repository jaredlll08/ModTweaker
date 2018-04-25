package com.blamejared.compat.thaumcraft.handlers.handlers;

import com.blamejared.ModTweaker;
import com.blamejared.mtlib.helpers.*;
import com.blamejared.mtlib.utils.BaseAction;
import crafttweaker.annotations.*;
import crafttweaker.api.item.IItemStack;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.*;
import thaumcraft.api.ThaumcraftApi;

@ZenClass("mods.thaumcraft.Warp")
@ZenRegister
@ModOnly("thaumcraft")
public class Warp {
    
    /**
     * Warp to gain on craft
     * @param stack
     * @param amount
     */
    @ZenMethod
    public static void setWarp(IItemStack stack, int amount) {
        ModTweaker.LATE_ADDITIONS.add(new Set(InputHelper.toStack(stack), amount));
    }
    
    
    private static class Set extends BaseAction {
        
        private ItemStack stack;
        private int amount;
        
        public Set(ItemStack stack, int amount) {
            super("Warp");
            this.stack = stack;
            this.amount = amount;
        }
        
        @Override
        public void apply() {
            ThaumcraftApi.addWarpToItem(stack, amount);
        }
        
        
        @Override
        public String describe() {
            return "Adding " + amount + " warp to item: " + getRecipeInfo();
        }
        
        
        @Override
        protected String getRecipeInfo() {
            return LogHelper.getStackDescription(stack);
        }
    }
    
    
}
