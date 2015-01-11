package modtweaker.mods.factorization.handlers;

import static modtweaker.helpers.InputHelper.toStack;
import static modtweaker.helpers.StackHelper.areEqual;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import modtweaker.helpers.ReflectionHelper;
import modtweaker.mods.factorization.FactorizationHelper;
import modtweaker.util.BaseListAddition;
import modtweaker.util.BaseMultipleListRemoval;
import modtweaker.util.BaseMultipleListRemoval.Position;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.factorization.SlagFurnace")
public class SlagFurnace {
    @ZenMethod
    public static void addRecipe(IItemStack input, IItemStack output1, double chance1, IItemStack output2, double chance2) {
        Object recipe = FactorizationHelper.getSlagFurnaceRecipe(toStack(input), (float) chance1, toStack(output1), (float) chance2, toStack(output2));
        MineTweakerAPI.apply(new Add(toStack(input), recipe));
    }

    private static class Add extends BaseListAddition {
        private final ItemStack input;

        public Add(ItemStack input, Object recipe) {
            super("Slag Furnace", FactorizationHelper.slag, recipe);
            this.input = input;
        }

        @Override
        public String getRecipeInfo() {
            return input.getDisplayName();
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @ZenMethod
    public static void removeRecipe(IItemStack input) {
        MineTweakerAPI.apply(new Remove(toStack(input), Position.ALL));
    }

    @ZenMethod
    public static void removeFirst(IItemStack output) {
        MineTweakerAPI.apply(new Remove(toStack(output), Position.FIRST));
    }

    @ZenMethod
    public static void removeLast(IItemStack output) {
        MineTweakerAPI.apply(new Remove(toStack(output), Position.LAST));
    }

    private static class Remove extends BaseMultipleListRemoval {
        public Remove(ItemStack stack, Position pos) {
            super("Slag Furnace", FactorizationHelper.slag, stack, pos);
        }

        @Override
        protected boolean isEqual(Object recipe, Object search) {
            ItemStack input = (ItemStack) ReflectionHelper.getObject(recipe, "input");
            if (input != null && areEqual(input, (ItemStack) search)) {
                return true;
            } else return false;
        }

        @Override
        public String getRecipeInfo() {
            return ((ItemStack) search).getDisplayName();
        }
    }
}
