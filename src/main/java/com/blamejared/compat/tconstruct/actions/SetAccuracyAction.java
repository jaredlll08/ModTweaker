package com.blamejared.compat.tconstruct.actions;

import com.blamejared.brackets.util.IMaterial;
import minetweaker.IUndoableAction;
import slimeknights.tconstruct.library.materials.FletchingMaterialStats;
import slimeknights.tconstruct.library.materials.IMaterialStats;
import slimeknights.tconstruct.library.materials.Material;

/**
 * Created by Rinart73 on 7/25/2017.
 */
public class SetAccuracyAction implements IUndoableAction {

    private final IMaterial material;
    private final String stat;
    private final float newValue;
    private final float oldValue;

    public SetAccuracyAction(IMaterial material, String stat, float newValue) {
        this.material = material;
        this.stat = stat;
        this.newValue = newValue;
        this.oldValue = ((FletchingMaterialStats) ((Material) material.getInternal()).getStats(stat)).accuracy;
    }

    private void set(Material material, String stat, float accuracy) {
        IMaterialStats oldStat = material.getStats(stat);
        if (oldStat instanceof FletchingMaterialStats) {
            FletchingMaterialStats fletchingStat = (FletchingMaterialStats) oldStat;
            FletchingMaterialStats newFletching = new FletchingMaterialStats(accuracy, fletchingStat.modifier);
            material.addStats(newFletching);
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
        return "Setting Accuracy of " + material.getName() + " to " + newValue + " for " + stat;
    }

    @Override
    public String describeUndo() {
        return "Reverting Accuracy of " + material.getName() + " to " + oldValue + " for " + stat;
    }

    @Override
    public Object getOverrideKey() {
        return null;
    }
}
