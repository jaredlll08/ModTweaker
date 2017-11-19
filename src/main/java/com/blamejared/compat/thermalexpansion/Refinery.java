package com.blamejared.compat.thermalexpansion;

import cofh.thermalexpansion.util.managers.machine.*;
import com.blamejared.ModTweaker;
import com.blamejared.mtlib.helpers.InputHelper;
import com.blamejared.mtlib.helpers.LogHelper;
import com.blamejared.mtlib.utils.BaseUndoable;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.*;
import crafttweaker.api.liquid.ILiquidStack;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.thermalexpansion.Refinery")
@ModOnly("thermalexpansion")
@ZenRegister
public class Refinery {
    
    @ZenMethod
    public static void addRecipe(ILiquidStack output, WeightedItemStack outputItem, ILiquidStack input, int energy) {
        ModTweaker.LATE_ADDITIONS.add(new Add(InputHelper.toFluid(output), InputHelper.toFluid(input), outputItem, energy));
    }
    
    @ZenMethod
    public static void removeRecipe(ILiquidStack input) {
        ModTweaker.LATE_REMOVALS.add(new Remove(InputHelper.toFluid(input)));
    }
    
    private static class Add extends BaseUndoable {
        
        private FluidStack output, input;
        private WeightedItemStack outputItem;
        private int energy;
        
        public Add(FluidStack output, FluidStack input, WeightedItemStack outputItem, int energy) {
            super("Refinery");
            this.output = output;
            this.input = input;
            this.outputItem = outputItem;
            this.energy = energy;
        }
        
        @Override
        public void apply() {
            RefineryManager.addRecipe(energy, input, output, InputHelper.toStack(outputItem.getStack()), (int) outputItem.getPercent());
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
            if(!RefineryManager.recipeExists(input)) {
                CraftTweakerAPI.logError("No Refinery recipe exists for: " + input);
                return;
            }
            RefineryManager.removeRecipe(input);
        }
        
        @Override
        protected String getRecipeInfo() {
            return LogHelper.getStackDescription(input);
        }
    }
}
