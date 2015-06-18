package modtweaker2.mods.tconstruct.handlers;

import static modtweaker2.helpers.InputHelper.toStack;
import static modtweaker2.helpers.StackHelper.areEqual;

import java.util.ArrayList;

import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import modtweaker2.mods.tconstruct.TConstructHelper;
import modtweaker2.utils.BaseListAddition;
import modtweaker2.utils.BaseListRemoval;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import tconstruct.library.crafting.DryingRackRecipes;
import tconstruct.library.crafting.DryingRackRecipes.DryingRecipe;

@ZenClass("mods.tconstruct.Drying")
public class Drying {
    //Adding a TConstruct Drying Rack recipe
    @ZenMethod
    public static void addRecipe(IItemStack input, IItemStack output, int time) {
        MineTweakerAPI.apply(new Add(TConstructHelper.getDryingRecipe(toStack(input), time, toStack(output))));
    }

    //Passes the list to the base list implementation, and adds the recipe
    private static class Add extends BaseListAddition {
        public Add(DryingRecipe recipe) {
            super("TConstruct Drying Rack", DryingRackRecipes.recipes, recipe);
        }

        @Override
        public String getRecipeInfo() {
            return ((DryingRecipe) recipe).result.getDisplayName();
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //Removing a TConstruct Drying Rack recipe
    @ZenMethod
    public static void removeRecipe(IItemStack output) {
        MineTweakerAPI.apply(new Remove((toStack(output))));
    }

    //Removes a recipe, apply is never the same for anything, so will always need to override it
    private static class Remove extends BaseListRemoval {
        public Remove(ItemStack output) {
            super("TConstruct Drying Rack", DryingRackRecipes.recipes, output);
        }

        //Loops through the registry, to find the item that matches, saves that recipe then removes it
        @Override
        public void apply() {
            for (DryingRecipe r : (ArrayList<DryingRecipe>) list) {
                if (r.result != null && areEqual(r.result, stack)) {
                    recipes.add(r);
                }
            }
            super.apply();
        }

        @Override
        public String getRecipeInfo() {
            return stack.getDisplayName();
        }
    }
}
