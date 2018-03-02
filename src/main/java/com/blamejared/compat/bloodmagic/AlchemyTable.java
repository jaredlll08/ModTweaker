package com.blamejared.compat.bloodmagic;

import WayofTime.bloodmagic.api.impl.BloodMagicAPI;
import com.blamejared.ModTweaker;
import com.blamejared.mtlib.helpers.InputHelper;
import com.blamejared.mtlib.utils.BaseAction;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.*;
import crafttweaker.api.item.IItemStack;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.*;

@ZenClass("mods.bloodmagic.AlchemyTable")
@ZenRegister
@ModOnly("bloodmagic")
public class AlchemyTable {
    
    @ZenMethod
    public static void addRecipe(IItemStack output, IItemStack[] inputs, int syphon, int ticks, int minTier) {
        if(inputs.length == 0 || inputs.length > 6) {
            CraftTweakerAPI.logError("Invalid Input Array! Maximum size is 6!");
            return;
        }
        ModTweaker.LATE_ADDITIONS.add(new Add(InputHelper.toStack(output), syphon, ticks, minTier, InputHelper.toStacks(inputs)));
    }
    
    @ZenMethod
    public static void removeRecipe(IItemStack[] inputs) {
        if(inputs.length == 0 || inputs.length > 6) {
            CraftTweakerAPI.logError("Invalid Input Array! Maximum size is 6!");
            return;
        }
        ModTweaker.LATE_REMOVALS.add(new Remove(InputHelper.toStacks(inputs)));
    }
    
    
    private static class Add extends BaseAction {
        
        private ItemStack output;
        private int syphon, ticks, minTier;
        private ItemStack[] inputs;
        
        public Add(ItemStack output, int syphon, int ticks, int minTier, ItemStack[] inputs) {
            super("AlchemyTable");
            this.output = output;
            this.syphon = syphon;
            this.ticks = ticks;
            this.minTier = minTier;
            this.inputs = inputs;
        }
        
        @Override
        public void apply() {
            BloodMagicAPI.INSTANCE.getRecipeRegistrar().addTartaricForge(output, syphon, ticks, minTier, inputs);
        }
        
        
        @Override
        public String describe() {
            return "Adding AlchemyTable recipe for: " + output + " from: [" + String.join(",", getStringFromStacks(inputs)) + "] syphon: " + syphon + ", ticks: " + ticks + ", minTier: " + minTier;
        }
        
        
    }
    
    public static String[] getStringFromStacks(ItemStack[] arr) {
        String[] retArr = new String[arr.length];
        for(int i = 0; i < arr.length; i++) {
            retArr[i] = arr[i].toString();
        }
        return retArr;
    }
    
    private static class Remove extends BaseAction {
        
        private ItemStack[] inputs;
        
        public Remove(ItemStack[] inputs) {
            super("AlchemyTable");
            this.inputs = inputs;
        }
        
        @Override
        public void apply() {
            BloodMagicAPI.INSTANCE.getRecipeRegistrar().removeAlchemyTable(inputs);
        }
        
        @Override
        public String describe() {
            return "Removing AlchemyTable recipe for: [" + String.join(",", getStringFromStacks(inputs)) + "]";
        }
    }
}
