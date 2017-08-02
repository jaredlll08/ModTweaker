package com.blamejared.brackets.util;

import minetweaker.MineTweakerAPI;
import com.blamejared.compat.tconstruct.actions.*;
import slimeknights.tconstruct.library.materials.*;

/**
 * Created by Jared on 6/16/2016.
 * Adapted by Rinart73 on 7/26/2017 for 1.11.2
 */
public class MCMaterial implements IMaterial {
    private final Material material;

    public MCMaterial(Material material) {
        this.material = material;
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
        MineTweakerAPI.apply(new SetDurabilityAction(this, MaterialTypes.HEAD, durability));
    }

    @Override
    public int getDurabilityHead() {
        return ((HeadMaterialStats) material.getStats(MaterialTypes.HEAD)).durability;
    }

    @Override
    public void setMiningSpeedHead(float miningSpeed) {
        MineTweakerAPI.apply(new SetMiningSpeedAction(this, MaterialTypes.HEAD, miningSpeed));
    }

    @Override
    public float getMiningSpeedHead() {
        return ((HeadMaterialStats) material.getStats(MaterialTypes.HEAD)).miningspeed;
    }

    @Override
    public void setAttackHead(float attack) {
        MineTweakerAPI.apply(new SetAttackAction(this, MaterialTypes.HEAD, attack));
    }

    @Override
    public float getAttackHead() {
        return ((HeadMaterialStats) material.getStats(MaterialTypes.HEAD)).attack;
    }

    @Override
    public void setHarvestLevelHead(int level) {
        MineTweakerAPI.apply(new SetHarvestLevelAction(this, MaterialTypes.HEAD, level));
    }

    @Override
    public int getHarvestLevelHead() {
        return ((HeadMaterialStats) material.getStats(MaterialTypes.HEAD)).harvestLevel;
    }


    @Override
    public void setDurabilityHandle(int durability) {
        MineTweakerAPI.apply(new SetDurabilityAction(this, MaterialTypes.HANDLE, durability));
    }

    @Override
    public int getDurabilityHandle() {
        return ((HandleMaterialStats) material.getStats(MaterialTypes.HANDLE)).durability;
    }

    @Override
    public void setModifierHandle(float modifier) {
        MineTweakerAPI.apply(new SetModifierAction(this, MaterialTypes.HANDLE, modifier));
    }

    @Override
    public float getModifierHandle() {
        return ((HandleMaterialStats) material.getStats(MaterialTypes.HANDLE)).modifier;
    }


    @Override
    public void setDurabilityExtra(int durability) {
        MineTweakerAPI.apply(new SetDurabilityAction(this, MaterialTypes.EXTRA, durability));
    }

    @Override
    public int getDurabilityExtra() {
        return ((ExtraMaterialStats) material.getStats(MaterialTypes.EXTRA)).extraDurability;
    }


    @Override
    public void setDrawspeedBow(float drawspeed) {
        MineTweakerAPI.apply(new SetDrawspeedAction(this, MaterialTypes.BOW, drawspeed));
    }

    @Override
    public float getDrawspeedBow() {
        return ((BowMaterialStats) material.getStats(MaterialTypes.BOW)).drawspeed;
    }

    @Override
    public void setRangeBow(float range) {
        MineTweakerAPI.apply(new SetRangeAction(this, MaterialTypes.BOW, range));
    }

    @Override
    public float getRangeBow() {
        return ((BowMaterialStats) material.getStats(MaterialTypes.BOW)).range;
    }

    @Override
    public void setBonusDamageBow(float bonusDamage) {
        MineTweakerAPI.apply(new SetBonusDamageAction(this, MaterialTypes.BOW, bonusDamage));
    }

    @Override
    public float getBonusDamageBow() {
        return ((BowMaterialStats) material.getStats(MaterialTypes.BOW)).bonusDamage;
    }


    @Override
    public void setModifierBowString(float modifier) {
        MineTweakerAPI.apply(new SetModifierAction(this, MaterialTypes.BOWSTRING, modifier));
    }

    @Override
    public float getModifierBowString() {
        return ((HandleMaterialStats) material.getStats(MaterialTypes.BOWSTRING)).modifier;
    }


    @Override
    public void setModifierArrowShaft(float modifier) {
        MineTweakerAPI.apply(new SetModifierAction(this, MaterialTypes.SHAFT, modifier));
    }

    @Override
    public float getModifierArrowShaft() {
        return ((ArrowShaftMaterialStats) material.getStats(MaterialTypes.SHAFT)).modifier;
    }

    @Override
    public void setBonusAmmoArrowShaft(int bonusAmmo) {
        MineTweakerAPI.apply(new SetBonusAmmoAction(this, MaterialTypes.SHAFT, bonusAmmo));
    }

    @Override
    public int getBonusAmmoArrowShaft() {
        return ((ArrowShaftMaterialStats) material.getStats(MaterialTypes.SHAFT)).bonusAmmo;
    }


    @Override
    public void setAccuracyFletching(float accuracy) {
        MineTweakerAPI.apply(new SetAccuracyAction(this, MaterialTypes.FLETCHING, accuracy));
    }

    @Override
    public float getAccuracyFletching() {
        return ((FletchingMaterialStats) material.getStats(MaterialTypes.FLETCHING)).accuracy;
    }

    @Override
    public void setModifierFletching(float modifier) {
        MineTweakerAPI.apply(new SetModifierAction(this, MaterialTypes.FLETCHING, modifier));
    }

    @Override
    public float getModifierFletching() {
        return ((FletchingMaterialStats) material.getStats(MaterialTypes.FLETCHING)).modifier;
    }

}