package modtweaker.mods.bloodmagic.handlers;

import static modtweaker.helpers.InputHelper.toStack;
import static modtweaker.helpers.StackHelper.areEqual;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import modtweaker.util.BaseListAddition;
import modtweaker.util.BaseListRemoval;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import WayofTime.alchemicalWizardry.api.bindingRegistry.BindingRecipe;
import WayofTime.alchemicalWizardry.api.bindingRegistry.BindingRegistry;

@ZenClass("mods.bloodmagic.Binding")
public class Binding {
    //Adding a Blood Magic Binding recipe
    @ZenMethod
    public static void addRecipe(IItemStack input, IItemStack output) {
        MineTweakerAPI.apply(new Add(new BindingRecipe(toStack(output), toStack(input))));
    }

    //Passes the list to the base list implementation, and adds the recipe
    private static class Add extends BaseListAddition {
        public Add(BindingRecipe recipe) {
            super("Binding", BindingRegistry.bindingRecipes, recipe);
        }

        @Override
        public String getRecipeInfo() {
            return ((BindingRecipe) recipe).getResult().getDisplayName();
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //Removing a Blood Magic Binding recipe
    @ZenMethod
    public static void removeRecipe(IItemStack output) {
        MineTweakerAPI.apply(new Remove(toStack(output)));
    }

    //Removes a recipe, apply is never the same for anything, so will always need to override it
    private static class Remove extends BaseListRemoval {
        public Remove(ItemStack stack) {
            super("Binding", BindingRegistry.bindingRecipes, stack);
        }

        //Loops through the registry, to find the item that matches, saves that recipe then removes it
        @Override
        public void apply() {
            for (BindingRecipe r : BindingRegistry.bindingRecipes) {
                if (r.getResult() != null && areEqual(r.getResult(), stack)) {
                    recipe = r;
                    break;
                }
            }

            BindingRegistry.bindingRecipes.remove(recipe);
        }

        @Override
        public String getRecipeInfo() {
            return stack.getDisplayName();
        }
    }
}
