package com.blamejared.compat.tconstruct.handlers;

import com.blamejared.api.annotations.*;
import gnu.trove.map.hash.THashMap;
import minetweaker.MineTweakerAPI;
import com.blamejared.mtlib.helpers.LogHelper;
import com.blamejared.compat.tconstruct.TConstructHelper;
import com.blamejared.mtlib.utils.BaseMapRemoval;
import slimeknights.tconstruct.library.modifiers.IModifier;
import stanhebben.zenscript.annotations.*;

import java.util.Map;

@ZenClass("mods.tconstruct.Modifiers")
@Handler("tconstruct")
public class Modifiers {

    protected static final String name = "TConstruct Modifier";


    /**********************************************
     * TConstruct Modifier Recipes
     **********************************************/

    @ZenMethod
    @Document({"mod"})
    public static void remove(String mod) {
        Map<String, IModifier> recipes = new THashMap<>();
        for (Map.Entry<String, IModifier> ent : TConstructHelper.modifiers.entrySet()) {
            if (ent.getKey().equals(mod)) {
                recipes.put(ent.getKey(), ent.getValue());
            }
        }
        if (!recipes.isEmpty()) {
            MineTweakerAPI.apply(new Remove(recipes));
        } else {
            LogHelper.logWarning(String.format("No %s Recipe found for %s. Command ignored!", Modifiers.name, mod));
        }
    }

    private static class Remove extends BaseMapRemoval<String, IModifier> {
        public Remove(Map<String, IModifier> recipes) {
            super(Modifiers.name, TConstructHelper.modifiers, recipes);
        }

        @Override
        protected String getRecipeInfo(Map.Entry<String, IModifier> recipe) {
            return recipe.getKey();
        }
    }
}