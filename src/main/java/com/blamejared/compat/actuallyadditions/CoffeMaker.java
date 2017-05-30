package com.blamejared.compat.actuallyadditions;

import com.blamejared.api.annotations.*;
import com.blamejared.api.potions.IPotion;
import com.blamejared.mtlib.helpers.*;
import com.blamejared.mtlib.utils.*;
import de.ellpeck.actuallyadditions.api.ActuallyAdditionsAPI;
import de.ellpeck.actuallyadditions.api.recipe.CoffeeIngredient;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import net.minecraft.potion.*;
import stanhebben.zenscript.annotations.*;

import java.util.*;

@ZenClass("mods.actuallyadditions.Coffee")
@Handler("actuallyadditions")
public class CoffeMaker {
    
    @ZenMethod
    @Document({"input", "potionEffects", "effectDuration", "maxAmplifier"})
    public static void addRecipe(IItemStack input, IPotion[] potionEffects, int[] effectDuration, int maxAmplifier) {
        if(potionEffects.length != effectDuration.length) {
            MineTweakerAPI.logError("potionEffects and effectDuration must have the same size");
            return;
        }
        PotionEffect[] potionArr = new PotionEffect[potionEffects.length];
        for(int i = 0; i < potionEffects.length; i++) {
            IPotion potion = potionEffects[i];
            int duration = effectDuration[i];
            potionArr[i] = toPotionEffect(potion, duration);
        }
        MineTweakerAPI.apply(new Add(Collections.singletonList(new CoffeeIngredient(InputHelper.toStack(input), potionArr, maxAmplifier))));
    }
    
    @ZenMethod
    public static void removeRecipe(IItemStack input) {
        List<CoffeeIngredient> recipes = new ArrayList<>();
        for(CoffeeIngredient ingredient : ActuallyAdditionsAPI.COFFEE_MACHINE_INGREDIENTS) {
            if(input.matches(InputHelper.toIItemStack(ingredient.ingredient))) {
                recipes.add(ingredient);
            }
        }
        MineTweakerAPI.apply(new Remove(recipes));
    }
    
    private static class Add extends BaseListAddition<CoffeeIngredient> {
        
        protected Add(List<CoffeeIngredient> recipies) {
            super("Coffee Maker", ActuallyAdditionsAPI.COFFEE_MACHINE_INGREDIENTS, recipies);
        }
        
        @Override
        protected String getRecipeInfo(CoffeeIngredient recipe) {
            return LogHelper.getStackDescription(recipe.ingredient);
        }
        
        @Override
        public String getJEICategory(CoffeeIngredient recipe) {
            return "actuallyadditions.coffee";
        }
    }
    
    private static class Remove extends BaseListRemoval<CoffeeIngredient> {
        
        protected Remove(List<CoffeeIngredient> recipies) {
            super("Coffee Maker", ActuallyAdditionsAPI.COFFEE_MACHINE_INGREDIENTS, recipies);
        }
        
        @Override
        protected String getRecipeInfo(CoffeeIngredient recipe) {
            return LogHelper.getStackDescription(recipe.ingredient);
        }
        
        @Override
        public String getJEICategory(CoffeeIngredient recipe) {
            return "actuallyadditions.coffee";
        }
    }
    
    private static PotionEffect toPotionEffect(IPotion potion, int duration) {
        return new PotionEffect((Potion) potion.getInternal(), duration);
    }
}
