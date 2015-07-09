package modtweaker2.mods.metallurgy.handlers;

import static modtweaker2.helpers.InputHelper.toIItemStack;
import static modtweaker2.helpers.InputHelper.toStack;
import static modtweaker2.helpers.StackHelper.matches;

import java.util.LinkedList;
import java.util.List;

import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import modtweaker2.helpers.LogHelper;
import modtweaker2.mods.metallurgy.MetallurgyHelper;
import modtweaker2.utils.BaseListAddition;
import modtweaker2.utils.BaseListRemoval;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import com.teammetallurgy.metallurgy.recipes.AlloyerRecipes.AlloyRecipe;

@ZenClass("mods.metallurgy.Alloyer")
public class Alloyer {
    
    public static final String name = "Metallurgy Alloyer";
    
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    @ZenMethod
    public static void addRecipe(IItemStack first, IItemStack base, IItemStack result) {
        MineTweakerAPI.apply(new Add(MetallurgyHelper.getAlloyRecipe(toStack(first), toStack(base), toStack(result))));
    }

    private static class Add extends BaseListAddition<AlloyRecipe> {
        public Add(AlloyRecipe recipe) {
            super(Alloyer.name, MetallurgyHelper.alloyerRecipes);
            recipes.add(recipe);
        }

        @Override
        public String getRecipeInfo(AlloyRecipe recipe) {
            return LogHelper.getStackDescription(recipe.getCraftingResult());
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @ZenMethod
    public static void removeRecipe(IIngredient output) {
        List<AlloyRecipe> recipes = new LinkedList<AlloyRecipe>();
        
        for (AlloyRecipe r : MetallurgyHelper.alloyerRecipes) {
            if (r != null && r.getCraftingResult() != null && matches(output, toIItemStack(r.getCraftingResult()))) {
                recipes.add(r);
            }
        }
        MineTweakerAPI.apply(new Remove(recipes));
    }

    private static class Remove extends BaseListRemoval<AlloyRecipe> {
        public Remove(List<AlloyRecipe> recipes) {
            super(Alloyer.name, MetallurgyHelper.alloyerRecipes, recipes);
        }

        @Override
        public String getRecipeInfo(AlloyRecipe recipe) {
            return LogHelper.getStackDescription(recipe.getCraftingResult());
        }
    }
}
