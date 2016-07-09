package modtweaker2.brackets.util;

import minetweaker.MineTweakerAPI;
import modtweaker2.mods.tconstruct.actions.*;
import slimeknights.tconstruct.library.materials.HandleMaterialStats;
import slimeknights.tconstruct.library.materials.HeadMaterialStats;
import slimeknights.tconstruct.library.materials.Material;

/**
 * Created by Jared on 6/16/2016.
 */
public class MCMaterial implements IMaterial {
    private final Material material;

    public MCMaterial(Material material) {
        this.material = material;
        System.out.println("added: " + material.getIdentifier());
    }

    @Override
    public String getName() {
        return material.getIdentifier();
    }

    @Override
    public boolean matches(IMaterial var1) {
        return var1.getName().equals(getName());
    }

    @Override
    public Object getInternal() {
        return material;
    }

    @Override
    public IMaterialDefinition getDefinition() {
        return new MCMaterialDefinition(material);
    }

    @Override
    public void setDurabilityHead(int durability) {
        MineTweakerAPI.apply(new SetDurabilityAction(this, "head", durability));
    }

    @Override
    public int getDurabilityHead() {
        return ((HeadMaterialStats) material.getStats("head")).durability;
    }

    @Override
    public void setMiningSpeedHead(float miningSpeed) {
        MineTweakerAPI.apply(new SetMiningSpeedAction(this, "head", miningSpeed));
    }

    @Override
    public float getMiningSpeedHead() {
        return ((HeadMaterialStats) material.getStats("head")).miningspeed;
    }

    @Override
    public void setAttackHead(float attack) {
        MineTweakerAPI.apply(new SetAttackAction(this, "head", attack));
    }

    @Override
    public float getAttackHead() {
        return ((HeadMaterialStats) material.getStats("head")).attack;
    }


    @Override
    public void setHarvestLevelHead(int level) {
        MineTweakerAPI.apply(new SetHarvestLevelAction(this, "head", level));
    }

    @Override
    public int getHarvestLevelHead() {
        return ((HeadMaterialStats) material.getStats("head")).harvestLevel;
    }

    @Override
    public void setDurabilityHandle(int durability) {
        MineTweakerAPI.apply(new SetDurabilityAction(this, "handle", durability));
    }

    @Override
    public int getDurabilityHandle() {
        return ((HandleMaterialStats) material.getStats("handle")).durability;
    }

    @Override
    public void setModifierHandle(float modifier) {
        MineTweakerAPI.apply(new SetModifierAction(this, "handle", modifier));
    }

    @Override
    public float getModifierHandle() {
        return ((HandleMaterialStats) material.getStats("handle")).modifier;
    }

    @Override
    public void setDurabilityExtra(int durability) {
        MineTweakerAPI.apply(new SetDurabilityAction(this, "extra", durability));
    }

    @Override
    public int getDurabilityExtra() {
        return ((HandleMaterialStats) material.getStats("extra")).durability;
    }

}
