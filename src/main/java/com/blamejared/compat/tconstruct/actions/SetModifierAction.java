package com.blamejared.compat.tconstruct.actions;

import minetweaker.IUndoableAction;
import com.blamejared.brackets.util.IMaterial;
import slimeknights.tconstruct.library.materials.HandleMaterialStats;
import slimeknights.tconstruct.library.materials.IMaterialStats;
import slimeknights.tconstruct.library.materials.Material;

/**
 * Created by Jared on 6/16/2016.
 */
public class SetModifierAction implements IUndoableAction {

    private final IMaterial material;
    private final String stat;
    private final float newValue;
    private final float oldValue;

    public SetModifierAction(IMaterial material, String stat, float newValue) {
        this.material = material;
        this.stat = stat;
        this.newValue = newValue;
        this.oldValue = ((HandleMaterialStats) ((Material) material.getInternal()).getStats("handle")).modifier;
    }

    private static void set(Material material, String stat, float modifier) {
        IMaterialStats oldStat = material.getStats(stat);
        if (oldStat instanceof HandleMaterialStats) {
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
    public boolean canUndo() {
        return true;
    }

    @Override
    public void undo() {
        set((Material) material.getInternal(), stat, oldValue);
    }

    @Override
    public String describe() {
        return "Setting Modifier of " + material.getName() + " to " + newValue + " for " + stat;
    }

    @Override
    public String describeUndo() {
        return "Reverting Modifier of " + material.getName() + " to " + oldValue + " for " + stat;
    }

    @Override
    public Object getOverrideKey() {
        return null;
    }
}
