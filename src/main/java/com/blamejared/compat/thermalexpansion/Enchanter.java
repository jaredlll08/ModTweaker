package com.blamejared.compat.thermalexpansion;

import cofh.thermalexpansion.util.managers.machine.EnchanterManager;
import com.blamejared.ModTweaker;
import com.blamejared.mtlib.helpers.*;
import com.blamejared.mtlib.utils.BaseAction;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.*;
import crafttweaker.api.item.IItemStack;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.*;

@ZenClass("mods.thermalexpansion.Enchanter")
@ModOnly("thermalexpansion")
@ZenRegister
public class Enchanter {
    
    @ZenMethod
    public static void addRecipe(IItemStack output, IItemStack input, IItemStack secondInput, int energy, int experience, boolean empowered) {
        ModTweaker.LATE_ADDITIONS.add(new Add(InputHelper.toStack(output), InputHelper.toStack(input), InputHelper.toStack(secondInput), energy, experience, empowered ? EnchanterManager.Type.EMPOWERED : EnchanterManager.Type.STANDARD));
    }
    
    @ZenMethod
    public static void removeRecipe(IItemStack input, IItemStack secondInput) {
        ModTweaker.LATE_REMOVALS.add(new Remove(InputHelper.toStack(input), InputHelper.toStack(secondInput)));
    }
    
    private static class Add extends BaseAction {
        
        private ItemStack output, input, inputSecondary;
        private int energy, experience;
        private EnchanterManager.Type type;
        
        public Add(ItemStack output, ItemStack input, ItemStack inputSecondary, int energy, int experience, EnchanterManager.Type type) {
            super("Enchanter");
            this.output = output;
            this.input = input;
            this.inputSecondary = inputSecondary;
            this.energy = energy;
            this.experience = experience;
            this.type = type;
        }
        
        @Override
        public void apply() {
            EnchanterManager.addRecipe(energy, input, inputSecondary, output, experience, type);
        }
        
        @Override
        protected String getRecipeInfo() {
            return LogHelper.getStackDescription(input) + " and " + LogHelper.getStackDescription(inputSecondary);
        }
    }
    
    private static class Remove extends BaseAction {
        
        private ItemStack input, inputSecondary;
        
        public Remove(ItemStack input, ItemStack inputSecondary) {
            super("Enchanter");
            this.input = input;
            this.inputSecondary = inputSecondary;
        }
        
        @Override
        public void apply() {
            if(!EnchanterManager.recipeExists(input, inputSecondary)) {
                CraftTweakerAPI.logError("No Enchanter recipe exists for: " + input + " and " + inputSecondary);
                return;
            }
            EnchanterManager.removeRecipe(input, inputSecondary);
        }
        
        @Override
        protected String getRecipeInfo() {
            return LogHelper.getStackDescription(input) + " and " + LogHelper.getStackDescription(inputSecondary);
        }
    }
}
