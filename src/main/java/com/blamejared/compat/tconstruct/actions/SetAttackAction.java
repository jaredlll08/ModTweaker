package com.blamejared.compat.tconstruct.actions;

import com.blamejared.compat.tconstruct.materials.ITICMaterial;
import crafttweaker.IAction;
import slimeknights.tconstruct.library.materials.*;

public class SetAttackAction implements IAction {
    
    private final ITICMaterial material;
    private final String stat;
    private final float newValue;
    
    public SetAttackAction(ITICMaterial material, String stat, float newValue) {
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
        } else if(oldStat instanceof FletchingMaterialStats) {
            FletchingMaterialStats fletch = (FletchingMaterialStats) oldStat;
            FletchingMaterialStats newShaft = new FletchingMaterialStats(attack, fletch.modifier);
            material.addStats(newShaft);
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