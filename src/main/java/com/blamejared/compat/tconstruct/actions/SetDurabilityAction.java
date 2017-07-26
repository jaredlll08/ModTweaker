package com.blamejared.compat.tconstruct.actions;

import minetweaker.IUndoableAction;
import com.blamejared.brackets.util.IMaterial;
import slimeknights.tconstruct.library.materials.*;

/**
 * Created by Jared on 6/16/2016.
 * Adapted by Rinart73 on 24.07.17 for 1.11.2
 */
public class SetDurabilityAction implements IUndoableAction {

    private final IMaterial material;
    private final String stat;
    private final int newValue;
    private final int oldValue;

    public SetDurabilityAction(IMaterial material, String stat, int newValue) {
        this.material = material;
        this.stat = stat;
        this.newValue = newValue;
        IMaterialStats oldStat = ((Material) material.getInternal()).getStats(stat);
        if (oldStat instanceof HeadMaterialStats)
            this.oldValue = ((HeadMaterialStats) oldStat).durability;
        else if (oldStat instanceof HandleMaterialStats)
            this.oldValue = ((HandleMaterialStats) oldStat).durability;
        else if (oldStat instanceof ExtraMaterialStats)
            this.oldValue = ((ExtraMaterialStats) oldStat).extraDurability;
        else
            this.oldValue = -1;
    }

    private static void set(Material material, String stat, int durability) {
        IMaterialStats oldStat = material.getStats(stat);
        if (oldStat instanceof HeadMaterialStats) {
            HeadMaterialStats headStat = (HeadMaterialStats) oldStat;
            HeadMaterialStats newHead = new HeadMaterialStats(durability, headStat.miningspeed, headStat.attack, headStat.harvestLevel);
            material.addStats(newHead);
        } else if (oldStat instanceof HandleMaterialStats) {
            HandleMaterialStats handleStat = (HandleMaterialStats) oldStat;
            HandleMaterialStats newHandle = new HandleMaterialStats(handleStat.modifier, durability);
            material.addStats(newHandle);
        } else if (oldStat instanceof ExtraMaterialStats) {
            ExtraMaterialStats newExtra = new ExtraMaterialStats(durability);
            material.addStats(newExtra);
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
        set((Material) material.getInternal(),stat, oldValue);
    }

    @Override
    public String describe() {
        return "Setting Durability of " + material.getName() + " to " + newValue + " for " + stat;
    }

    @Override
    public String describeUndo() {
        return "Reverting Durability of " + material.getName() + " to " + oldValue + " for " + stat;
    }

    @Override
    public Object getOverrideKey() {
        return null;
    }
}
