package com.blamejared.compat.chisel;

import com.blamejared.api.annotations.*;
import minetweaker.*;
import minetweaker.api.item.IItemStack;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import stanhebben.zenscript.annotations.*;
import team.chisel.api.carving.*;

import static com.blamejared.mtlib.helpers.InputHelper.toStack;


@ZenClass("mods.chisel.Groups")
@Handler("chisel")
public class Groups {
    
    @ZenMethod
    @Document({"groupName", "stack"})
    public static void addVariation(String groupName, IItemStack stack) {
        ICarvingGroup group = CarvingUtils.getChiselRegistry().getGroup(groupName);
        Block block = Block.getBlockFromItem(toStack(stack).getItem());
        IBlockState state = block.getStateFromMeta(toStack(stack).getItemDamage());
        ICarvingVariation variation = CarvingUtils.getDefaultVariationFor(state, 0);
        if(group == null) {
            MineTweakerAPI.getLogger().logError("Cannot find group " + groupName);
            return;
        }
        if(variation == null) {
            MineTweakerAPI.getLogger().logError("Can't create variation from " + stack);
            return;
        }
        MineTweakerAPI.apply(new AddVariation(group, variation, stack.toString()));
    }
    
    @ZenMethod
    @Document({"groupName"})
    public static void removeGroup(String groupName) {
        ICarvingGroup group = CarvingUtils.getChiselRegistry().getGroup(groupName);
        if(group == null) {
            MineTweakerAPI.getLogger().logError("Could not find group " + groupName);
            return;
        }
        MineTweakerAPI.apply(new RemoveGroup(group));
    }
    
    @ZenMethod
    @Document({"stack"})
    public static void removeVariation(IItemStack stack) {
        ICarvingVariation variation = null;
        ICarvingGroup g = CarvingUtils.getChiselRegistry().getGroup(toStack(stack));
        if(g != null) {
            for(ICarvingVariation v : g.getVariations()) {
                if(v.getStack().isItemEqual(toStack(stack))) {
                    variation = v;
                }
            }
        }
        if(variation == null) {
            MineTweakerAPI.getLogger().logError("Can't find variation from " + stack);
            return;
        }
        MineTweakerAPI.apply(new RemoveVariation(variation, stack.toString()));
    }
    
    @ZenMethod
    @Document({"groupName"})
    public static void addGroup(String groupName) {
        ICarvingGroup group = CarvingUtils.getChiselRegistry().getGroup(groupName);
        if(group != null) {
            MineTweakerAPI.getLogger().logError("Group already exists " + groupName);
            return;
        }
        group = CarvingUtils.getDefaultGroupFor(groupName);
        MineTweakerAPI.apply(new AddGroup(group));
    }
    
    private static class AddVariation implements IUndoableAction {
        
        ICarvingGroup group;
        ICarvingVariation variation;
        String variationName;
        
        AddVariation(ICarvingGroup group, ICarvingVariation variation, String variationName) {
            this.group = group;
            this.variation = variation;
            this.variationName = variationName;
        }
        
        @Override
        public void apply() {
            CarvingUtils.getChiselRegistry().addVariation(group.getName(), variation);
        }
        
        @Override
        public void undo() {
            CarvingUtils.getChiselRegistry().removeVariation(variation.getBlockState(), group.getName());
        }
        
        @Override
        public boolean canUndo() {
            return group != null && variation != null;
        }
        
        @Override
        public String describe() {
            return "Adding Variation: " + variationName;
        }
        
        @Override
        public String describeUndo() {
            return "Removing Variation: " + variationName;
        }
        
        
        @Override
        public Object getOverrideKey() {
            return null;
        }
    }
    
    
    private static class RemoveVariation implements IUndoableAction {
        
        ICarvingVariation variation;
        String variationName;
        ICarvingGroup group;
        
        RemoveVariation(ICarvingVariation variation, String variationName) {
            this.variation = variation;
            this.variationName = variationName;
        }
        
        @Override
        public void apply() {
            group = CarvingUtils.getChiselRegistry().getGroup(variation.getStack());
            CarvingUtils.getChiselRegistry().removeVariation(variation.getBlockState(), group.getName());
        }
        
        @Override
        public boolean canUndo() {
            return group != null && variation != null;
        }
        
        @Override
        public String describe() {
            return "Removing Variation: " + variationName;
        }
        
        @Override
        public String describeUndo() {
            return "Adding Variation: " + variationName;
        }
        
        @Override
        public void undo() {
            CarvingUtils.getChiselRegistry().addVariation(group.getName(), variation);
        }
        
        @Override
        public Object getOverrideKey() {
            return null;
        }
    }
    
    
    static class AddGroup implements IUndoableAction {
        
        ICarvingGroup group;
        
        AddGroup(ICarvingGroup group) {
            this.group = group;
        }
        
        @Override
        public void apply() {
            CarvingUtils.getChiselRegistry().addGroup(group);
        }
        
        @Override
        public boolean canUndo() {
            return group != null;
        }
        
        @Override
        public String describe() {
            return "Adding Group: " + group.getName();
        }
        
        @Override
        public String describeUndo() {
            return "Removing Group: " + group.getName();
        }
        
        @Override
        public void undo() {
            CarvingUtils.getChiselRegistry().removeGroup(group.getName());
        }
        
        @Override
        public Object getOverrideKey() {
            return null;
        }
    }
    
    
    private static class RemoveGroup implements IUndoableAction {
        
        ICarvingGroup group;
        
        RemoveGroup(ICarvingGroup group) {
            this.group = group;
        }
        
        @Override
        public void apply() {
            CarvingUtils.getChiselRegistry().removeGroup(group.getName());
        }
        
        @Override
        public boolean canUndo() {
            return group != null;
        }
        
        @Override
        public String describe() {
            return "Removing Group: " + group.getName();
        }
        
        @Override
        public String describeUndo() {
            return "Adding Group: " + group.getName();
        }
        
        @Override
        public void undo() {
            CarvingUtils.getChiselRegistry().addGroup(group);
        }
        
        @Override
        public Object getOverrideKey() {
            return null;
        }
    }
    
}