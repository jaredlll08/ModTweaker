package com.blamejared.compat.tconstruct.materials;

import crafttweaker.annotations.ZenRegister;
import stanhebben.zenscript.annotations.*;

@ZenClass("modtweaker.tconstruct.ITICMaterial")
@ZenRegister
public interface ITICMaterial {
    
    @ZenGetter
    String getName();
    
    @ZenMethod
    boolean matches(ITICMaterial var1);
    
    Object getInternal();
    
    @ZenGetter("definition")
    ITICMaterialDefinition getDefinition();
    
    @ZenSetter("durabilityHead")
    void setDurabilityHead(int durability);
    
    @ZenGetter("durabilityHead")
    int getDurabilityHead();
    
    @ZenSetter("miningSpeedHead")
    void setMiningSpeedHead(float miningSpeed);
    
    @ZenGetter("miningSpeedHead")
    float getMiningSpeedHead();
    
    @ZenSetter("attackHead")
    void setAttackHead(float attack);
    
    @ZenGetter("attackHead")
    float getAttackHead();
    
    @ZenSetter("harvestLevelHead")
    void setHarvestLevelHead(int level);
    
    @ZenGetter("harvestLevelHead")
    int getHarvestLevelHead();
    
    
    @ZenSetter("durabilityHandle")
    void setDurabilityHandle(int durability);
    
    @ZenGetter("durabilityHandle")
    int getDurabilityHandle();
    
    @ZenSetter("modifierHandle")
    void setModifierHandle(float modifier);
    
    @ZenGetter("modifierHandle")
    float getModifierHandle();
    
    @ZenSetter("durabilityExtra")
    void setDurabilityExtra(int durability);
    
    @ZenGetter("durabilityExtra")
    int getDurabilityExtra();
    
    
    @ZenSetter("arrowModifier")
    void setArrowModifier(float modifier);
    
    @ZenGetter("arrowModifier")
    float getArrowModifier();
    
    @ZenSetter("arrowBonusAmmo")
    void setArrowBonusAmmo(int bonusAmmo);
    
    @ZenGetter("arrowBonusAmmo")
    int getArrowBonusAmmo();
    
    @ZenSetter("fletchingModifier")
    void setFletchingModifier(float modifier);
    
    @ZenGetter("fletchingModifier")
    float getFletchingModifier();
    
    @ZenSetter("fletchingAccuracy")
    void setFletchingAccuracy(float accuracy);
    
    @ZenGetter("fletchingAccuracy")
    float getFletchingAccuracy();
    
    @ZenGetter("bowDrawSpeed")
    float getBowDrawSpeed();
    
    @ZenSetter("bowDrawSpeed")
    void setBowDrawSpeed(float drawSpeed);
    
    @ZenGetter("bowRange")
    float getBowRange();
    
    @ZenSetter("bowRange")
    void setBowRange(float range);
    
    @ZenGetter("bonusDamage")
    float getBonusDamage();
    
    @ZenSetter("bonusDamage")
    void setBonusDamage(float bonusDamage);
    
    @ZenGetter("bowStringModifier")
    float getBowStringModifier();
    
    @ZenSetter("bowStringModifier")
    void setBowStringModifier(float modifier);
    
    
    
}