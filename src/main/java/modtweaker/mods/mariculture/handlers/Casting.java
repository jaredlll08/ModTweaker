package modtweaker.mods.mariculture.handlers;

import static modtweaker.helpers.InputHelper.toFluid;
import static modtweaker.helpers.InputHelper.toStack;

import java.util.HashMap;

import mariculture.api.core.MaricultureHandlers;
import mariculture.api.core.RecipeCasting;
import mariculture.api.core.RecipeCasting.RecipeNuggetCasting;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import minetweaker.api.liquid.ILiquidStack;
import modtweaker.mods.mariculture.MaricultureHelper;
import modtweaker.util.BaseMapAddition;
import modtweaker.util.BaseMapRemoval;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.mariculture.Casting")
public class Casting {
    //Adding Mariculture Casting Recipes
    @ZenMethod
    public static void addNuggetRecipe(ILiquidStack input, IItemStack output) {
        MineTweakerAPI.apply(new Add(new RecipeNuggetCasting(toFluid(input), toStack(output)), MaricultureHandlers.casting.getNuggetRecipes()));
    }

    @ZenMethod
    public static void addIngotRecipe(ILiquidStack input, IItemStack output) {
        MineTweakerAPI.apply(new Add(new RecipeNuggetCasting(toFluid(input), toStack(output)), MaricultureHandlers.casting.getIngotRecipes()));
    }

    @ZenMethod
    public static void addBlockRecipe(ILiquidStack input, IItemStack output) {
        MineTweakerAPI.apply(new Add(new RecipeNuggetCasting(toFluid(input), toStack(output)), MaricultureHandlers.casting.getBlockRecipes()));
    }

    private static class Add extends BaseMapAddition {
        public Add(RecipeCasting recipe, HashMap map) {
            super("Mariculture Casting", map, MaricultureHelper.getKey(recipe.fluid), recipe);
        }

        @Override
        public String getRecipeInfo() {
            return ((RecipeCasting) recipe).output.getDisplayName();
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //Removing Mariculture Casting Recipes
    @ZenMethod
    public static void removeNuggetRecipe(IItemStack input) {
        MineTweakerAPI.apply(new Remove(toStack(input), MaricultureHandlers.casting.getNuggetRecipes()));
    }

    @ZenMethod
    public static void removeIngotRecipe(IItemStack input) {
        MineTweakerAPI.apply(new Remove(toStack(input), MaricultureHandlers.casting.getIngotRecipes()));
    }

    @ZenMethod
    public static void removeBlockRecipe(IItemStack input) {
        MineTweakerAPI.apply(new Remove(toStack(input), MaricultureHandlers.casting.getBlockRecipes()));
    }

    private static class Remove extends BaseMapRemoval {
        public Remove(ItemStack stack, HashMap map) {
            super("Mariculture Casting", map, MaricultureHelper.getKey(stack), stack);
        }

        @Override
        public String getRecipeInfo() {
            return ((ItemStack) stack).getDisplayName();
        }
    }
}
