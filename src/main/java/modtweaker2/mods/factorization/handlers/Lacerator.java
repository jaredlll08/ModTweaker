package modtweaker2.mods.factorization.handlers;

import static modtweaker2.helpers.InputHelper.toStack;
import static modtweaker2.helpers.StackHelper.areEqual;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import modtweaker2.helpers.ReflectionHelper;
import modtweaker2.mods.factorization.FactorizationHelper;
import modtweaker2.utils.BaseListAddition;
import modtweaker2.utils.BaseListRemoval;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.factorization.Lacerator")
public class Lacerator {
    @ZenMethod
    public static void addRecipe(IItemStack input, IItemStack output, double probability) {
        Object recipe = FactorizationHelper.getLaceratorRecipe(toStack(input), toStack(output), (float) probability);
        MineTweakerAPI.apply(new Add(toStack(input), recipe));
    }

    private static class Add extends BaseListAddition {
        private final ItemStack output;

        public Add(ItemStack output, Object recipe) {
            super("Lacerator", FactorizationHelper.lacerator, recipe);
            this.output = output;
        }

        @Override
        public String getRecipeInfo() {
            return output.getDisplayName();
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @ZenMethod
    public static void removeRecipe(IItemStack output) {
        MineTweakerAPI.apply(new Remove(toStack(output)));
    }

    private static class Remove extends BaseListRemoval {
        public Remove(ItemStack stack) {
            super("Lacerator", FactorizationHelper.lacerator, stack);
        }

        //Returns the output ItemStack
        private ItemStack getOutput(Object o) {
            return (ItemStack) ReflectionHelper.getObject(o, "output");
        }

        @Override
        public void apply() {
            for (Object r : list) {
                ItemStack output = getOutput(r);
                if (output != null && areEqual(output, stack)) {
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
