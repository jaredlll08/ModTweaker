package com.blamejared.compat.actuallyadditions;

import com.blamejared.api.annotations.*;
import com.blamejared.mtlib.helpers.*;
import com.blamejared.mtlib.utils.*;
import de.ellpeck.actuallyadditions.api.ActuallyAdditionsAPI;
import de.ellpeck.actuallyadditions.api.recipe.TreasureChestLoot;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import stanhebben.zenscript.annotations.*;

import java.util.*;

@ZenClass("mods.actuallyadditions.TreasureChest")
@Handler("actuallyadditions")
public class TreasureChest {
    
    @ZenMethod
    @Document({"returnItem", "chance", "minAmount", "maxAmount"})
    public static void addLoot(IItemStack returnItem, int chance, int minAmount, int maxAmount) {
        MineTweakerAPI.apply(new Add(Collections.singletonList(new TreasureChestLoot(InputHelper.toStack(returnItem), chance, minAmount, maxAmount))));
    }
    
    @ZenMethod
    @Document({"returnItem"})
    public static void removeLoot(IItemStack returnItem) {
        List<TreasureChestLoot> recipes = new ArrayList<>();
        
        ActuallyAdditionsAPI.TREASURE_CHEST_LOOT.forEach(recipe -> {
            if(returnItem.matches(InputHelper.toIItemStack(recipe.returnItem))) {
                recipes.add(recipe);
            }
        });
        MineTweakerAPI.apply(new Remove(recipes));
    }
    
    private static class Add extends BaseListAddition<TreasureChestLoot> {
        
        protected Add(List<TreasureChestLoot> recipes) {
            super("Treasure Chest", recipes);
        }
        
        @Override
        protected String getRecipeInfo(TreasureChestLoot recipe) {
            return LogHelper.getStackDescription(recipe.returnItem);
        }
    }
    
    private static class Remove extends BaseListRemoval<TreasureChestLoot> {
        
        protected Remove(List<TreasureChestLoot> recipes) {
            super("Treasure Chest", recipes);
        }
        
        @Override
        protected String getRecipeInfo(TreasureChestLoot recipe) {
            return LogHelper.getStackDescription(recipe.returnItem);
        }
    }
}
