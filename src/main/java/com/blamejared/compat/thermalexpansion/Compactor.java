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
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.thermalexpansion.Compactor")
@ModOnly("thermalexpansion")
@ZenRegister
public class Compactor {
    
    @ZenMethod
    public static void addMintRecipe(IItemStack output, IItemStack input, int energy) {
        ModTweaker.LATE_ADDITIONS.add(new Add(InputHelper.toStack(output), InputHelper.toStack(input), energy, CompactorManager.Mode.MINT));
    }
    
    @ZenMethod
    public static void addPressRecipe(IItemStack output, IItemStack input, int energy) {
        ModTweaker.LATE_ADDITIONS.add(new Add(InputHelper.toStack(output), InputHelper.toStack(input), energy, CompactorManager.Mode.PRESS));
    }
    
    @ZenMethod
    public static void addStorageRecipe(IItemStack output, IItemStack input, int energy) {
        ModTweaker.LATE_ADDITIONS.add(new Add(InputHelper.toStack(output), InputHelper.toStack(input), energy, CompactorManager.Mode.STORAGE));
    }
    
    @ZenMethod
    public static void removeMintRecipe(IItemStack input) {
        ModTweaker.LATE_REMOVALS.add(new Remove(InputHelper.toStack(input), CompactorManager.Mode.MINT));
    }
    
    @ZenMethod
    public static void removePressRecipe(IItemStack input) {
        ModTweaker.LATE_REMOVALS.add(new Remove(InputHelper.toStack(input), CompactorManager.Mode.PRESS));
    }
    
    @ZenMethod
    public static void removeStorageRecipe(IItemStack input) {
        ModTweaker.LATE_REMOVALS.add(new Remove(InputHelper.toStack(input), CompactorManager.Mode.STORAGE));
    }
    
    private static class Add extends BaseUndoable {
        
        private ItemStack output, input;
        private int energy;
        private CompactorManager.Mode mode;
        
        public Add(ItemStack output, ItemStack input, int energy, CompactorManager.Mode mode) {
            super("Compactor");
            this.output = output;
            this.input = input;
            this.energy = energy;
            this.mode = mode;
        }
        
        @Override
        public void apply() {
            CompactorManager.addRecipe(energy, input, output, mode);
        }
        
        @Override
        protected String getRecipeInfo() {
            return LogHelper.getStackDescription(output) + " in mode: " + mode;
        }
    }
    
    private static class Remove extends BaseUndoable {
        
        private ItemStack input;
        private CompactorManager.Mode mode;
        
        public Remove(ItemStack input, CompactorManager.Mode mode) {
            super("Compactor");
            this.input = input;
            this.mode = mode;
        }
        
        @Override
        public void apply() {
            if(!CompactorManager.recipeExists(input, mode)) {
                CraftTweakerAPI.logError("No Compactor recipe exists for: " + input);
                return;
            }
            
            CompactorManager.removeRecipe(input, mode);
        }
        
        @Override
        protected String getRecipeInfo() {
            return LogHelper.getStackDescription(input) + " in mode: " + mode;
        }
    }
}
