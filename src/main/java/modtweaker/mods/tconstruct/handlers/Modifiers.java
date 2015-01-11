package modtweaker.mods.tconstruct.handlers;

import java.util.List;

import minetweaker.MineTweakerAPI;
import modtweaker.mods.tconstruct.TConstructHelper;
import modtweaker.util.BaseListRemoval;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import tconstruct.library.modifier.ItemModifier;

@ZenClass("mods.tconstruct.Modifiers")
public class Modifiers {
    @ZenMethod
    public static void remove(String mod) {
        MineTweakerAPI.apply(new Remove(mod));
    }

    //Searches through the modifiers and removes the first valid entry
    private static class Remove extends BaseListRemoval {
        private final String check;

        public Remove(String check) {
            super("Modifier", TConstructHelper.modifiers);
            this.check = check;
        }

        @Override
        public void apply() {
            for (ItemModifier m : (List<ItemModifier>) list) {
                if (m.key.equals(check)) {
                    recipe = m;
                    break;
                }
            }

            list.remove(recipe);
        }
    }
}
