package modtweaker.mods.mekanism.handlers;

import static modtweaker.helpers.InputHelper.toStack;
import static modtweaker.helpers.StackHelper.areEqual;

import java.util.Iterator;
import java.util.Map;

import mekanism.api.infuse.InfuseRegistry;
import mekanism.api.infuse.InfusionInput;
import mekanism.api.infuse.InfusionOutput;
import mekanism.common.recipe.RecipeHandler.Recipe;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import modtweaker.mods.mekanism.util.AddMekanismRecipe;
import modtweaker.mods.mekanism.util.RemoveMekanismRecipe;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.mekanism.Infuser")
public class Infuser {
    @ZenMethod
    public static void addRecipe(String type, int infuse, IItemStack input, IItemStack output) {
        InfusionInput infuseIn = new InfusionInput(InfuseRegistry.get(type), infuse, toStack(input));
        InfusionOutput infuseOut = new InfusionOutput(infuseIn, toStack(output));
        MineTweakerAPI.apply(new AddMekanismRecipe("METALLURGIC_INFUSER", Recipe.METALLURGIC_INFUSER.get(), infuseIn, infuseOut));
    }

    @ZenMethod
    public static void removeRecipe(IItemStack output) {
        MineTweakerAPI.apply(new Remove("METALLURGIC_INFUSER", Recipe.METALLURGIC_INFUSER.get(), new InfusionOutput(null, toStack(output))));
    }

    private static class Remove extends RemoveMekanismRecipe {
        public Remove(String string, Map map, Object key) {
            super(string, map, key);
        }

        @Override
        public void apply() {
            Iterator it = map.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pairs = (Map.Entry) it.next();
                InfusionInput key = (InfusionInput) pairs.getKey();
                InfusionOutput value = (InfusionOutput) pairs.getValue();
                if (key != null) {
                    if (this.key instanceof InfusionOutput && areEqual(value.resource, ((InfusionOutput) this.key).resource)) {
                        this.key = key;
                        break;
                    }
                }

            }

            recipe = map.get(key);
            map.remove(key);
        }
    }
}
