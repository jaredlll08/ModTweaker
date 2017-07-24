package com.blamejared.compat.tconstruct.actions;

import minetweaker.IUndoableAction;
import com.blamejared.brackets.util.IMaterial;
import slimeknights.tconstruct.library.materials.*;

/**
 * Created by Jared on 6/16/2016.
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
        this.oldValue = ((HeadMaterialStats) ((Material) material.getInternal()).getStats("head")).durability;
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
        return "Setting durability of " + material.getName() + " to " + newValue + " for " + stat;
    }

    @Override
    public String describeUndo() {
        return "Reverting durability of " + material.getName() + " to " + oldValue + " for " + stat;
    }

    @Override
    public Object getOverrideKey() {
        return null;
    }
}
