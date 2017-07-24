package com.blamejared.compat.tconstruct.handlers;

import com.blamejared.api.annotations.*;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import com.blamejared.mtlib.helpers.LogHelper;
import com.blamejared.compat.tconstruct.TConstructHelper;
import com.blamejared.mtlib.utils.BaseListAddition;
import com.blamejared.mtlib.utils.BaseListRemoval;
import slimeknights.mantle.util.RecipeMatch;
import slimeknights.tconstruct.library.DryingRecipe;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.plugin.jei.DryingRecipeCategory;
import stanhebben.zenscript.annotations.*;

import java.util.LinkedList;
import java.util.List;

import static com.blamejared.mtlib.helpers.InputHelper.toIItemStack;
import static com.blamejared.mtlib.helpers.InputHelper.toStack;

@ZenClass("mods.tconstruct.Drying")
@Handler("tconstruct")
public class Drying {

    protected static final String name = "TConstruct Drying Rack";


    /**********************************************
     * TConstruct Drying Recipes
     **********************************************/

    // Adding a TConstruct Drying recipe
    @ZenMethod
    @Document({"input", "output", "timeInTicks"})
    public static void addRecipe(IItemStack input, IItemStack output, int timeInTicks) {
        if (input == null || output == null || timeInTicks <= 0) {
            LogHelper.logError(String.format("Required parameters missing for %s Recipe.", name));
            return;
        }

        MineTweakerAPI.apply(new Add(TConstructHelper.getDryingRecipe(new RecipeMatch.Item(toStack(input), 1), toStack(output), timeInTicks)));
    }

    //Passes the list to the base list implementation, and adds the recipe
    private static class Add extends BaseListAddition<DryingRecipe> {

        public Add(DryingRecipe recipe) {
            super(Drying.name, TConstructHelper.dryingList);
            this.recipes.add(recipe);
        }

        @Override
        protected String getRecipeInfo(DryingRecipe recipe) {
            return LogHelper.getStackDescription(recipe.getResult());
        }

        @Override
        public String getJEICategory(DryingRecipe recipe) {
            return DryingRecipeCategory.CATEGORY;
        }
    }


    //Removing a TConstruct Drying Rack recipe
    @ZenMethod
    @Document({"output"})
    public static void removeRecipe(IIngredient output) {
        List<DryingRecipe> recipes = new LinkedList<>();

        for (DryingRecipe recipe : TinkerRegistry.getAllDryingRecipes()) {
            if (recipe != null && recipe.getResult() != null && output.matches(toIItemStack(recipe.getResult()))) {
                recipes.add(recipe);
            }
        }

        if (!recipes.isEmpty()) {
            MineTweakerAPI.apply(new Remove(recipes));
        } else {
            LogHelper.logWarning(String.format("No %s Recipe found for %s. Command ignored!", Drying.name, output.toString()));
        }
    }

    //Removes a recipe, apply is never the same for anything, so will always need to override it
    private static class Remove extends BaseListRemoval<DryingRecipe> {

        public Remove(List<DryingRecipe> list) {
            super(Drying.name, TConstructHelper.dryingList, list);
        }

        @Override
        protected String getRecipeInfo(DryingRecipe recipe) {
            return LogHelper.getStackDescription(recipe.getResult());
        }

        @Override
        public String getJEICategory(DryingRecipe recipe) {
            return DryingRecipeCategory.CATEGORY;
        }
    }
}