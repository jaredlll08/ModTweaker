package modtweaker.mods.randomthings.handlers;

import lumien.randomthings.recipes.imbuing.ImbuingRecipe;
import lumien.randomthings.recipes.imbuing.ImbuingRecipeHandler;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import modtweaker.helpers.InputHelper;
import modtweaker.helpers.LogHelper;
import modtweaker.utils.BaseListAddition;
import modtweaker.utils.BaseListRemoval;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

/**
 * Created by Jared on 7/16/2016.
 */
@ZenClass("mods.randomthings.ImbuingStation")
public class ImbuingStation {

    @ZenMethod
    public static void add(IItemStack output, IItemStack input, IItemStack ingredient1, IItemStack ingredient2, IItemStack ingredient3) {
        MineTweakerAPI.apply(new Add(new ImbuingRecipe(InputHelper.toStack(input), InputHelper.toStack(output), InputHelper.toStack(ingredient1), InputHelper.toStack(ingredient2), InputHelper.toStack(ingredient3))));
    }

    @ZenMethod
    public static void remove(IItemStack output, IItemStack input, IItemStack ingredient1, IItemStack ingredient2, IItemStack ingredient3) {
        MineTweakerAPI.apply(new Remove(new ImbuingRecipe(InputHelper.toStack(input), InputHelper.toStack(output), InputHelper.toStack(ingredient1), InputHelper.toStack(ingredient2), InputHelper.toStack(ingredient3))));
    }

    private static class Add extends BaseListAddition<ImbuingRecipe> {

        protected Add(ImbuingRecipe recipe) {
            super("ImbuingStation", ImbuingRecipeHandler.imbuingRecipes);
            this.recipes.add(recipe);
        }

        @Override
        protected String getRecipeInfo(ImbuingRecipe recipe) {
            StringBuilder build = new StringBuilder();
            build.append(LogHelper.getStackDescription(recipe.getResult()));
            build.append(LogHelper.getStackDescription(recipe.toImbue()));
            for (ItemStack stack : recipe.getIngredients()) {
                build.append(LogHelper.getStackDescription(stack));
            }
            return build.toString();
        }
    }

    private static class Remove extends BaseListRemoval<ImbuingRecipe> {

        protected Remove(ImbuingRecipe recipe) {
            super("ImbuingStation", ImbuingRecipeHandler.imbuingRecipes);
            this.recipes.add(recipe);
        }

        @Override
        public void apply() {
            if (recipes.isEmpty()) {
                return;
            }

            for (ImbuingRecipe recipe : this.recipes) {
                if (recipe != null) {
                    for (ImbuingRecipe r : ImbuingRecipeHandler.imbuingRecipes) {
                        System.out.println(recipe.getIngredients() + " : " +r.getIngredients());
                        System.out.println(recipe.toImbue() + " : " +r.toImbue());
                        System.out.println(recipe.getResult() + " : " +r.getResult());

                    }
                    if (ImbuingRecipeHandler.imbuingRecipes.remove(recipe)) {
                        successful.add(recipe);
                    } else {
                        LogHelper.logError(String.format("Error removing %s Recipe for %s", name, getRecipeInfo(recipe)));
                    }
                } else {
                    LogHelper.logError(String.format("Error removing %s Recipe: null object", name));
                }
            }
        }

        @Override
        protected String getRecipeInfo(ImbuingRecipe recipe) {
            StringBuilder build = new StringBuilder();
            build.append(LogHelper.getStackDescription(recipe.getResult()) + ", ");
            build.append(LogHelper.getStackDescription(recipe.toImbue()) + ", ");
            for (ItemStack stack : recipe.getIngredients()) {
                build.append(LogHelper.getStackDescription(stack) + " ");
            }
            return build.toString();
        }
    }
}
