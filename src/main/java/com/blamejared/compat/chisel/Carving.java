package com.blamejared.compat.chisel;

import com.blamejared.ModTweaker;
import com.blamejared.mtlib.utils.BaseAction;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.*;
import crafttweaker.api.item.IItemStack;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.*;
import team.chisel.api.carving.CarvingUtils;

import static com.blamejared.mtlib.helpers.InputHelper.toStack;

@ZenRegister
@ZenClass("mods.chisel.Carving")
@ModOnly("chisel")
public class Carving {
    
    @ZenMethod
    public static void addGroup(String name) {
        ModTweaker.LATE_ADDITIONS.add(new ActionAddGroup(name));
    }
    
    @ZenMethod
    public static void addVariation(String groupName, IItemStack stack) {
        ModTweaker.LATE_ADDITIONS.add(new ActionAddVariation(groupName, toStack(stack)));
    }
    
    @ZenMethod
    public static void removeGroup(String name) {
        ModTweaker.LATE_REMOVALS.add(new ActionRemoveGroup(name));
    }
    
    @ZenMethod
    public static void removeVariation(String groupName, IItemStack stack) {
        ModTweaker.LATE_ADDITIONS.add(new ActionRemoveVariation(groupName, toStack(stack)));
    }
    
    private static class ActionAddGroup extends BaseAction {
        
        private String groupName;
        
        protected ActionAddGroup(String groupName) {
            super("Carving");
            this.groupName = groupName;
        }
        
        @Override
        public void apply() {
            if(CarvingUtils.getChiselRegistry().getSortedGroupNames().contains(groupName)) {
                CraftTweakerAPI.logError("Error trying to add a  Chisel Group that already exists! Group: " + groupName);
                return;
            }
            CarvingUtils.getChiselRegistry().addGroup(CarvingUtils.getDefaultGroupFor(groupName));
        }
        
        @Override
        public String describe() {
            return "Adding chisel group called: " + groupName;
        }
        
    }
    
    
    private static class ActionAddVariation extends BaseAction {
        
        private String groupName;
        private ItemStack stack;
        
        
        protected ActionAddVariation(String groupName, ItemStack stack) {
            super("Variation");
            this.groupName = groupName;
            this.stack = stack;
        }
        
        @Override
        public void apply() {
            CarvingUtils.getChiselRegistry().addVariation(groupName, CarvingUtils.variationFor(stack, 0));
        }
        
        @Override
        public String describe() {
            return "Adding chisel variation for group: " + groupName + " and item: " + stack;
        }
        
    }
    
    private static class ActionRemoveGroup extends BaseAction {
        
        private String groupName;
        
        protected ActionRemoveGroup(String groupName) {
            super("Carving");
            this.groupName = groupName;
        }
        
        @Override
        public void apply() {
            if(!CarvingUtils.getChiselRegistry().getSortedGroupNames().contains(groupName)) {
                CraftTweakerAPI.logError("Error trying to remove Chisel Group that doesn't exist! Group: " + groupName);
                return;
            }
            CarvingUtils.getChiselRegistry().removeGroup(groupName);
        }
        
        @Override
        public String describe() {
            return "Removing chisel group called: " + groupName;
        }
        
    }
    
    private static class ActionRemoveVariation extends BaseAction {
        
        private String groupName;
        private ItemStack item;
        
        protected ActionRemoveVariation(String groupName, ItemStack item) {
            super("Variation");
            this.groupName = groupName;
            this.item = item;
        }
        
        @Override
        public void apply() {
            CarvingUtils.getChiselRegistry().removeVariation(item, groupName);
        }
        
        @Override
        public String describe() {
            return "Removing chisel group called: " + groupName;
        }
        
    }
    
}
