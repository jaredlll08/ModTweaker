package com.blamejared.compat.tconstruct.actions;

import com.blamejared.compat.tconstruct.materials.IMaterial;
import crafttweaker.IAction;
import slimeknights.tconstruct.library.materials.HandleMaterialStats;
import slimeknights.tconstruct.library.materials.IMaterialStats;
import slimeknights.tconstruct.library.materials.Material;

public class SetModifierAction implements IAction {
    
    private final IMaterial material;
    private final String stat;
    private final float newValue;
    
    public SetModifierAction(IMaterial material, String stat, float newValue) {
        this.material = material;
        this.stat = stat;
        this.newValue = newValue;
    }
    
    private static void set(Material material, String stat, float modifier) {
        IMaterialStats oldStat = material.getStats(stat);
        if(oldStat instanceof HandleMaterialStats) {
            HandleMaterialStats handleStat = (HandleMaterialStats) oldStat;
            HandleMaterialStats newHandle = new HandleMaterialStats(modifier, handleStat.durability);
            material.addStats(newHandle);
        }
        
    }
    
    @Override
    public void apply() {
        set((Material) material.getInternal(), stat, newValue);
    }
    
    
    @Override
    public String describe() {
        return "Setting Modifier of " + material.getName() + " to " + newValue + " for " + stat;
    }
    
}