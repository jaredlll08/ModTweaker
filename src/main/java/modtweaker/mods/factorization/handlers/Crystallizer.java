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

@ZenClass("mods.factorization.Crystallizer")
public class Crystallizer {
    @ZenMethod
    public static void addRecipe(IItemStack input, IItemStack output, IItemStack solution, double output_count) {
        Object recipe = FactorizationHelper.getCrystallizerRecipe(toStack(input), toStack(output), toStack(solution), (float) output_count);
        MineTweakerAPI.apply(new Add(toStack(input), recipe));
    }

    private static class Add extends BaseListAddition {
        private final ItemStack output;

        public Add(ItemStack output, Object recipe) {
            super("Crystallizer", FactorizationHelper.crystallizer, recipe);
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
            super("Crystallizer", FactorizationHelper.crystallizer, stack);
        }

        //Returns the output ItemStack
        private ItemStack getOutput(Object o) {
            return (ItemStack) ReflectionHelper.getObject(o, "output");
        }

        @Override
        public void apply() {
            for (Object r : list) {
                if (r != null) {
                    ItemStack output = getOutput(r);
                    if (output != null && areEqual(output, stack)) {
                        recipe = r;
                        break;
                    }
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
