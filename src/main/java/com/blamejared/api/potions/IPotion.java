package com.blamejared.api.potions;

import stanhebben.zenscript.annotations.*;

@ZenClass("modtweaker.api.IPotion")
public interface IPotion {
    
    @ZenGetter("name")
    String name();
    
    @ZenGetter("badEffect")
    boolean isBadEffect();
    
    @ZenGetter("liquidColor")
    int getLiquidColor();
    
    Object getInternal();
}
