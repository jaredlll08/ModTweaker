package com.blamejared.compat.thermalexpansion;

import cofh.thermalexpansion.util.managers.machine.*;
import com.blamejared.ModTweaker;
import com.blamejared.mtlib.helpers.*;
import com.blamejared.mtlib.utils.BaseUndoable;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.*;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.liquid.ILiquidStack;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import stanhebben.zenscript.annotations.*;

@ZenClass("mods.thermalexpansion.Infuser")
@ModOnly("thermalexpansion")
@ZenRegister
public class Infuser {
    
    @ZenMethod
    public static void addRecipe(IItemStack output, IItemStack input, int energy) {
        ModTweaker.LATE_ADDITIONS.add(new Add(InputHelper.toStack(output), InputHelper.toStack(input), energy));
    }
    
    @ZenMethod
    public static void removeRecipe(IItemStack input) {
        
        ModTweaker.LATE_REMOVALS.add(new Remove(InputHelper.toStack(input)));
    }
    
    private static class Add extends BaseUndoable {
        
        private ItemStack output;
        private ItemStack input;
        private int energy;
        
        public Add(ItemStack output, ItemStack input, int energy) {
            super("Infuser");
            this.output = output;
            this.input = input;
            this.energy = energy;
        }
        
        @Override
        public void apply() {
            ChargerManager.addRecipe(energy, input, output);
        }
        
        @Override
        protected String getRecipeInfo() {
            return LogHelper.getStackDescription(output);
        }
    }
    
    private static class Remove extends BaseUndoable {
        
        private ItemStack input;
        
        public Remove(ItemStack input) {
            super("Infuser");
            this.input = input;
        }
        
        @Override
        public void apply() {
            if(!ChargerManager.recipeExists(input)) {
                CraftTweakerAPI.logError("No Infuser recipe exists for: " + input);
                return;
            }
            ChargerManager.removeRecipe(input);
        }
        
        @Override
        protected String getRecipeInfo() {
            return LogHelper.getStackDescription(input);
        }
    }
}
