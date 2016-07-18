package modtweaker.mods.tconstruct.handlers;

import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import minetweaker.api.item.IngredientAny;
import minetweaker.api.liquid.ILiquidStack;
import modtweaker.helpers.LogHelper;
import modtweaker.mods.tconstruct.TConstructHelper;
import modtweaker.utils.BaseListAddition;
import modtweaker.utils.BaseListRemoval;
import slimeknights.mantle.util.RecipeMatch;
import slimeknights.tconstruct.library.smeltery.CastingRecipe;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.LinkedList;
import java.util.List;

import static modtweaker.helpers.InputHelper.*;
import static modtweaker.helpers.StackHelper.matches;

@ZenClass("mods.tconstruct.Casting")
public class Casting {

    protected static final String name = "TConstruct Casting";

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @ZenMethod
    public static void addBasinRecipe(IItemStack output, ILiquidStack metal, @Optional IItemStack cast) {
        if (metal == null || output == null) {
            LogHelper.logError(String.format("Required parameters missing for %s Recipe.", name));
            return;
        }
        CastingRecipe rec = new CastingRecipe(toStack(output), RecipeMatch.of(toStack(cast)), toFluid(metal).getFluid(), metal.getAmount());
        MineTweakerAPI.apply(new Add(rec, (LinkedList<CastingRecipe>) TConstructHelper.basinCasting));
    }

    @ZenMethod
    public static void addTableRecipe(IItemStack output, ILiquidStack metal, @Optional IItemStack cast) {
        if (metal == null || output == null) {
            LogHelper.logError(String.format("Required parameters missing for %s Recipe.", name));
            return;
        }
        RecipeMatch match = null;
        if (cast != null) {
            match = RecipeMatch.of(toStack(cast));
        }
        CastingRecipe rec = new CastingRecipe(toStack(output), match, toFluid(metal).getFluid(), metal.getAmount());
        MineTweakerAPI.apply(new Add(rec, (LinkedList<CastingRecipe>) TConstructHelper.tableCasting));
    }

    //Passes the list to the base list implementation, and adds the recipe
    private static class Add extends BaseListAddition<CastingRecipe> {
        public Add(CastingRecipe recipe, LinkedList<CastingRecipe> list) {
            super(Casting.name, list);
            this.recipes.add(recipe);
        }

        @Override
        protected String getRecipeInfo(CastingRecipe recipe) {
            return LogHelper.getStackDescription(recipe.getResult());
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @ZenMethod
    public static void removeTableRecipe(IIngredient output, @Optional IIngredient material, @Optional IIngredient cast) {
        removeRecipe(output, material, cast, TConstructHelper.tableCasting);
    }

    @ZenMethod
    public static void removeBasinRecipe(IIngredient output, @Optional IIngredient material, @Optional IIngredient cast) {

        removeRecipe(output, material, cast, TConstructHelper.basinCasting);
    }

    public static void removeRecipe(IIngredient output, IIngredient material, IIngredient cast, List<CastingRecipe> list) {
        if (output == null) {
            LogHelper.logError(String.format("Required parameters missing for %s Recipe.", name));
            return;
        }

        if (material == null) {
            material = IngredientAny.INSTANCE;
        }

        List<CastingRecipe> recipes = new LinkedList<CastingRecipe>();

        for (CastingRecipe recipe : list) {
            if (recipe != null) {
                if (!matches(output, toIItemStack(recipe.getResult()))) {

                    continue;
                }

                if (!matches(material, toILiquidStack(recipe.getFluid()))) {

                    continue;
                }
                if ((recipe.cast != null && cast != null) && (recipe.cast.matches(toStacks(cast.getItems().toArray(new IItemStack[0]))) == null)) {
                    continue;
                }

                recipes.add(recipe);
            }
        }

        if (!recipes.isEmpty())

        {
            MineTweakerAPI.apply(new Remove(list, recipes));
        } else

        {
            LogHelper.logWarning(String.format("No %s Recipe found for output %s, material %s and cast %s. Command ignored!", Casting.name, output.toString(), material.toString(), cast != null ? cast.toString() : null));
        }
    }

    // Removes all matching recipes, apply is never the same for anything, so will always need to override it
    private static class Remove extends BaseListRemoval<CastingRecipe> {
        public Remove(List<CastingRecipe> list, List<CastingRecipe> recipes) {
            super(Casting.name, list, recipes);
        }

        @Override
        protected String getRecipeInfo(CastingRecipe recipe) {
            return LogHelper.getStackDescription(recipe.getResult());
        }
    }
}
