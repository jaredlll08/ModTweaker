package com.blamejared.compat.thermalexpansion;

import cofh.thermalexpansion.util.managers.machine.*;
import com.blamejared.ModTweaker;
import com.blamejared.mtlib.helpers.InputHelper;
import com.blamejared.mtlib.helpers.LogHelper;
import com.blamejared.mtlib.utils.BaseUndoable;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IItemStack;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.thermalexpansion.InductionSmelter")
@ModOnly("thermalexpansion")
@ZenRegister
public class InductionSmelter {
    
    @ZenMethod
    public static void addRecipe(IItemStack primaryOutput, IItemStack primaryInput, IItemStack secondaryInput, int energy, @Optional IItemStack secondaryOutput, @Optional int secondaryChance) {
        ModTweaker.LATE_ADDITIONS.add(new Add(InputHelper.toStack(primaryOutput), InputHelper.toStack(primaryInput), InputHelper.toStack(secondaryInput), energy, InputHelper.toStack(secondaryOutput), secondaryChance));
    }
    
    @ZenMethod
    public static void removeRecipe(IItemStack primaryInput, IItemStack secondaryInput) {
        ModTweaker.LATE_REMOVALS.add(new Remove(InputHelper.toStack(primaryInput), InputHelper.toStack(secondaryInput)));
    }
    
    private static class Add extends BaseUndoable {
        
        private ItemStack primaryOutput, primaryInput, secondaryInput, secondaryOutput;
        private int energy, secondaryChance;
        
        public Add(ItemStack primaryOutput, ItemStack primaryInput, ItemStack secondaryInput, int energy, ItemStack secondaryOutput, int secondaryChance) {
            super("InductionSmelter");
            this.primaryOutput = primaryOutput;
            this.primaryInput = primaryInput;
            this.secondaryInput = secondaryInput;
            this.secondaryOutput = secondaryOutput;
            this.energy = energy;
            this.secondaryChance = secondaryChance;
            if(!secondaryOutput.isEmpty() && secondaryChance <= 0) {
                this.secondaryChance = 100;
            }
        }
        
        @Override
        public void apply() {
            SmelterManager.addRecipe(energy, primaryInput, secondaryInput, primaryOutput, secondaryOutput, secondaryChance);
        }
        
        @Override
        protected String getRecipeInfo() {
            return LogHelper.getStackDescription(primaryOutput);
        }
    }
    
    private static class Remove extends BaseUndoable {
        
        private ItemStack primaryInput, secondaryInput;
        
        public Remove(ItemStack primaryInput, ItemStack secondaryInput) {
            super("InductionSmelter");
            this.primaryInput = primaryInput;
            this.secondaryInput = secondaryInput;
        }
        
        @Override
        public void apply() {
            if(!SmelterManager.recipeExists(primaryInput, secondaryInput)) {
                CraftTweakerAPI.logError("No InductionSmelter recipe exists for: " + primaryInput + " and " + secondaryInput);
                return;
            }
            SmelterManager.removeRecipe(primaryInput, secondaryInput);
        }
        
        @Override
        protected String getRecipeInfo() {
            return LogHelper.getStackDescription(primaryInput) + " and " + LogHelper.getStackDescription(secondaryInput);
        }
    }
}
