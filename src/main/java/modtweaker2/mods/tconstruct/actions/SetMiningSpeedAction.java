package modtweaker2.mods.tconstruct.actions;

import minetweaker.IUndoableAction;
import modtweaker2.brackets.util.IMaterial;
import slimeknights.tconstruct.library.materials.HeadMaterialStats;
import slimeknights.tconstruct.library.materials.IMaterialStats;
import slimeknights.tconstruct.library.materials.Material;

/**
 * Created by Jared on 6/16/2016.
 */
public class SetMiningSpeedAction implements IUndoableAction {

    private final IMaterial material;
    private final String stat;
    private final float newValue;
    private final float oldValue;

    public SetMiningSpeedAction(IMaterial material, String stat, float newValue) {
        this.material = material;
        this.stat = stat;
        this.newValue = newValue;
        this.oldValue = ((HeadMaterialStats) ((Material) material.getInternal()).getStats("head")).miningspeed;
    }

    private static void set(Material material, String stat, float miningSpeed) {
        IMaterialStats oldStat = material.getStats(stat);
        if (oldStat instanceof HeadMaterialStats) {
            HeadMaterialStats headStat = (HeadMaterialStats) oldStat;
            HeadMaterialStats newHead = new HeadMaterialStats(headStat.durability, miningSpeed, headStat.attack, headStat.harvestLevel);
            material.addStats(newHead);
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
        return "Setting MiningSpeed of " + material.getName() + " to " + newValue + " for " + stat;
    }

    @Override
    public String describeUndo() {
        return "Reverting MiningSpeed of " + material.getName() + " to " + oldValue + " for " + stat;
    }

    @Override
    public Object getOverrideKey() {
        return null;
    }
}
