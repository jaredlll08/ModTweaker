package com.blamejared.brackets.util;

import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenGetter;
import stanhebben.zenscript.annotations.ZenMethod;
import stanhebben.zenscript.annotations.ZenSetter;

/**
 * Created by Jared on 6/16/2016.
 * Adapted by Rinart73 on 7/26/2017 for 1.11.2
 */
@ZenClass("modtweaker.materials.IMaterial")
public interface IMaterial {

    @ZenGetter("name")
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


    @ZenSetter("drawspeedBow")
    void setDrawspeedBow(float drawspeed);

    @ZenGetter("drawspeedBow")
    float getDrawspeedBow();

    @ZenSetter("rangeBow")
    void setRangeBow(float range);

    @ZenGetter("rangeBow")
    float getRangeBow();

    @ZenSetter("bonusDamageBow")
    void setBonusDamageBow(float bonusDamage);

    @ZenGetter("bonusDamageBow")
    float getBonusDamageBow();


    @ZenSetter("modifierBowString")
    void setModifierBowString(float modifier);

    @ZenGetter("modifierBowString")
    float getModifierBowString();


    @ZenSetter("modifierArrowShaft")
    void setModifierArrowShaft(float modifier);

    @ZenGetter("modifierArrowShaft")
    float getModifierArrowShaft();

    @ZenSetter("bonusAmmoArrowShaft")
    void setBonusAmmoArrowShaft(int bonusAmmo);

    @ZenGetter("bonusAmmoArrowShaft")
    int getBonusAmmoArrowShaft();


    @ZenSetter("accuracyFletching")
    void setAccuracyFletching(float accuracy);

    @ZenGetter("accuracyFletching")
    float getAccuracyFletching();

    @ZenGetter("modifierFletching")
    float getModifierFletching();

    @ZenSetter("modifierFletching")
    void setModifierFletching(float modifier);

}
