package com.blamejared.compat.thaumcraft.handlers.handlers;

import com.blamejared.ModTweaker;
import com.blamejared.mtlib.helpers.*;
import com.blamejared.mtlib.utils.BaseAction;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.*;
import crafttweaker.api.item.*;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.*;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.internal.WeightedRandomLoot;

import java.util.*;

@ZenClass("mods.thaumcraft.LootBag")
@ZenRegister
@ModOnly("thaumcraft")
public class LootBag {
    
    @ZenMethod
    public static void addLoot(WeightedItemStack stack, int[] bagTypes) {
        if(bagTypes == null || bagTypes.length <= 0) {
            CraftTweakerAPI.logError("Invalid array size (or null) for LootBag!");
            return;
        }
        
        ModTweaker.LATE_ADDITIONS.add(new Add(InputHelper.toStack(stack.getStack()), (int) stack.getPercent(), bagTypes));
    }
    
    @ZenMethod
    public static void removeLoot(IItemStack stack, int[] bagTypes) {
        if(bagTypes == null || bagTypes.length <= 0) {
            CraftTweakerAPI.logError("Invalid array size (or null) for LootBag!");
            return;
        }
        ModTweaker.LATE_ADDITIONS.add(new Remove(stack, bagTypes));
    }
    
    private static class Add extends BaseAction {
        
        private ItemStack stack;
        private int chance;
        private int[] bagTypes;
        
        public Add(ItemStack stack, int chance, int[] bagTypes) {
            super("LootBag");
            this.stack = stack;
            this.chance = chance;
            this.bagTypes = bagTypes;
        }
        
        @Override
        public void apply() {
            ThaumcraftApi.addLootBagItem(stack, chance, bagTypes);
        }
        
        
        @Override
        public String describe() {
            return "Adding " + getRecipeInfo() + " to LootBag types: " + getBags();
        }
        
        public String getBags() {
            String[] arr = new String[bagTypes.length];
            for(int i = 0; i < bagTypes.length; i++) {
                arr[i] = String.valueOf(bagTypes[i]);
            }
            return String.join(",", arr);
        }
        
        @Override
        protected String getRecipeInfo() {
            return LogHelper.getStackDescription(stack);
        }
    }
    
    private static class Remove extends BaseAction {
        
        private IItemStack stack;
        private int[] bagTypes;
        
        public Remove(IItemStack stack, int[] bagTypes) {
            super("LootBag");
            this.stack = stack;
            this.bagTypes = bagTypes;
        }
        
        @Override
        public void apply() {
            for(int i : bagTypes) {
                ArrayList<WeightedRandomLoot> list = new ArrayList<>();
                
                switch(i) {
                    case 0:
                        list = WeightedRandomLoot.lootBagCommon;
                        break;
                    case 1:
                        list = WeightedRandomLoot.lootBagUncommon;
                        break;
                    case 2:
                        list = WeightedRandomLoot.lootBagRare;
                        break;
                    default:
                    CraftTweakerAPI.logError("Invalid bag type! Type: " + i + " is out of bounds!");
                    
                }
                List<WeightedRandomLoot> remove = new ArrayList<>();
                for(WeightedRandomLoot loot : list) {
                    if(stack.matches(InputHelper.toIItemStack(loot.item))) {
                        remove.add(loot);
                    }
                }
                list.removeAll(remove);
            }
        }
        
        
        @Override
        public String describe() {
            return "Removing " + getRecipeInfo() + " from LootBag types: " + getBags();
        }
        
        public String getBags() {
            String[] arr = new String[bagTypes.length];
            for(int i = 0; i < bagTypes.length; i++) {
                arr[i] = String.valueOf(bagTypes[i]);
            }
            return String.join(",", arr);
        }
        
        @Override
        protected String getRecipeInfo() {
            return LogHelper.getStackDescription(stack);
        }
    }
    
}
