package com.blamejared.compat.botania.handlers;

import com.blamejared.ModTweaker;
import crafttweaker.*;
import crafttweaker.annotations.*;
import crafttweaker.api.oredict.IOreDictEntry;
import stanhebben.zenscript.annotations.*;
import vazkii.botania.api.BotaniaAPI;

@ZenClass("mods.botania.OrechidIgnem")
@ModOnly("botania")
@ZenRegister
public class OrechidIgnem {
    
    @ZenMethod
    public static void addOre(IOreDictEntry oreDict, int weight) {
        ModTweaker.LATE_ADDITIONS.add(new Add(oreDict.getName(), weight));
    }
    
    @ZenMethod
    public static void addOre(String oreDict, int weight) {
        ModTweaker.LATE_ADDITIONS.add(new Add(oreDict, weight));
    }
    
    @ZenMethod
    public static void removeOre(IOreDictEntry oreDict) {
        ModTweaker.LATE_REMOVALS.add(new Remove(oreDict.getName()));
    }
    
    @ZenMethod
    public static void removeOre(String oreDict) {
        ModTweaker.LATE_REMOVALS.add(new Remove(oreDict));
    }
    
    
    private static class Add implements IAction {
        
        String oreDict;
        int weight;
        
        public Add(String ore, int prop) {
            oreDict = ore;
            weight = prop;
        }
        
        @Override
        public void apply() {
            BotaniaAPI.addOreWeightNether(oreDict, weight);
        }
        
        @Override
        public String describe() {
            return "Adding OrechidIgnem Ore Weight: " + oreDict + ":" + weight;
        }
        
    }
    
    private static class Remove implements IAction {
        
        String oreDict;
        
        public Remove(String ore) {
            oreDict = ore;
        }
        
        @Override
        public void apply() {
            BotaniaAPI.oreWeightsNether.remove(oreDict);
        }
        
        @Override
        public String describe() {
            return "Removing Orechid Ore: " + oreDict;
        }
    }
}
