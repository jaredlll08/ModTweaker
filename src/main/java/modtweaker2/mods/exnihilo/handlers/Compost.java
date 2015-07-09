package modtweaker2.mods.exnihilo.handlers;

import static modtweaker2.helpers.InputHelper.toIItemStack;
import static modtweaker2.helpers.InputHelper.toStack;
import static modtweaker2.helpers.StackHelper.matches;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import modtweaker2.helpers.LogHelper;
import modtweaker2.utils.BaseMapAddition;
import modtweaker2.utils.BaseMapRemoval;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import exnihilo.registries.CompostRegistry;
import exnihilo.registries.helpers.Color;
import exnihilo.registries.helpers.Compostable;
import exnihilo.utils.ItemInfo;


@ZenClass("mods.exnihilo.Composting")
public class Compost {
    
    public static final String name = "ExNihilo Composting";

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @ZenMethod
    public static void addRecipe(IItemStack input, double value, @Optional String hex) {
        if(input == null) {
            LogHelper.logError(String.format("Required parameters missing for %s Recipe.", name));
            return;
        }
        
        hex = (hex == null || hex.equals("")) ? "35A82A" : hex;
        
        Map<ItemInfo, Compostable> recipes = new HashMap<ItemInfo, Compostable>();
        
        recipes.put(
                new ItemInfo(toStack(input)),
                new Compostable(Math.min(1.0F, (float)value), new Color(hex)));
        
        MineTweakerAPI.apply(new Add(recipes));
    }
    
    private static class Add extends BaseMapAddition<ItemInfo, Compostable> {
        public Add(Map<ItemInfo, Compostable> recipes) {
            super(Compost.name, CompostRegistry.entries, recipes);
        }
        
        @Override
        protected String getRecipeInfo(Entry<ItemInfo, Compostable> recipe) {
            return LogHelper.getStackDescription(recipe.getKey().getStack());
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @ZenMethod
    public static void removeRecipe(IIngredient input) {
        Map<ItemInfo, Compostable> recipes = new HashMap<ItemInfo, Compostable>();
        
        for(Entry<ItemInfo, Compostable> recipe : CompostRegistry.entries.entrySet()) {
            if(matches(input, toIItemStack(recipe.getKey().getStack()))) {
                recipes.put(recipe.getKey(), recipe.getValue());
            }
        }
        
        if(!recipes.isEmpty()) {
            MineTweakerAPI.apply(new Remove(recipes));
        } else {
            LogHelper.logWarning(String.format("No %s recipes found for %s. Command ignored!", Compost.name, input.toString()));
        }
        
    }
    
    private static class Remove extends BaseMapRemoval<ItemInfo, Compostable> {
        public Remove(Map<ItemInfo, Compostable> recipes) {
            super(Compost.name, CompostRegistry.entries, recipes);
        }
        
        @Override
        protected String getRecipeInfo(Entry<ItemInfo, Compostable> recipe) {
            return LogHelper.getStackDescription(recipe.getKey().getStack());
        }
    }
}
