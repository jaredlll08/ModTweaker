package com.blamejared.compat.thermalexpansion.dynamos;

import cofh.thermalexpansion.util.managers.dynamo.ReactantManager;
import com.blamejared.ModTweaker;
import com.blamejared.mtlib.helpers.*;
import com.blamejared.mtlib.utils.BaseAction;
import crafttweaker.annotations.*;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.liquid.ILiquidStack;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import stanhebben.zenscript.annotations.*;

@ZenClass("mods.thermalexpansion.ReactantDynamo")
@ModOnly("thermalexpansion")
@ZenRegister
public class ReactantDynamo {
    
    @ZenMethod
    public static void addReaction(IItemStack stack, ILiquidStack fluid, int energy) {
        ModTweaker.LATE_ADDITIONS.add(new Add(InputHelper.toStack(stack), InputHelper.toFluid(fluid).getFluid(), energy));
    }
    
    @ZenMethod
    public static void addReactionElemental(IItemStack stack, ILiquidStack fluid, int energy) {
        ModTweaker.LATE_ADDITIONS.add(new AddElemental(InputHelper.toStack(stack), InputHelper.toFluid(fluid).getFluid(), energy));
    }
    
    @ZenMethod
    public static void removeReaction(IItemStack stack, ILiquidStack fluid) {
        ModTweaker.LATE_REMOVALS.add(new Remove(InputHelper.toStack(stack), InputHelper.toFluid(fluid).getFluid()));
    }
    
    @ZenMethod
    public static void removeReactionElemental(IItemStack stack, ILiquidStack fluid) {
        ModTweaker.LATE_REMOVALS.add(new RemoveElemental(InputHelper.toStack(stack), InputHelper.toFluid(fluid).getFluid()));
    }
    
    private static class Add extends BaseAction {
        
        private ItemStack stack;
        private Fluid fluid;
        private int energy;
        
        protected Add(ItemStack stack, Fluid fluid, int energy) {
            super("ReactantDynamo");
            this.stack = stack;
            this.fluid = fluid;
            this.energy = energy;
        }
        
        @Override
        public void apply() {
            ReactantManager.addReaction(stack, fluid, energy);
        }
        
        @Override
        protected String getRecipeInfo() {
            return LogHelper.getStackDescription(stack) + " " + LogHelper.getStackDescription(fluid);
        }
    }
    
    private static class Remove extends BaseAction {
        
        private ItemStack stack;
        private Fluid fluid;
        
        protected Remove(ItemStack stack, Fluid fluid) {
            super("ReactantDynamo");
            this.stack = stack;
            this.fluid = fluid;
        }
        
        @Override
        public void apply() {
            ReactantManager.removeReaction(stack, fluid);
        }
        
        @Override
        protected String getRecipeInfo() {
            return LogHelper.getStackDescription(stack) + " " + LogHelper.getStackDescription(fluid);
        }
    }
    
    private static class AddElemental extends BaseAction {
        
        private ItemStack stack;
        private Fluid fluid;
        private int energy;
        
        protected AddElemental(ItemStack stack, Fluid fluid, int energy) {
            super("ReactantDynamoElemental");
            this.stack = stack;
            this.fluid = fluid;
            this.energy = energy;
        }
        
        @Override
        public void apply() {
            ReactantManager.addElementalReaction(stack, fluid, energy);
        }
        
        @Override
        protected String getRecipeInfo() {
            return LogHelper.getStackDescription(stack) + " " + LogHelper.getStackDescription(fluid);
        }
    }
    
    private static class RemoveElemental extends BaseAction {
        
        private ItemStack stack;
        private Fluid fluid;
        
        protected RemoveElemental(ItemStack stack, Fluid fluid) {
            super("ReactantDynamo");
            this.stack = stack;
            this.fluid = fluid;
        }
        
        @Override
        public void apply() {
            ReactantManager.removeElementalReaction(stack, fluid);
        }
        
        @Override
        protected String getRecipeInfo() {
            return LogHelper.getStackDescription(stack) + " " + LogHelper.getStackDescription(fluid);
        }
    }
    
}
