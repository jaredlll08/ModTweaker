package com.blamejared.compat.tconstruct.actions;

import com.blamejared.brackets.util.IMaterial;
import minetweaker.IUndoableAction;
import slimeknights.tconstruct.library.materials.ArrowShaftMaterialStats;
import slimeknights.tconstruct.library.materials.IMaterialStats;
import slimeknights.tconstruct.library.materials.Material;

/**
 * Created by Rinart73 on 7/25/2017.
 */
public class SetBonusAmmoAction implements IUndoableAction {

    private final IMaterial material;
    private final String stat;
    private final int newValue;
    private final int oldValue;

    public SetBonusAmmoAction(IMaterial material, String stat, int newValue) {
        this.material = material;
        this.stat = stat;
        this.newValue = newValue;
        this.oldValue = ((ArrowShaftMaterialStats) ((Material) material.getInternal()).getStats(stat)).bonusAmmo;
    }

    private static void set(Material material, String stat, int bonusAmmo) {
        IMaterialStats oldStat = material.getStats(stat);
        if (oldStat instanceof ArrowShaftMaterialStats) {
            ArrowShaftMaterialStats arrowShaftStat = (ArrowShaftMaterialStats) oldStat;
            ArrowShaftMaterialStats newArrowShaft = new ArrowShaftMaterialStats(arrowShaftStat.modifier, bonusAmmo);
            material.addStats(newArrowShaft);
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
        return "Setting Bonus Ammo of " + material.getName() + " to " + newValue + " for " + stat;
    }

    @Override
    public String describeUndo() {
        return "Reverting Bonus Ammo of " + material.getName() + " to " + oldValue + " for " + stat;
    }

    @Override
    public Object getOverrideKey() {
        return null;
    }
}
