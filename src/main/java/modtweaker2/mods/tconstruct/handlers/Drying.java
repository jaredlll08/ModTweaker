package modtweaker2.mods.tconstruct.handlers;

import static modtweaker2.helpers.InputHelper.toIItemStack;
import static modtweaker2.helpers.InputHelper.toStack;

import java.util.LinkedList;
import java.util.List;

import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import modtweaker2.helpers.InputHelper;
import modtweaker2.helpers.LogHelper;
import modtweaker2.mods.tconstruct.TConstructHelper;
import modtweaker2.utils.BaseListAddition;
import modtweaker2.utils.BaseListRemoval;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import tconstruct.library.crafting.DryingRackRecipes;
import tconstruct.library.crafting.DryingRackRecipes.DryingRecipe;

@ZenClass("mods.tconstruct.Drying")
public class Drying {
    
    protected static final String name = "TConstruct Drying Rack";
    
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    @ZenMethod
    public static void addRecipe(IItemStack input, IItemStack output, int time) {
        MineTweakerAPI.apply(new Add(TConstructHelper.getDryingRecipe(toStack(input), time, toStack(output))));
    }

    //Passes the list to the base list implementation, and adds the recipe
    private static class Add extends BaseListAddition<DryingRecipe> {
        public Add(DryingRecipe recipe) {
            super(Drying.name, DryingRackRecipes.recipes);
            this.recipes.add(recipe);
        }

        @Override
        protected String getRecipeInfo(DryingRecipe recipe) {
            return InputHelper.getStackDescription(recipe.result);
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //Removing a TConstruct Drying Rack recipe
    @ZenMethod
    public static void removeRecipe(IIngredient ingredient) {
        List<DryingRecipe> recipes = new LinkedList<DryingRecipe>();
        
        for (DryingRecipe recipe : DryingRackRecipes.recipes) {
            if (recipe != null && recipe.result != null && ingredient.matches(toIItemStack(recipe.result))) {
                recipes.add(recipe);
            }
        }

        if(!recipes.isEmpty()) {
            MineTweakerAPI.apply(new Remove(recipes));
        } else {
            LogHelper.logWarning(String.format("No %s Recipe found for %s. Command ignored!", Drying.name, ingredient.toString()));
        }
    }

    //Removes a recipe, apply is never the same for anything, so will always need to override it
    private static class Remove extends BaseListRemoval<DryingRecipe> {
        public Remove(List<DryingRecipe> list) {
            super(Drying.name, DryingRackRecipes.recipes, list);
        }

        @Override
        protected String getRecipeInfo(DryingRecipe recipe) {
            return InputHelper.getStackDescription(recipe.result);
        }
    }
}
