package com.blamejared.compat.tconstruct.materials;

import com.blamejared.compat.tconstruct.actions.*;
import crafttweaker.CraftTweakerAPI;
import slimeknights.tconstruct.library.materials.*;

public class TICMaterial implements ITICMaterial {
    
    private final Material material;
    
    public TICMaterial(Material material) {
        this.material = material;
    }
    
    @Override
    public String getName() {
        return material.getIdentifier();
    }
    
    @Override
    public boolean matches(ITICMaterial var1) {
        return var1.getName().equals(getName());
    }
    
    @Override
    public Object getInternal() {
        return material;
    }
    
    @Override
    public ITICMaterialDefinition getDefinition() {
        return new TICMaterialDefinition(material);
    }
    
    @Override
    public void setDurabilityHead(int durability) {
        CraftTweakerAPI.apply(new SetDurabilityAction(this, "head", durability));
    }
    
    @Override
    public int getDurabilityHead() {
        return ((HeadMaterialStats) material.getStats("head")).durability;
    }
    
    @Override
    public void setMiningSpeedHead(float miningSpeed) {
        CraftTweakerAPI.apply(new SetMiningSpeedAction(this, "head", miningSpeed));
    }
    
    @Override
    public float getMiningSpeedHead() {
        return ((HeadMaterialStats) material.getStats("head")).miningspeed;
    }
    
    @Override
    public void setAttackHead(float attack) {
        CraftTweakerAPI.apply(new SetAttackAction(this, "head", attack));
    }
    
    @Override
    public float getAttackHead() {
        return ((HeadMaterialStats) material.getStats("head")).attack;
    }
    
    
    @Override
    public void setHarvestLevelHead(int level) {
        CraftTweakerAPI.apply(new SetHarvestLevelAction(this, "head", level));
    }
    
    @Override
    public int getHarvestLevelHead() {
        return ((HeadMaterialStats) material.getStats("head")).harvestLevel;
    }
    
    @Override
    public void setDurabilityHandle(int durability) {
        CraftTweakerAPI.apply(new SetDurabilityAction(this, "handle", durability));
    }
    
    @Override
    public int getDurabilityHandle() {
        return ((HandleMaterialStats) material.getStats("handle")).durability;
    }
    
    @Override
    public void setModifierHandle(float modifier) {
        CraftTweakerAPI.apply(new SetModifierAction(this, "handle", modifier));
    }
    
    @Override
    public float getModifierHandle() {
        return ((HandleMaterialStats) material.getStats("handle")).modifier;
    }
    
    @Override
    public void setDurabilityExtra(int durability) {
        CraftTweakerAPI.apply(new SetDurabilityAction(this, "extra", durability));
    }
    
    @Override
    public int getDurabilityExtra() {
        return ((ExtraMaterialStats) material.getStats("extra")).extraDurability;
    }
    
    @Override
    public void setArrowModifier(float modifier) {
        CraftTweakerAPI.apply(new SetModifierAction(this, "shaft", modifier));
    }
    
    @Override
    public float getArrowModifier() {
        return ((ArrowShaftMaterialStats) material.getStats("shaft")).modifier;
    }
    
    @Override
    public void setArrowBonusAmmo(int bonusAmmo) {
        CraftTweakerAPI.apply(new SetDurabilityAction(this, "shaft", bonusAmmo));
    }
    
    @Override
    public int getArrowBonusAmmo() {
        return ((ArrowShaftMaterialStats) material.getStats("shaft")).bonusAmmo;
    }
    
    @Override
    public void setFletchingModifier(float modifier) {
        CraftTweakerAPI.apply(new SetModifierAction(this, "fletching", modifier));
    }
    
    @Override
    public float getFletchingModifier() {
        return ((FletchingMaterialStats) material.getStats("fletching")).modifier;
    }
    
    @Override
    public void setFletchingAccuracy(float accuracy) {
        CraftTweakerAPI.apply(new SetAttackAction(this, "fletching", accuracy));
    }
    
    @Override
    public float getFletchingAccuracy() {
        return ((FletchingMaterialStats) material.getStats("fletching")).accuracy;
    }
    
    @Override
    public float getBowDrawSpeed() {
        return ((BowMaterialStats) material.getStats("bow")).drawspeed;
    }
    
    @Override
    public void setBowDrawSpeed(float drawSpeed) {
        CraftTweakerAPI.apply(new SetDrawSpeedAction(this, "bow", drawSpeed));
    }
    
    @Override
    public float getBowRange() {
        return ((BowMaterialStats) material.getStats("bow")).range;
    }
    
    @Override
    public void setBowRange(float range) {
        CraftTweakerAPI.apply(new SetBowRangeAction(this, "bow", range));
    }
    
    @Override
    public float getBonusDamage() {
        return ((BowMaterialStats) material.getStats("bow")).bonusDamage;
    }
    
    @Override
    public void setBonusDamage(float bonusDamage) {
        CraftTweakerAPI.apply(new SetBowBonusDamageAction(this, "bow", bonusDamage));
    }
    
    @Override
    public float getBowStringModifier() {
        return ((BowStringMaterialStats) material.getStats("bowstring")).modifier;
        
    }
    
    @Override
    public void setBowStringModifier(float modifier) {
        CraftTweakerAPI.apply(new SetModifierAction(this, "bowstring", modifier));
        
    }
    
    
}