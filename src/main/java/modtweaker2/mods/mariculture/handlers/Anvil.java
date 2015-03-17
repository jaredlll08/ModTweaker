package modtweaker2.mods.mariculture.handlers;

import static modtweaker2.helpers.InputHelper.toStack;
import mariculture.api.core.IAnvilHandler.RecipeAnvil;
import mariculture.api.core.MaricultureHandlers;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import modtweaker2.mods.mariculture.MaricultureHelper;
import modtweaker2.utils.BaseMapAddition;
import modtweaker2.utils.BaseMapRemoval;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.mariculture.Anvil")
public class Anvil {
    //Adding a Mariculture Anvil recipe
    @ZenMethod
    public static void addRecipe(IItemStack input, IItemStack output, int hits) {
        MineTweakerAPI.apply(new Add(new RecipeAnvil(toStack(input), toStack(output), hits)));
    }

    private static class Add extends BaseMapAddition {
        public Add(RecipeAnvil recipe) {
            super("Mariculture Anvil", MaricultureHandlers.anvil.getRecipes(), MaricultureHelper.getKey(recipe.input), recipe);
        }

        @Override
        public String getRecipeInfo() {
            return ((RecipeAnvil) recipe).output.getDisplayName();
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //Removing a Mariculture Anvil recipe
    @ZenMethod
    public static void removeRecipe(IItemStack input) {
        MineTweakerAPI.apply(new Remove(toStack(input)));
    }

    private static class Remove extends BaseMapRemoval {
        public Remove(ItemStack stack) {
            super("Mariculture Anvil", MaricultureHandlers.anvil.getRecipes(), MaricultureHelper.getKey(stack), stack);
        }

        @Override
        public String getRecipeInfo() {
            return ((ItemStack) stack).getDisplayName();
        }
    }
}
