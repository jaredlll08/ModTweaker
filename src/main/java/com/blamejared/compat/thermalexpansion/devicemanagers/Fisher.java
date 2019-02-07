package com.blamejared.compat.thermalexpansion.devicemanagers;

import cofh.thermalexpansion.util.managers.device.FisherManager;
import com.blamejared.ModTweaker;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.item.WeightedItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.thermalexpansion.Fisher")
@ModOnly("thermalexpansion")
@ZenRegister
public class Fisher {
    
    
    @ZenMethod
    public static void addBait(IItemStack baitItem, int multiplier) {
        //Weight by default 2,3,4 for TE bait
        ModTweaker.LATE_ADDITIONS.add(new ActionAddFisherManager(true, baitItem, multiplier));
    }
    
    @ZenMethod
    public static void addFish(IItemStack fish, int weight) {
        //Weight by default 120,50,4,26 for Vanilla fish with meta 0,1,2,3, respective
        ModTweaker.LATE_ADDITIONS.add(new ActionAddFisherManager(false, fish, weight));
    }
    
    //#########################################//
    //    TE does not expose Remove methods    //
    //#########################################//
    
    private static final class ActionAddFisherManager implements IAction {
        
        private final boolean isBait;
        private final IItemStack itemStack;
        private final int weightOrMultiplier;
        
        
        private ActionAddFisherManager(boolean isBait, IItemStack itemStack, int weightOrMultiplier) {
            this.isBait = isBait;
            this.itemStack = itemStack;
            this.weightOrMultiplier = weightOrMultiplier;
        }
        
        @Override
        public void apply() {
            final ItemStack itemStack = CraftTweakerMC.getItemStack(this.itemStack);
            if(isBait)
                FisherManager.addBait(itemStack, weightOrMultiplier);
            else if(!FisherManager.addFish(itemStack, weightOrMultiplier))
                CraftTweakerAPI.logError("Could not add Fish entry for " + this.itemStack.toCommandString() + " with weight " + weightOrMultiplier);
            
        }
        
        @Override
        public String describe() {
            return String.format("Adding %s entry for %s with %s %d", isBait ? "bait" : "fish", itemStack.toCommandString(), isBait ? "multiplier" : "weight", weightOrMultiplier);
        }
    }
}
