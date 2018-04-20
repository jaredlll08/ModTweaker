package com.blamejared.compat.betterwithmods;

import betterwithmods.common.registry.BellowsManager;
import betterwithmods.module.hardcore.world.HCBuoy;
import betterwithmods.util.item.Stack;
import betterwithmods.util.item.StackMap;
import com.blamejared.ModTweaker;
import com.blamejared.mtlib.helpers.InputHelper;
import com.blamejared.mtlib.utils.BaseMapAddition;
import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.Map;

@ZenClass("mods.betterwithmods.Bellows")
@ModOnly("betterwithmods")
@ZenRegister
public class Bellows {
    
    @ZenMethod
    public static void set(IItemStack stack, float value) {
        StackMap<Float> map = new StackMap<>(1.0f);
        map.put(new Stack(InputHelper.toStack(stack)), value);
        ModTweaker.LATE_ADDITIONS.add(new Set(map));
    }
    
    public static class Set extends BaseMapAddition<Stack, Float> {
        
        protected Set(StackMap<Float> map) {
            super("Set Bellows Item Weight", BellowsManager.bellowing, map);
        }
        
        @Override
        protected String getRecipeInfo(Map.Entry<Stack, Float> recipe) {
            return recipe.getKey().toString() + " -> " + recipe.getValue();
        }
    }
}
