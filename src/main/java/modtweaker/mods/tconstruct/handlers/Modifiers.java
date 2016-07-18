package modtweaker.mods.tconstruct.handlers;

import gnu.trove.map.hash.THashMap;
import minetweaker.MineTweakerAPI;
import modtweaker.helpers.LogHelper;
import modtweaker.mods.tconstruct.TConstructHelper;
import modtweaker.utils.BaseMapRemoval;
import slimeknights.tconstruct.library.modifiers.IModifier;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.Map;

@ZenClass("mods.tconstruct.Modifiers")
public class Modifiers {

    protected static final String name = "TConstruct Modifier";

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @ZenMethod
    public static void remove(String mod) {
        Map<String, IModifier> recipes = new THashMap<String, IModifier>();
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
