package com.blamejared.compat.thaumcraft.handlers.handlers;

import com.blamejared.ModTweaker;
import com.blamejared.mtlib.helpers.*;
import com.blamejared.mtlib.utils.BaseAction;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.*;
import crafttweaker.api.item.*;
import crafttweaker.api.oredict.IOreDictEntry;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.*;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.internal.CommonInternals;

import java.util.*;

@ZenClass("mods.thaumcraft.SmeltingBonus")
@ZenRegister
@ModOnly("thaumcraft")
public class SmeltingBonus {
    
    @ZenMethod
    public static void addSmeltingBonus(IIngredient input, WeightedItemStack stack) {
        if(!(input instanceof IItemStack || input instanceof IOreDictEntry)) {
            CraftTweakerAPI.logError("Invalid input for SmeltingBonus");
            return;
        }
        
        ModTweaker.LATE_ADDITIONS.add(new Add(InputHelper.toObject(input), InputHelper.toStack(stack.getStack()),  stack.getChance()));
    }
    
    @ZenMethod
    public static void removeSmeltingBonus(IIngredient input, IItemStack stack) {
        if(!(input instanceof IItemStack || input instanceof IOreDictEntry)) {
            CraftTweakerAPI.logError("Invalid input for SmeltingBonus");
            return;
        }
        ModTweaker.LATE_ADDITIONS.add(new Remove(InputHelper.toObject(input), stack));
    }
    
    private static class Add extends BaseAction {
        
        private ItemStack output;
        private Object input;
        private float chance;
        
        public Add(Object input, ItemStack output, float chance) {
            super("SmeltingBonus");
            this.output = output;
            this.input = input;
            this.chance = chance;
        }
        
        @Override
        public void apply() {
            ThaumcraftApi.addSmeltingBonus(input, output, chance);
        }
        
        
        @Override
        public String describe() {
            return "Adding " + getRecipeInfo() + " as a possible smelting bonus for: " + input;
        }
        
        @Override
        protected String getRecipeInfo() {
            return LogHelper.getStackDescription(output);
        }
    }
    
    private static class Remove extends BaseAction {
        
        private IItemStack output;
        private Object input;
        
        public Remove(Object input, IItemStack output) {
            super("SmeltingBonus");
            this.output = output;
            this.input = input;
        }
        
        @Override
        public void apply() {
            List<ThaumcraftApi.SmeltBonus> remove = new ArrayList<>();
            for(ThaumcraftApi.SmeltBonus bonus : CommonInternals.smeltingBonus) {
                if(bonus.in.equals(input) && output.matches(InputHelper.toIItemStack(bonus.out))) {
                    remove.add(bonus);
                }
            }
            CommonInternals.smeltingBonus.removeAll(remove);
        }
        
        
        @Override
        public String describe() {
            return "Removing " + getRecipeInfo() + " as a possible smelting bonus for: " + input;
        }
        
        @Override
        protected String getRecipeInfo() {
            return LogHelper.getStackDescription(output);
        }
    }
    
}
