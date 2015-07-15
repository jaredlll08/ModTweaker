package modtweaker2.mods.tconstruct.handlers;

import java.util.LinkedList;
import java.util.List;

import minetweaker.MineTweakerAPI;
import modtweaker2.helpers.LogHelper;
import modtweaker2.mods.tconstruct.TConstructHelper;
import modtweaker2.utils.BaseListRemoval;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import tconstruct.library.modifier.ItemModifier;

@ZenClass("mods.tconstruct.Modifiers")
public class Modifiers {
    
    protected static final String name = "TConstruct Modifier";
    
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    @ZenMethod
    public static void remove(String mod) {
        List<ItemModifier> recipes = new LinkedList<ItemModifier>();
        
        for (ItemModifier recipe : TConstructHelper.modifiers) {
            if (recipe.key.equals(mod)) {
                recipes.add(recipe);
            }
        }
        
        if(!recipes.isEmpty()) {
            MineTweakerAPI.apply(new Remove(recipes));
        } else {
            LogHelper.logWarning(String.format("No %s Recipe found for %s. Command ignored!", Modifiers.name, mod));
        }
    }

    private static class Remove extends BaseListRemoval<ItemModifier>{
        public Remove(List<ItemModifier> recipes) {
            super(Modifiers.name, TConstructHelper.modifiers, recipes);
        }

        @Override
        protected String getRecipeInfo(ItemModifier recipe) {
            return recipe.key;
        }
    }
}
