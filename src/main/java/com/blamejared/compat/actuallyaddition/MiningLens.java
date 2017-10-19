package com.blamejared.compat.actuallyaddition;

import com.blamejared.ModTweaker;
import com.blamejared.mtlib.utils.*;
import crafttweaker.annotations.*;
import crafttweaker.api.oredict.IOreDictEntry;
import de.ellpeck.actuallyadditions.api.ActuallyAdditionsAPI;
import de.ellpeck.actuallyadditions.api.recipe.*;
import stanhebben.zenscript.annotations.*;

import java.util.*;

@ZenClass("mods.actuallyadditions.MiningLens")
@ModOnly("actuallyadditions")
@ZenRegister
public class MiningLens {
    
    @ZenMethod
    public static void addStoneOre(IOreDictEntry ore, int weight) {
        ModTweaker.LATE_ADDITIONS.add(new Add(ActuallyAdditionsAPI.STONE_ORES, Collections.singletonList(new WeightedOre(ore.getName(), weight))));
    }
    
    @ZenMethod
    public static void addNetherOre(IOreDictEntry ore, int weight) {
        ModTweaker.LATE_ADDITIONS.add(new Add(ActuallyAdditionsAPI.NETHERRACK_ORES, Collections.singletonList(new WeightedOre(ore.getName(), weight))));
    }
    
    @ZenMethod
    public static void removeStoneOre(IOreDictEntry ore) {
        ModTweaker.LATE_REMOVALS.add(new Remove(ActuallyAdditionsAPI.STONE_ORES, ore.getName()));
    }
    
    @ZenMethod
    public static void removeNetherOre(IOreDictEntry ore) {
        ModTweaker.LATE_REMOVALS.add(new Remove(ActuallyAdditionsAPI.NETHERRACK_ORES, ore.getName()));
    }
    
    private static class Add extends BaseListAddition<WeightedOre> {
        
        protected Add(List<WeightedOre> recipes, List<WeightedOre> newRecipes) {
            super("MiningLens", recipes, newRecipes);
        }
        
        @Override
        protected String getRecipeInfo(WeightedOre recipe) {
            return recipe.name;
        }
    }
    
    public static class Remove extends BaseListRemoval<WeightedOre> {
        
        private String output;
        
        protected Remove(List<WeightedOre> recipes, String output) {
            super("MiningLens", recipes);
            this.output = output;
        }
        
        @Override
        public void apply() {
            for(WeightedOre recipe : this.list) {
                if(recipe.name.equals(output)) {
                    recipes.add(recipe);
                }
            }
            super.apply();
        }
        
        @Override
        protected String getRecipeInfo(WeightedOre recipe) {
            return recipe.name;
        }
        
    }
    
}