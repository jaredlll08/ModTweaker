package com.blamejared.compat.tconstruct.actions;

import com.blamejared.compat.tconstruct.materials.ITICMaterial;
import crafttweaker.IAction;
import slimeknights.tconstruct.library.materials.HeadMaterialStats;
import slimeknights.tconstruct.library.materials.IMaterialStats;
import slimeknights.tconstruct.library.materials.Material;

public class SetHarvestLevelAction implements IAction {
    
    private final ITICMaterial material;
    private final String stat;
    private final int newValue;
    
    public SetHarvestLevelAction(ITICMaterial material, String stat, int newValue) {
        this.material = material;
        this.stat = stat;
        this.newValue = newValue;
    }
    
    private static void set(Material material, String stat, int level) {
        IMaterialStats oldStat = material.getStats(stat);
        if(oldStat instanceof HeadMaterialStats) {
            HeadMaterialStats headStat = (HeadMaterialStats) oldStat;
            HeadMaterialStats newHead = new HeadMaterialStats(headStat.durability, headStat.miningspeed, headStat.attack, level);
            material.addStats(newHead);
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