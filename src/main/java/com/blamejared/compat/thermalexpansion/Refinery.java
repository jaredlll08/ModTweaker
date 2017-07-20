package com.blamejared.compat.thermalexpansion;

import cofh.thermalexpansion.util.managers.machine.*;
import com.blamejared.ModTweaker;
import com.blamejared.api.annotations.Handler;
import com.blamejared.mtlib.helpers.*;
import com.blamejared.mtlib.utils.BaseUndoable;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.liquid.ILiquidStack;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import stanhebben.zenscript.annotations.*;

@ZenClass("mods.thermalexpansion.Refinery")
@Handler("thermalexpansion")
public class Refinery {
    
    @ZenMethod
    public static void addRecipe(ILiquidStack output, IItemStack outputItem, ILiquidStack input, int energy) {
        ModTweaker.LATE_ADDITIONS.add(new Add(InputHelper.toFluid(output), InputHelper.toFluid(input), InputHelper.toStack(outputItem), energy));
    }
    
    @ZenMethod
    public static void removeRecipe(ILiquidStack input) {
        ModTweaker.LATE_REMOVALS.add(new Remove(InputHelper.toFluid(input)));
    }
    
    private static class Add extends BaseUndoable {
        
        private FluidStack output, input;
        private ItemStack outputItem;
        private int energy;
        
        public Add(FluidStack output, FluidStack input, ItemStack outputItem, int energy) {
            super("Refinery");
            this.output = output;
            this.input = input;
            this.outputItem = outputItem;
            this.energy = energy;
        }
        
        @Override
        public void apply() {
            RefineryManager.addRecipe(energy, input, output, outputItem);
        }
        
        @Override
        protected String getRecipeInfo() {
            return LogHelper.getStackDescription(output);
        }
    }
    
    private static class Remove extends BaseUndoable {
        
        private FluidStack input;
        
        public Remove(FluidStack input) {
            super("Refinery");
            this.input = input;
        }
        
        @Override
        public void apply() {
            RefineryManager.removeRecipe(input);
        }
        
        @Override
        protected String getRecipeInfo() {
            return LogHelper.getStackDescription(input);
        }
    }
}
