package com.blamejared.compat.tconstruct.actions;

import com.blamejared.compat.tconstruct.materials.ITICMaterial;
import crafttweaker.IAction;
import slimeknights.tconstruct.library.materials.*;

public class SetDurabilityAction implements IAction {
    
    private final ITICMaterial material;
    private final String stat;
    private final int newValue;
    
    public SetDurabilityAction(ITICMaterial material, String stat, int newValue) {
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
        }else if(oldStat instanceof ArrowShaftMaterialStats){
            ArrowShaftMaterialStats old = (ArrowShaftMaterialStats) oldStat;
            ArrowShaftMaterialStats newStat = new ArrowShaftMaterialStats(old.modifier, durability);
            material.addStats(newStat);
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