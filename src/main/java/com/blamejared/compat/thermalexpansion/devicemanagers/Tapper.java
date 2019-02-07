package com.blamejared.compat.thermalexpansion.devicemanagers;

import cofh.thermalexpansion.util.managers.device.TapperManager;
import com.blamejared.ModTweaker;
import com.blamejared.mtlib.utils.BaseAction;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.block.IBlockState;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.liquid.ILiquidStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.thermalexpansion.Tapper")
@ModOnly("thermalexpansion")
@ZenRegister
public class Tapper {
    
    @ZenMethod
    public static void addStandardMapping(IItemStack in, ILiquidStack out) {
        ModTweaker.LATE_ADDITIONS.add(new ActionAddTappingMapping(ActionAddTappingMapping.AddType.Standard, out, in));
    }
    
    @ZenMethod
    public static void addItemMapping(IItemStack in, ILiquidStack out) {
        ModTweaker.LATE_ADDITIONS.add(new ActionAddTappingMapping(ActionAddTappingMapping.AddType.Item, out, in));
    }
    
    @ZenMethod
    public static void addBlockStateMapping(IItemStack in, ILiquidStack out) {
        ModTweaker.LATE_ADDITIONS.add(new ActionAddTappingMapping(ActionAddTappingMapping.AddType.BlockStateItem, out, in));
    }
    
    @ZenMethod
    public static void addBlockStateMapping(IBlockState in, ILiquidStack out) {
        ModTweaker.LATE_ADDITIONS.add(new ActionAddTappingMappingState(in, out));
    }
    
    @ZenMethod
    public static void addLeafMapping(IBlockState log, IBlockState leaf) {
        ModTweaker.LATE_ADDITIONS.add(new ActionAddTappingLeaf(log, leaf));
    }
    
    
    private static final class ActionAddTappingMapping extends BaseAction {
        
        private final AddType type;
        private final ILiquidStack outLiquid;
        private final IItemStack inItem;
        
        private ActionAddTappingMapping(AddType type, ILiquidStack outLiquid, IItemStack inItem) {
            super("Tapper");
            this.type = type;
            this.outLiquid = outLiquid;
            this.inItem = inItem;
        }
        
        @Override
        public void apply() {
            final boolean added;
            switch(type) {
                case Standard:
                    added = TapperManager.addStandardMapping(CraftTweakerMC.getItemStack(inItem), CraftTweakerMC.getLiquidStack(outLiquid));
                    break;
                case Item:
                    added = TapperManager.addItemMapping(CraftTweakerMC.getItemStack(inItem), CraftTweakerMC.getLiquidStack(outLiquid));
                    break;
                case BlockStateItem:
                    added = TapperManager.addBlockStateMapping(CraftTweakerMC.getItemStack(inItem), CraftTweakerMC.getLiquidStack(outLiquid));
                    break;
                default:
                    added = false;
            }
            
            if(!added) {
                CraftTweakerAPI.logError(String.format("Could not add recipe for %s -> %s", inItem.toCommandString(), outLiquid.toCommandString()));
            }
        }
        
        @Override
        protected String getRecipeInfo() {
            return outLiquid.toCommandString();
        }
        
        private enum AddType {Item, BlockStateItem, Standard}
    }
    
    
    private static final class ActionAddTappingMappingState extends BaseAction {
        
        private final ILiquidStack outLiquid;
        private final IBlockState inState;
        
        
        private ActionAddTappingMappingState(IBlockState inState, ILiquidStack outLiquid) {
            super("Tapper");
            this.outLiquid = outLiquid;
            this.inState = inState;
        }
        
        @Override
        public void apply() {
            final boolean added = TapperManager.addBlockStateMapping(CraftTweakerMC.getBlockState(inState), CraftTweakerMC.getLiquidStack(outLiquid));
            if(!added) {
                CraftTweakerAPI.logError(String.format("Could not add recipe for %s -> %s", inState, outLiquid.toCommandString()));
            }
        }
        
        @Override
        protected String getRecipeInfo() {
            return outLiquid.toCommandString();
        }
    }
    
    private static final class ActionAddTappingLeaf extends BaseAction {
        
        private final IBlockState logState;
        private final IBlockState leafState;
        
        private ActionAddTappingLeaf(IBlockState logState, IBlockState leafState) {
            super("Tapping");
            this.logState = logState;
            this.leafState = leafState;
        }
        
        @Override
        public void apply() {
            final boolean added = TapperManager.addLeafMapping(CraftTweakerMC.getBlockState(logState), CraftTweakerMC.getBlockState(leafState));
            if(!added) {
                CraftTweakerAPI.logError(String.format("Could not add recipe for %s -> %s", logState, leafState));
            }
        }
        
        @Override
        protected String getRecipeInfo() {
            return String.format("the mapping %s -> %s", logState.toString(), logState.toString());
        }
    }
}
