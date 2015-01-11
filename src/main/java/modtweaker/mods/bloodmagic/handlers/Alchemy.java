package modtweaker.mods.bloodmagic.handlers;

import static modtweaker.helpers.InputHelper.toStack;
import static modtweaker.helpers.InputHelper.toStacks;
import static modtweaker.helpers.StackHelper.areEqual;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import modtweaker.util.BaseListAddition;
import modtweaker.util.BaseListRemoval;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import WayofTime.alchemicalWizardry.api.alchemy.AlchemyRecipe;
import WayofTime.alchemicalWizardry.api.alchemy.AlchemyRecipeRegistry;

@ZenClass("mods.bloodmagic.Alchemy")
public class Alchemy {
    //Adding a Blood Magic Alchemical Chemistry Set recipe
    @ZenMethod
    public static void addRecipe(IItemStack output, IItemStack[] input, int tier, int lp) {
        MineTweakerAPI.apply(new Add(new AlchemyRecipe(toStack(output), (int) (((double) lp) / 100), toStacks(input), tier)));
    }

    //Passes the list to the base list implementation, and adds the recipe
    private static class Add extends BaseListAddition {
        public Add(AlchemyRecipe recipe) {
            super("Alchemical Chemistry Set", AlchemyRecipeRegistry.recipes, recipe);
        }

        @Override
        public String getRecipeInfo() {
            return ((AlchemyRecipe) recipe).getResult().getDisplayName();
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //Removing a Blood Magic Alchemical Chemistry Set recipe
    @ZenMethod
    public static void removeRecipe(IItemStack output) {
        MineTweakerAPI.apply(new Remove(toStack(output)));
    }

    //Removes a recipe, apply is never the same for anything, so will always need to override it
    private static class Remove extends BaseListRemoval {
        public Remove(ItemStack stack) {
            super("Alchemical Chemistry Set", AlchemyRecipeRegistry.recipes, stack);
        }

        //Loops through the registry, to find the item that matches, saves that recipe then removes it
        @Override
        public void apply() {
            for (AlchemyRecipe r : AlchemyRecipeRegistry.recipes) {
                if (r.getResult() != null && areEqual(r.getResult(), stack)) {
                    recipe = r;
                    break;
                }
            }

            AlchemyRecipeRegistry.recipes.remove(recipe);
        }

        @Override
        public String getRecipeInfo() {
            return stack.getDisplayName();
        }
    }
}
