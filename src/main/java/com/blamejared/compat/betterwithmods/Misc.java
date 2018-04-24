package com.blamejared.compat.betterwithmods;

import betterwithmods.common.registry.BellowsManager;
import betterwithmods.module.hardcore.crafting.HCFurnace;
import betterwithmods.util.item.Stack;
import betterwithmods.util.item.StackMap;
import com.blamejared.ModTweaker;
import com.blamejared.mtlib.utils.BaseMapAddition;
import com.google.common.collect.Maps;
import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.mc1120.CraftTweaker;
import net.minecraft.item.crafting.Ingredient;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@ZenClass("mods.betterwithmods.Misc")
@ModOnly("betterwithmods")
@ZenRegister
public class Misc {
    @ZenMethod
    public static void setFurnaceSmeltingTime(IIngredient ingredient, int time) {
        HashMap<Ingredient,Integer> map = new HashMap<>();
        map.put(CraftTweakerMC.getIngredient(ingredient),time);
        ModTweaker.LATE_ADDITIONS.add(new SetSmeltingTime(map));
    }

    public static class SetSmeltingTime extends BaseMapAddition<Ingredient,Integer> {

        protected SetSmeltingTime(Map<Ingredient,Integer> map) {
            super("Set HCFurnace Smelting Time", HCFurnace.FURNACE_TIMINGS, map);
        }

        @Override
        protected String getRecipeInfo(Map.Entry<Ingredient,Integer> recipe) {
            return Arrays.toString(recipe.getKey().getMatchingStacks()) + " -> " + recipe.getValue(); //Fortunately mojang is adding a toString method to Ingredient
        }
    }
}
