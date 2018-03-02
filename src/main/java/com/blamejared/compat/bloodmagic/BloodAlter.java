package com.blamejared.compat.bloodmagic;

import WayofTime.bloodmagic.api.impl.BloodMagicAPI;
import com.blamejared.ModTweaker;
import com.blamejared.mtlib.helpers.InputHelper;
import com.blamejared.mtlib.utils.BaseAction;
import crafttweaker.annotations.*;
import crafttweaker.api.item.IItemStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import stanhebben.zenscript.annotations.*;

@ZenClass("mods.bloodmagic.BloodAlter")
@ZenRegister
@ModOnly("bloodmagic")
public class BloodAlter {
    
    @ZenMethod
    public static void addRecipe(IItemStack output, IItemStack input, int minimumTier, int syphon, int consumeRate, int drainRate) {
        ModTweaker.LATE_ADDITIONS.add(new Add(Ingredient.fromStacks(InputHelper.toStack(input)), InputHelper.toStack(output), minimumTier, syphon, consumeRate, drainRate));
    }
    
    @ZenMethod
    public static void removeRecipe(IItemStack input) {
        ModTweaker.LATE_REMOVALS.add(new Remove(InputHelper.toStack(input)));
    }
    
    
    private static class Add extends BaseAction {
        
        private Ingredient input;
        private ItemStack output;
        private int minTier, syphon, consumeRate, drainRate;
        
        public Add(Ingredient input, ItemStack output, int minTier, int syphon, int consumeRate, int drainRate) {
            super("BloodAlter");
            this.input = input;
            this.output = output;
            this.minTier = minTier;
            this.syphon = syphon;
            this.consumeRate = consumeRate;
            this.drainRate = drainRate;
        }
        
        @Override
        public void apply() {
            BloodMagicAPI.INSTANCE.getRecipeRegistrar().addBloodAltar(input, output, minTier, syphon, consumeRate, drainRate);
        }
        
        @Override
        public String describe() {
            return "Adding Blood Alter recipe for: " + output + " from: " + input + " minTier: " + minTier + ", syphon: " + syphon + ", consumeRate: " + consumeRate + ", drainRate: " + drainRate;
        }
    }
    
    private static class Remove extends BaseAction {
        
        private ItemStack input;
        
        public Remove(ItemStack input) {
            super("BloodAlter");
            this.input = input;
        }
        
        @Override
        public void apply() {
            BloodMagicAPI.INSTANCE.getRecipeRegistrar().removeBloodAltar(input);
        }
        
        @Override
        public String describe() {
            return "Removing Blood Alter recipe for: " + input;
        }
    }
}
