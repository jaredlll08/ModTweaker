package modtweaker2.mods.metallurgy.handlers;

import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import modtweaker2.mods.metallurgy.MetallurgyHelper;
import modtweaker2.util.BaseMapAddition;
import modtweaker2.util.BaseMapRemoval;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import static modtweaker2.helpers.InputHelper.*;
import static modtweaker2.helpers.StackHelper.*;

@ZenClass("mods.metallurgy.Crusher")
public class Crusher {
    //Adding a Metallurgy Crusher Recipe
    @ZenMethod
    public static void addRecipe(IItemStack input, IItemStack output) {
        MineTweakerAPI.apply(new Add(toStack(input), toStack(output)));
    }

    private static class Add extends BaseMapAddition {
        private final ItemStack stack;

        public Add(ItemStack input, ItemStack output) {
            super("Metallurgy Crusher", MetallurgyHelper.crusherMetaList, MetallurgyHelper.getCrusherKey(input), output);
            this.stack = input;
        }

        @Override
        public void apply() {
            super.apply();
            addToInput(stack, (ItemStack) recipe);
        }

        @Override
        public void undo() {
            super.undo();
            removeFromInput(stack, (ItemStack) recipe);
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //Removing a Metallurgy Crusher recipe
    @ZenMethod
    public static void removeRecipe(IItemStack input) {
        MineTweakerAPI.apply(new Remove(toStack(input)));
    }

    private static class Remove extends BaseMapRemoval {
        public Remove(ItemStack stack) {
            super("Metallurgy Crusher", MetallurgyHelper.crusherMetaList, MetallurgyHelper.getCrusherKey(stack), stack);
        }

        @Override
        public String getRecipeInfo() {
            return ((ItemStack) stack).getDisplayName();
        }

        @Override
        public void apply() {
            super.apply();
            removeFromInput(((ItemStack) stack), (ItemStack) recipe);
        }

        @Override
        public void undo() {
            super.undo();
            addToInput(((ItemStack) stack), (ItemStack) recipe);
        }
    }

    //Helper Methods
    private static void addToInput(ItemStack input, ItemStack output) {
        ItemStack[] inputList = MetallurgyHelper.crusherInputList.get(output.getUnlocalizedName());
        if (inputList == null) {
            inputList = new ItemStack[1];
            inputList[0] = input;
        } else {
            ItemStack[] newList = new ItemStack[inputList.length + 1];
            for (int i = 0; i < inputList.length; i++) {
                newList[i] = inputList[i];
            }

            newList[inputList.length] = input;

            inputList = newList;
        }

        MetallurgyHelper.crusherInputList.put(output.getUnlocalizedName(), inputList);
    }

    private static void removeFromInput(ItemStack input, ItemStack output) {
        boolean switched = false;
        ItemStack[] inputList = MetallurgyHelper.crusherInputList.get(output.getUnlocalizedName());
        ItemStack[] newList = new ItemStack[inputList.length - 1];
        if (newList.length >= 1) {
            for (int i = 0; i < newList.length; i++) {
                if (switched || (areEqual(inputList[i], input))) {
                    switched = true;
                    newList[i] = inputList[i + 1];
                } else newList[i] = inputList[i];
            }
        } else newList = null;

        inputList = newList;
        if (inputList == null || inputList.length <= 0) {
            MetallurgyHelper.crusherInputList.remove(output.getUnlocalizedName());
        } else {
            MetallurgyHelper.crusherInputList.put(output.getUnlocalizedName(), inputList);
        }
    }
}
