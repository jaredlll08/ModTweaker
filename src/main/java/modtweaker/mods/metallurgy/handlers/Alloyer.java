package modtweaker.mods.metallurgy.handlers;

import static modtweaker.helpers.InputHelper.toStack;
import static modtweaker.helpers.StackHelper.areEqual;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import modtweaker.mods.metallurgy.MetallurgyHelper;
import modtweaker.util.BaseListAddition;
import modtweaker.util.BaseListRemoval;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import com.teammetallurgy.metallurgy.recipes.AlloyerRecipes.AlloyRecipe;

@ZenClass("mods.metallurgy.Alloyer")
public class Alloyer {
    //Adding a Metallurgy Alloyer recipe
    @ZenMethod
    public static void addRecipe(IItemStack first, IItemStack base, IItemStack result) {
        MineTweakerAPI.apply(new Add(MetallurgyHelper.getAlloyRecipe(toStack(first), toStack(base), toStack(result))));
    }

    //Passes the list to the base list implementation, and adds the recipe
    private static class Add extends BaseListAddition {
        public Add(AlloyRecipe recipe) {
            super("Metallurgy Alloyer", MetallurgyHelper.alloyerRecipes, recipe);
        }

        @Override
        public String getRecipeInfo() {
            return ((AlloyRecipe) recipe).getCraftingResult().getDisplayName();
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //Removing a Metallurgy Alloyer recipe
    @ZenMethod
    public static void removeRecipe(IItemStack output) {
        MineTweakerAPI.apply(new Remove(toStack(output)));
    }

    //Removes a recipe, apply is never the same for anything, so will always need to override it
    private static class Remove extends BaseListRemoval {
        public Remove(ItemStack stack) {
            super("Metallurgy Alloyer", MetallurgyHelper.alloyerRecipes, stack);
        }

        //Loops through the registry, to find the item that matches, saves that recipe then removes it
        @Override
        public void apply() {
            for (AlloyRecipe r : MetallurgyHelper.alloyerRecipes) {
                if (r.getCraftingResult() != null && areEqual(r.getCraftingResult(), stack)) {
                    recipe = r;
                    break;
                }
            }

            MetallurgyHelper.alloyerRecipes.remove(recipe);
        }

        @Override
        public String getRecipeInfo() {
            return stack.getDisplayName();
        }
    }
}
