package modtweaker2.mods.metallurgy.handlers;

import static modtweaker2.helpers.InputHelper.toStack;
import static modtweaker2.helpers.StackHelper.areEqual;

import java.util.Map.Entry;

import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import modtweaker2.helpers.InputHelper;
import modtweaker2.mods.metallurgy.MetallurgyHelper;
import modtweaker2.utils.BaseMapAddition;
import modtweaker2.utils.BaseMapRemoval;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.metallurgy.Crusher")
public class Crusher {
    //Adding a Metallurgy Crusher Recipe
    @ZenMethod
    public static void addRecipe(IItemStack input, IItemStack output) {
        MineTweakerAPI.apply(new Add(toStack(input), toStack(output)));
    }

    private static class Add extends BaseMapAddition<String, ItemStack> {
        private final ItemStack stack;

        public Add(ItemStack input, ItemStack output) {
            super("Metallurgy Crusher", MetallurgyHelper.crusherMetaList);
            this.stack = input;
            recipes.put(MetallurgyHelper.getCrusherKey(input), output);
        }

        @Override
        public void apply() {
            super.apply();
            for(Entry<String, ItemStack> recipe : successful.entrySet()) {
                addToInput(stack, recipe.getValue());
            }
        }

        @Override
        public void undo() {
            super.undo();
            for(Entry<String, ItemStack> recipe : successful.entrySet()) {
                removeFromInput(stack, recipe.getValue());
            }
        }
        
        @Override
        protected String getRecipeInfo(Entry<String, ItemStack> recipe) {
            return InputHelper.getStackDescription(recipe.getValue());
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //Removing a Metallurgy Crusher recipe
    @ZenMethod
    public static void removeRecipe(IItemStack input) {
        MineTweakerAPI.apply(new Remove(toStack(input)));
    }

    private static class Remove extends BaseMapRemoval<String, ItemStack> {
        private ItemStack stack;
        
        public Remove(ItemStack stack) {
            super("Metallurgy Crusher", MetallurgyHelper.crusherMetaList);
            
            map.put(MetallurgyHelper.getCrusherKey(stack), MetallurgyHelper.crusherMetaList.get(MetallurgyHelper.getCrusherKey(stack)));
            this.stack = stack;
        }

        @Override
        public void apply() {
            super.apply();
            for(Entry<String, ItemStack> recipe : successful.entrySet()) {
                removeFromInput(stack, recipe.getValue());
            }
        }

        @Override
        public void undo() {
            super.undo();
            for(Entry<String, ItemStack> recipe : successful.entrySet()) {
                addToInput(stack, recipe.getValue());
            }
        }
        
        @Override
        protected String getRecipeInfo(Entry<String, ItemStack> recipe) {
            return InputHelper.getStackDescription(recipe.getValue());
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
