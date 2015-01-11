package modtweaker.mods.exnihilo.handlers;

import static modtweaker.helpers.InputHelper.toStack;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import modtweaker.util.BaseMapAddition;
import modtweaker.util.BaseMapRemoval;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import exnihilo.registries.CompostRegistry;
import exnihilo.registries.helpers.Color;
import exnihilo.registries.helpers.Compostable;

@ZenClass("mods.exnihilo.Composting")
public class Compost {
    //Adding a Ex Nihilo Composting recipe
    @ZenMethod
    public static void addRecipe(IItemStack input, double value, @Optional String hex) {
        hex = (hex == null || hex.equals("")) ? "35A82A" : hex;
        MineTweakerAPI.apply(new Add(new Compostable(toStack(input).getItem(), toStack(input).getItemDamage(), Math.min(1.0F, (float) value), new Color(hex))));
    }

    //Passes the list to the map list implementation, and adds the recipe
    private static class Add extends BaseMapAddition {
        public Add(Compostable recipe) {
            super("ExNihilo Composting", CompostRegistry.entries, recipe.item + ":" + recipe.meta, recipe);
        }

        @Override
        public String getRecipeInfo() {
            return new ItemStack(((Compostable) recipe).item, 1, ((Compostable) recipe).meta).getDisplayName();
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //Removing a Ex Nihilo Composting recipe
    @ZenMethod
    public static void removeRecipe(IItemStack input) {
        MineTweakerAPI.apply(new Remove(toStack(input)));
    }

    //Removes a recipe, will always remove the key, so all should be good
    private static class Remove extends BaseMapRemoval {
        public Remove(ItemStack stack) {
            super("ExNihilo Composting", CompostRegistry.entries, stack.getItem() + ":" + stack.getItemDamage(), stack);
        }

        @Override
        public String getRecipeInfo() {
            return ((ItemStack) stack).getDisplayName();
        }
    }
}
