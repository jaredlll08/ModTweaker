package modtweaker.mods.factorization.handlers;

import static modtweaker.helpers.InputHelper.toStack;
import static modtweaker.helpers.StackHelper.areEqual;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import modtweaker.helpers.ReflectionHelper;
import modtweaker.mods.factorization.FactorizationHelper;
import modtweaker.util.BaseListAddition;
import modtweaker.util.BaseListRemoval;
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
                    recipe = r;
                    break;
                }
            }

            list.remove(recipe);
        }

        @Override
        public String getRecipeInfo() {
            return stack.getDisplayName();
        }
    }
}
