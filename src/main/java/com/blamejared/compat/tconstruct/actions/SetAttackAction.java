package com.blamejared.compat.tconstruct.actions;

import com.blamejared.compat.tconstruct.materials.IMaterial;
import crafttweaker.IAction;
import slimeknights.tconstruct.library.materials.HeadMaterialStats;
import slimeknights.tconstruct.library.materials.IMaterialStats;
import slimeknights.tconstruct.library.materials.Material;

public class SetAttackAction implements IAction {
    
    private final IMaterial material;
    private final String stat;
    private final float newValue;
    
    public SetAttackAction(IMaterial material, String stat, float newValue) {
        this.material = material;
        this.stat = stat;
        this.newValue = newValue;
    }
    
    private static void set(Material material, String stat, float attack) {
        IMaterialStats oldStat = material.getStats(stat);
        if(oldStat instanceof HeadMaterialStats) {
            HeadMaterialStats headStat = (HeadMaterialStats) oldStat;
            HeadMaterialStats newHead = new HeadMaterialStats(headStat.durability, headStat.miningspeed, attack, headStat.harvestLevel);
            material.addStats(newHead);
        }
        
    }
    
    @Override
    public void apply() {
        set((Material) material.getInternal(), stat, newValue);
    }
    
    @Override
    public String describe() {
        return "Setting Attack of " + material.getName() + " to " + newValue + " for " + stat;
    }
    
}