package com.blamejared.compat.thermalexpansion;

import cofh.thermalexpansion.util.managers.machine.PulverizerManager;
import com.blamejared.ModTweaker;
import com.blamejared.mtlib.helpers.*;
import com.blamejared.mtlib.utils.BaseAction;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.*;
import crafttweaker.api.item.IItemStack;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.*;

@ZenClass("mods.thermalexpansion.Pulverizer")
@ModOnly("thermalexpansion")
@ZenRegister
public class Pulverizer {
    
    @ZenMethod
    public static void addRecipe(IItemStack output, IItemStack input, int energy, @Optional IItemStack secondaryOutput, @Optional int secondaryChance) {
        ModTweaker.LATE_ADDITIONS.add(new Add(InputHelper.toStack(output), InputHelper.toStack(input), energy, InputHelper.toStack(secondaryOutput), secondaryChance));
    }
    
    @ZenMethod
    public static void removeRecipe(IItemStack input) {
        ModTweaker.LATE_REMOVALS.add(new Remove(InputHelper.toStack(input)));
    }
    
    private static class Add extends BaseAction {
        
        private ItemStack output, input, secondaryOutput;
        private int energy, secondaryChance;
        
        public Add(ItemStack output, ItemStack input, int energy, ItemStack secondaryOutput, int secondaryChance) {
            super("Pulverizer");
            this.output = output;
            this.input = input;
            this.secondaryOutput = secondaryOutput;
            this.energy = energy;
            this.secondaryChance = secondaryChance;
            if(!secondaryOutput.isEmpty() && secondaryChance <= 0) {
                this.secondaryChance = 100;
            }
        }
        
        @Override
        public void apply() {
            PulverizerManager.addRecipe(energy, input, output, secondaryOutput, secondaryChance);
        }
        
        @Override
        protected String getRecipeInfo() {
            return LogHelper.getStackDescription(output);
        }
    }
    
    private static class Remove extends BaseAction {
        
        private ItemStack input;
        
        public Remove(ItemStack input) {
            super("Pulverizer");
            this.input = input;
        }
        
        @Override
        public void apply() {
            if(!PulverizerManager.recipeExists(input)) {
                CraftTweakerAPI.logError("No Pulverizer recipe exists for: " + input);
                return;
            }
            PulverizerManager.removeRecipe(input);
        }
        
        @Override
        protected String getRecipeInfo() {
            return LogHelper.getStackDescription(input);
        }
    }
}
