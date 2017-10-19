package com.blamejared.compat.thermalexpansion;

import cofh.thermalexpansion.util.managers.machine.*;
import com.blamejared.ModTweaker;
import com.blamejared.mtlib.helpers.*;
import com.blamejared.mtlib.utils.BaseUndoable;
import crafttweaker.annotations.*;
import crafttweaker.api.item.*;
import crafttweaker.api.liquid.ILiquidStack;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import stanhebben.zenscript.annotations.*;

@ZenClass("mods.thermalexpansion.Transposer")
@ModOnly("thermalexpansion")
@ZenRegister
public class Transposer {
    
    @ZenMethod
    public static void addExtractRecipe(ILiquidStack output, IItemStack input, int energy, @Optional WeightedItemStack itemOut) {
        ItemStack stack = ItemStack.EMPTY;
        int percent = 0;
        if(itemOut != null) {
            stack = InputHelper.toStack(itemOut.getStack());
            percent = (int) itemOut.getPercent();
        }
        
        ModTweaker.LATE_ADDITIONS.add(new AddExtract(InputHelper.toFluid(output), InputHelper.toStack(input), energy, stack, percent));
    }
    
    @ZenMethod
    public static void removeExtractRecipe(IItemStack input) {
        ModTweaker.LATE_REMOVALS.add(new RemoveExtract(InputHelper.toStack(input)));
    }
    
    @ZenMethod
    public static void addFillRecipe(IItemStack output, IItemStack input, ILiquidStack fluid, int energy) {
        ModTweaker.LATE_ADDITIONS.add(new AddFill(InputHelper.toFluid(fluid), InputHelper.toStack(input), energy, InputHelper.toStack(output)));
    }
    
    @ZenMethod
    public static void removeFillRecipe(IItemStack input, ILiquidStack fluid) {
        ModTweaker.LATE_REMOVALS.add(new RemoveFill(InputHelper.toStack(input), InputHelper.toFluid(fluid)));
    }
    
    private static class AddExtract extends BaseUndoable {
        
        private FluidStack output;
        private ItemStack input;
        private int energy;
        
        private ItemStack itemOut;
        private int chance;
        
        public AddExtract(FluidStack output, ItemStack input, int energy, ItemStack itemOut, int chance) {
            super("Transposer Extract");
            this.output = output;
            this.input = input;
            this.energy = energy;
            this.itemOut = itemOut;
            this.chance = chance;
        }
        
        @Override
        public void apply() {
            TransposerManager.addExtractRecipe(energy, input, itemOut, output, chance, false);
        }
        
        @Override
        protected String getRecipeInfo() {
            return LogHelper.getStackDescription(output);
        }
    }
    
    private static class RemoveExtract extends BaseUndoable {
        
        private ItemStack input;
        
        public RemoveExtract(ItemStack input) {
            super("Transposer Extract");
            this.input = input;
        }
        
        @Override
        public void apply() {
            TransposerManager.removeExtractRecipe(input);
        }
        
        @Override
        protected String getRecipeInfo() {
            return LogHelper.getStackDescription(input);
        }
    }
    
    private static class AddFill extends BaseUndoable {
        
        private FluidStack fluid;
        private ItemStack input;
        private int energy;
        
        private ItemStack output;
        
        public AddFill(FluidStack output, ItemStack input, int energy, ItemStack itemOut) {
            super("Transposer Fill");
            this.fluid = output;
            this.input = input;
            this.energy = energy;
            this.output = itemOut;
        }
        
        @Override
        public void apply() {
            TransposerManager.addFillRecipe(energy, input, output, fluid, false);
        }
        
        @Override
        protected String getRecipeInfo() {
            return LogHelper.getStackDescription(fluid);
        }
    }
    
    private static class RemoveFill extends BaseUndoable {
        
        private ItemStack input;
        private FluidStack fluid;
        
        public RemoveFill(ItemStack input, FluidStack fluid) {
            super("Transposer Fill");
            this.input = input;
            this.fluid = fluid;
        }
        
        @Override
        public void apply() {
            TransposerManager.removeFillRecipe(input, fluid);
        }
        
        @Override
        protected String getRecipeInfo() {
            return LogHelper.getStackDescription(input);
        }
    }
    
}
