package com.blamejared.compat.thermalexpansion;

import cofh.thermalexpansion.util.managers.machine.*;
import com.blamejared.ModTweaker;
import com.blamejared.mtlib.helpers.*;
import com.blamejared.mtlib.utils.BaseUndoable;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.*;
import crafttweaker.api.item.*;
import crafttweaker.api.liquid.ILiquidStack;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import stanhebben.zenscript.annotations.*;
import stanhebben.zenscript.annotations.Optional;

import java.util.*;

@ZenClass("mods.thermalexpansion.Centrifuge")
@ModOnly("thermalexpansion")
@ZenRegister
public class Centrifuge {
    
    @ZenMethod
    public static void addRecipe(WeightedItemStack[] outputs, IItemStack input, ILiquidStack fluid, int energy) {
        IItemStack[] items = new IItemStack[outputs.length];
        Integer[] chances = new Integer[outputs.length];
        for(int i = 0; i < outputs.length; i++) {
            items[i] = outputs[i].getStack();
            chances[i] = (int) outputs[i].getPercent();
        }
        ModTweaker.LATE_ADDITIONS.add(new Add(InputHelper.toStacks(items), chances, InputHelper.toStack(input), energy, InputHelper.toFluid(fluid)));
    }
    
    @ZenMethod
    public static void removeRecipe(IItemStack input) {
        
        ModTweaker.LATE_REMOVALS.add(new Remove(InputHelper.toStack(input)));
        
    }
    
    private static class Add extends BaseUndoable {
        
        private ItemStack[] outputs;
        private Integer[] chances;
        private ItemStack input;
        private int energy;
        private FluidStack fluid;
        
        public Add(ItemStack[] outputs, Integer[] chances, ItemStack input, int energy, FluidStack fluid) {
            super("Centrifuge");
            this.outputs = outputs;
            this.chances = chances;
            this.input = input;
            this.energy = energy;
            this.fluid = fluid;
        }
        
        @Override
        public void apply() {
            CentrifugeManager.addRecipe(energy, input, Arrays.asList(outputs), Arrays.asList(chances), fluid);
        }
        
        @Override
        protected String getRecipeInfo() {
            return LogHelper.getStackDescription(input);
        }
    }
    
    private static class Remove extends BaseUndoable {
        
        private ItemStack input;
        
        public Remove(ItemStack input) {
            super("Centrifuge");
            this.input = input;
        }
        
        @Override
        public void apply() {
            if(!CentrifugeManager.recipeExists(input)) {
                CraftTweakerAPI.logError("No Centrifuge recipe exists for: " + input);
                return;
            }
            CentrifugeManager.removeRecipe(input);
        }
        
        @Override
        protected String getRecipeInfo() {
            return LogHelper.getStackDescription(input);
        }
    }
}
