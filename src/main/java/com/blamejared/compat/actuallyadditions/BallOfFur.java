package com.blamejared.compat.actuallyadditions;

import com.blamejared.api.annotations.*;
import com.blamejared.mtlib.helpers.*;
import com.blamejared.mtlib.utils.*;
import de.ellpeck.actuallyadditions.api.ActuallyAdditionsAPI;
import de.ellpeck.actuallyadditions.api.recipe.BallOfFurReturn;
import de.ellpeck.actuallyadditions.mod.jei.JEIActuallyAdditionsPlugin;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import stanhebben.zenscript.annotations.*;

import java.util.*;

@ZenClass("mods.actuallyadditions.BallOfFur")
@Handler("actuallyadditions")
public class BallOfFur {
    
    @ZenMethod
    @Document({"output", "chance"})
    public static void addReturn(IItemStack output, int chance) {
        MineTweakerAPI.apply(new Add(Collections.singletonList(new BallOfFurReturn(InputHelper.toStack(output), chance))));
    }
    
    @ZenMethod
    @Document({"output"})
    public static void removeReturn(IItemStack output) {
        List<BallOfFurReturn> recipes = new ArrayList<>();
        for(BallOfFurReturn recipe : ActuallyAdditionsAPI.BALL_OF_FUR_RETURN_ITEMS) {
            if(output.matches(InputHelper.toIItemStack(recipe.returnItem))) {
                recipes.add(recipe);
            }
        }
        MineTweakerAPI.apply(new Remove(recipes));
    }
    
    private static class Add extends BaseListAddition<BallOfFurReturn> {
        
        protected Add(List<BallOfFurReturn> recipes) {
            super("BallOfFur", ActuallyAdditionsAPI.BALL_OF_FUR_RETURN_ITEMS, recipes);
        }
        
        @Override
        protected String getRecipeInfo(BallOfFurReturn recipe) {
            return LogHelper.getStackDescription(recipe.returnItem);
        }
    }
    
    private static class Remove extends BaseListRemoval<BallOfFurReturn> {
        
        protected Remove(List<BallOfFurReturn> recipes) {
            super("BallOfFur", ActuallyAdditionsAPI.BALL_OF_FUR_RETURN_ITEMS, recipes);
        }
        
        @Override
        protected String getRecipeInfo(BallOfFurReturn recipe) {
            return LogHelper.getStackDescription(recipe.returnItem);
        }
    }
    
}
