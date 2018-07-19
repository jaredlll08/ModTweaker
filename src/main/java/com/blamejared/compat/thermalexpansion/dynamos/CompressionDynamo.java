package com.blamejared.compat.thermalexpansion.dynamos;

import cofh.thermalexpansion.util.managers.dynamo.CompressionManager;
import com.blamejared.ModTweaker;
import com.blamejared.mtlib.helpers.*;
import com.blamejared.mtlib.utils.BaseAction;
import crafttweaker.annotations.*;
import crafttweaker.api.liquid.ILiquidStack;
import net.minecraftforge.fluids.FluidStack;
import stanhebben.zenscript.annotations.*;

@ZenClass("mods.thermalexpansion.CompressionDynamo")
@ModOnly("thermalexpansion")
@ZenRegister
public class CompressionDynamo {
    
    @ZenMethod
    public static void addFuel(ILiquidStack stack, int energy) {
        ModTweaker.LATE_ADDITIONS.add(new Add(InputHelper.toFluid(stack), energy));
    }
    
    @ZenMethod
    public static void removeFuel(ILiquidStack stack) {
        ModTweaker.LATE_REMOVALS.add(new Remove(InputHelper.toFluid(stack)));
    }
    
    
    private static class Add extends BaseAction {
        
        private FluidStack stack;
        private int energy;
        
        protected Add(FluidStack stack, int energy) {
            super("CompressionDynamo");
            this.stack = stack;
            this.energy = energy;
        }
        
        @Override
        public void apply() {
            CompressionManager.addFuel(stack.getFluid().getName(), energy);
        }
        
        @Override
        protected String getRecipeInfo() {
            return LogHelper.getStackDescription(stack);
        }
    }
    
    private static class Remove extends BaseAction {
        
        private FluidStack stack;
        
        protected Remove(FluidStack stack) {
            super("CompressionDynamo");
            this.stack = stack;
        }
        
        @Override
        public void apply() {
            CompressionManager.removeFuel(stack.getFluid().getName());
        }
        
        @Override
        protected String getRecipeInfo() {
            return LogHelper.getStackDescription(stack);
        }
    }
    
    
}
