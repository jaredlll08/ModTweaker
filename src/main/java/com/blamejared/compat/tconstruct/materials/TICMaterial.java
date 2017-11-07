package com.blamejared.compat.tconstruct.materials;

import com.blamejared.compat.tconstruct.actions.*;
import crafttweaker.CraftTweakerAPI;
import slimeknights.tconstruct.library.materials.HandleMaterialStats;
import slimeknights.tconstruct.library.materials.HeadMaterialStats;
import slimeknights.tconstruct.library.materials.Material;

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
        return ((HandleMaterialStats) material.getStats("extra")).durability;
    }
    
}