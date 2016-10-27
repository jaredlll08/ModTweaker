package modtweaker.mods.randomthings.handlers;

import lumien.randomthings.recipes.imbuing.ImbuingRecipe;
import lumien.randomthings.recipes.imbuing.ImbuingRecipeHandler;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import com.blamejared.mtlib.helpers.InputHelper;
import com.blamejared.mtlib.helpers.LogHelper;
import com.blamejared.mtlib.utils.BaseListAddition;
import com.blamejared.mtlib.utils.BaseListRemoval;
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
    public static void remove(IItemStack output) {
        MineTweakerAPI.apply(new Remove(new ImbuingRecipe(null, InputHelper.toStack(output), null, null, null)));
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

        @Override
        public void undo() {

            if (this.successful.isEmpty()) {
                return;
            }

            for (ImbuingRecipe recipe : this.successful) {
                if (recipe != null) {
                    ImbuingRecipe toRemove = null;
                    for (ImbuingRecipe r : ImbuingRecipeHandler.imbuingRecipes) {
                        if (r.getResult().isItemEqual(recipe.getResult())) {
                            toRemove = r;
                        }
                    }
                    if (toRemove != null)
                        if (ImbuingRecipeHandler.imbuingRecipes.remove(toRemove)) {
                        } else {
                            LogHelper.logError(String.format("Error removing %s Recipe for %s", name, getRecipeInfo(recipe)));
                        }
                } else {
                    LogHelper.logError(String.format("Error removing %s Recipe: null object", name));
                }
            }

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
                    ImbuingRecipe toRemove = null;
                    for (ImbuingRecipe r : ImbuingRecipeHandler.imbuingRecipes) {
                        if (r.getResult().isItemEqual(recipe.getResult())) {
                            toRemove = r;
                        }
                    }
                    if (toRemove != null)
                        if (ImbuingRecipeHandler.imbuingRecipes.remove(toRemove)) {
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
            for (ItemStack stack : recipe.getIngredients()) {
                build.append(LogHelper.getStackDescription(stack) + " ");
            }
            return build.toString();
        }
    }
}
