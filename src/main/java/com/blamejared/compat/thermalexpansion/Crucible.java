package com.blamejared.compat.thermalexpansion;

import cofh.thermalexpansion.util.managers.machine.*;
import com.blamejared.ModTweaker;
import com.blamejared.api.annotations.Handler;
import com.blamejared.mtlib.helpers.*;
import com.blamejared.mtlib.utils.BaseUndoable;
import crafttweaker.annotations.*;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.liquid.ILiquidStack;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import stanhebben.zenscript.annotations.*;

@ZenClass("mods.thermalexpansion.Crucible")
@ModOnly("thermalexpansion")
@ZenRegister
public class Crucible {
    
    @ZenMethod
    public static void addRecipe(ILiquidStack output, IItemStack input, int energy) {
        ModTweaker.LATE_ADDITIONS.add(new Add(InputHelper.toFluid(output), InputHelper.toStack(input), energy));
    }
    
    @ZenMethod
    public static void removeRecipe(IItemStack input) {
        ModTweaker.LATE_REMOVALS.add(new Remove(InputHelper.toStack(input)));
    }
    
    private static class Add extends BaseUndoable {
        
        private FluidStack output;
        private ItemStack input;
        private int energy;
        
        public Add(FluidStack output, ItemStack input, int energy) {
            super("Crucible");
            this.output = output;
            this.input = input;
            this.energy = energy;
        }
        
        @Override
        public void apply() {
            CrucibleManager.addRecipe(energy, input, output);
        }
        
        @Override
        protected String getRecipeInfo() {
            return LogHelper.getStackDescription(output);
        }
    }
    
    private static class Remove extends BaseUndoable {
        
        private ItemStack input;
        
        public Remove(ItemStack input) {
            super("Crucible");
            this.input = input;
        }
        
        @Override
        public void apply() {
            CrucibleManager.removeRecipe(input);
        }
        
        @Override
        protected String getRecipeInfo() {
            return LogHelper.getStackDescription(input);
        }
    }
}
