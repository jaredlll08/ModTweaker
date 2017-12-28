package com.blamejared.compat.thermalexpansion;

import cofh.thermalexpansion.util.managers.machine.FurnaceManager;
import com.blamejared.ModTweaker;
import com.blamejared.mtlib.helpers.*;
import com.blamejared.mtlib.utils.BaseUndoable;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.*;
import crafttweaker.api.item.IItemStack;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.*;

@ZenClass("mods.thermalexpansion.RedstoneFurnace")
@ModOnly("thermalexpansion")
@ZenRegister
public class RedstoneFurnace {
    
    @ZenMethod
    public static void addRecipe(IItemStack output, IItemStack input, int energy) {
        ModTweaker.LATE_ADDITIONS.add(new Add(InputHelper.toStack(output), InputHelper.toStack(input), energy));
    }
    
    @ZenMethod
    public static void removeRecipe(IItemStack input) {
        ModTweaker.LATE_REMOVALS.add(new Remove(InputHelper.toStack(input)));
    }
    
    private static class Add extends BaseUndoable {
        
        private ItemStack output, input;
        private int energy;
        
        public Add(ItemStack output, ItemStack input, int energy) {
            super("RedstoneFurnace");
            this.output = output;
            this.input = input;
            this.energy = energy;
        }
        
        @Override
        public void apply() {
            FurnaceManager.addRecipe(energy, input, output);
        }
        
        @Override
        protected String getRecipeInfo() {
            return LogHelper.getStackDescription(output);
        }
    }
    
    private static class Remove extends BaseUndoable {
        
        private ItemStack input;
        
        public Remove(ItemStack input) {
            super("RedstoneFurnace");
            this.input = input;
        }
        
        @Override
        public void apply() {
            if(!FurnaceManager.recipeExists(input)) {
                CraftTweakerAPI.logError("No Furnace recipe exists for: " + input);
                return;
            }
            FurnaceManager.removeRecipe(input);
        }
        
        @Override
        protected String getRecipeInfo() {
            return LogHelper.getStackDescription(input);
        }
    }
}
