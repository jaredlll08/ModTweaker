package com.blamejared.compat.botania.handlers;

import com.blamejared.ModTweaker;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.oredict.IOreDictEntry;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import vazkii.botania.api.BotaniaAPI;

@ZenClass("mods.botania.Orechid")
@ModOnly("botania")
@ZenRegister
public class Orechid {
    
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
            BotaniaAPI.addOreWeight(oreDict, weight);
        }
        
        @Override
        public String describe() {
            return "Adding Orechid Ore Weight: " + oreDict + ":" + weight;
        }
        
    }
    
    private static class Remove implements IAction {
        
        String oreDict;
        
        public Remove(String ore) {
            oreDict = ore;
        }
        
        @Override
        public void apply() {
            BotaniaAPI.oreWeights.remove(oreDict);
        }
        
        @Override
        public String describe() {
            return "Removing Orechid Ore: " + oreDict;
        }
    }
}
