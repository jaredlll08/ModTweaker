package com.blamejared.compat.tconstruct.materials;

import crafttweaker.annotations.ZenRegister;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenGetter;
import stanhebben.zenscript.annotations.ZenMethod;
import stanhebben.zenscript.annotations.ZenSetter;

@ZenClass("modtweaker.materials.IMaterial")
@ZenRegister
public interface IMaterial {

    @ZenGetter
    String getName();

    @ZenMethod
    boolean matches(IMaterial var1);

    Object getInternal();

    @ZenGetter("definition")
    IMaterialDefinition getDefinition();

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

}