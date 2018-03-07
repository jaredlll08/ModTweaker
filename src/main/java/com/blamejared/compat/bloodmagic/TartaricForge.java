package com.blamejared.compat.bloodmagic;

import WayofTime.bloodmagic.api.impl.BloodMagicAPI;
import com.blamejared.ModTweaker;
import com.blamejared.mtlib.helpers.InputHelper;
import com.blamejared.mtlib.utils.BaseAction;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.*;
import crafttweaker.api.item.*;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.*;

@ZenClass("mods.bloodmagic.TartaricForge")
@ZenRegister
@ModOnly("bloodmagic")
public class TartaricForge {
    
    @ZenMethod
    public static void addRecipe(IItemStack output, IIngredient[] inputs, double minSouls, double soulDrain) {
        if(inputs.length == 0 || inputs.length > 4) {
            CraftTweakerAPI.logError("Invalid Input Array! Maximum size is 4!");
            return;
        }
        ModTweaker.LATE_ADDITIONS.add(new Add(InputHelper.toStack(output), minSouls, soulDrain, InputHelper.toObjects(inputs)));
    }
    
    @ZenMethod
    public static void removeRecipe(IItemStack[] inputs) {
        ModTweaker.LATE_REMOVALS.add(new Remove(InputHelper.toStacks(inputs)));
    }
    
    
    private static class Add extends BaseAction {
        
        private ItemStack output;
        private double minSouls, soulDrain;
        private Object[] inputs;
        
        public Add(ItemStack output, double minSouls, double soulDrain, Object[] inputs) {
            super("TartaricForge");
            this.output = output;
            this.minSouls = minSouls;
            this.soulDrain = soulDrain;
            this.inputs = inputs;
        }
        
        @Override
        public void apply() {
            BloodMagicAPI.INSTANCE.getRecipeRegistrar().addTartaricForge(output, minSouls, soulDrain, inputs);
        }
        
        
        @Override
        public String describe() {
            return "Adding TartaricForge recipe for: " + output + " from: [" + String.join(",", getStringFromStacks(inputs)) + "] minSouls: " + minSouls + ", soulDrain: " + soulDrain;
        }
        
        
    }
    
    public static String[] getStringFromStacks(Object[] arr) {
        String[] retArr = new String[arr.length];
        for(int i = 0; i < arr.length; i++) {
            retArr[i] = arr[i].toString();
        }
        return retArr;
    }
    
    private static class Remove extends BaseAction {
        
        private ItemStack[] inputs;
        
        public Remove(ItemStack[] inputs) {
            super("TartaricForge");
            this.inputs = inputs;
        }
        
        @Override
        public void apply() {
            BloodMagicAPI.INSTANCE.getRecipeRegistrar().removeTartaricForge(inputs);
        }
        
        @Override
        public String describe() {
            return "Removing TartaricForge recipe for: [" + String.join(",", getStringFromStacks(inputs)) + "]";
        }
    }
}
