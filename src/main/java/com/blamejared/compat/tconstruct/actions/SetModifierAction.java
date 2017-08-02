package com.blamejared.compat.tconstruct.actions;

import minetweaker.IUndoableAction;
import com.blamejared.brackets.util.IMaterial;
import slimeknights.tconstruct.library.materials.*;

/**
 * Created by Jared on 6/16/2016.
 * Adapted by Rinart73 on 24.07.17 for 1.11.2
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
        IMaterialStats oldStat = ((Material) material.getInternal()).getStats(stat);
        if (oldStat instanceof HandleMaterialStats)
            this.oldValue = ((HandleMaterialStats) oldStat).modifier;
        else if (oldStat instanceof BowStringMaterialStats)
            this.oldValue = ((BowStringMaterialStats) oldStat).modifier;
        else if (oldStat instanceof ArrowShaftMaterialStats)
            this.oldValue = ((ArrowShaftMaterialStats) oldStat).modifier;
        else if (oldStat instanceof FletchingMaterialStats)
            this.oldValue = ((FletchingMaterialStats) oldStat).modifier;
        else
            this.oldValue = -1;
    }

    private void set(Material material, String stat, float modifier) {
        IMaterialStats oldStat = material.getStats(stat);
        if (oldStat instanceof HandleMaterialStats) {
            HandleMaterialStats handleStat = (HandleMaterialStats) oldStat;
            HandleMaterialStats newHandle = new HandleMaterialStats(modifier, handleStat.durability);
            material.addStats(newHandle);
        } else if (oldStat instanceof BowStringMaterialStats) {
            BowStringMaterialStats newBowString = new BowStringMaterialStats(modifier);
            material.addStats(newBowString);
        } else if (oldStat instanceof ArrowShaftMaterialStats) {
            ArrowShaftMaterialStats arrowShaftStat = (ArrowShaftMaterialStats) oldStat;
            ArrowShaftMaterialStats newArrowShaft = new ArrowShaftMaterialStats(modifier, arrowShaftStat.bonusAmmo);
            material.addStats(newArrowShaft);
        } else if (oldStat instanceof FletchingMaterialStats) {
            FletchingMaterialStats fletchingStat = (FletchingMaterialStats) oldStat;
            FletchingMaterialStats newFletching = new FletchingMaterialStats(fletchingStat.accuracy, modifier);
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
