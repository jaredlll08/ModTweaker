package com.blamejared.compat.tconstruct.actions;

import com.blamejared.compat.tconstruct.materials.IMaterial;
import crafttweaker.IAction;
import slimeknights.tconstruct.library.materials.*;

public class SetDurabilityAction implements IAction {
    
    private final IMaterial material;
    private final String stat;
    private final int newValue;
    
    public SetDurabilityAction(IMaterial material, String stat, int newValue) {
        this.material = material;
        this.stat = stat;
        this.newValue = newValue;
    }
    
    private static void set(Material material, String stat, int durability) {
        IMaterialStats oldStat = material.getStats(stat);
        if(oldStat instanceof HeadMaterialStats) {
            HeadMaterialStats headStat = (HeadMaterialStats) oldStat;
            HeadMaterialStats newHead = new HeadMaterialStats(durability, headStat.miningspeed, headStat.attack, headStat.harvestLevel);
            material.addStats(newHead);
        } else if(oldStat instanceof HandleMaterialStats) {
            HandleMaterialStats handleStat = (HandleMaterialStats) oldStat;
            HandleMaterialStats newHandle = new HandleMaterialStats(handleStat.modifier, durability);
            material.addStats(newHandle);
        } else if(oldStat instanceof ExtraMaterialStats) {
            ExtraMaterialStats newExtra = new ExtraMaterialStats(durability);
            material.addStats(newExtra);
        }
        
    }
    
    @Override
    public void apply() {
        set((Material) material.getInternal(), stat, newValue);
    }
    
    
    @Override
    public String describe() {
        return "Setting durability of " + material.getName() + " to " + newValue + " for " + stat;
    }
    
}