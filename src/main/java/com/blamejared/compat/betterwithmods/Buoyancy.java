package com.blamejared.compat.betterwithmods;

import betterwithmods.module.hardcore.HCBuoy;
import betterwithmods.util.item.Stack;
import betterwithmods.util.item.StackMap;
import com.blamejared.ModTweaker;
import com.blamejared.api.annotations.Handler;
import com.blamejared.mtlib.helpers.InputHelper;
import com.blamejared.mtlib.utils.BaseMapAddition;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.*;
import crafttweaker.api.item.IItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.Map;

@ZenClass("mods.betterwithmods.Buoyancy")
@ModOnly("betterwithmods")
@ZenRegister
public class Buoyancy {
    
    @ZenMethod
    public static void set(IItemStack stack, float value) {
        StackMap<Float> map = new StackMap(1.0);
        map.put(new Stack(InputHelper.toStack(stack)), value);
        CraftTweakerAPI.apply(new Set(map));
    }
    
    public static class Set extends BaseMapAddition<Stack, Float> {
        
        protected Set(StackMap<Float> map) {
            super("Set Item Buoyancy", HCBuoy.buoyancy, map);
        }
        
        @Override
        protected String getRecipeInfo(Map.Entry<Stack, Float> recipe) {
            return recipe.getKey().toString() + " -> " + recipe.getValue();
        }
        
    }
}
