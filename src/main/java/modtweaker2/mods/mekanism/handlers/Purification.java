package modtweaker2.mods.mekanism.handlers;

import static modtweaker2.helpers.InputHelper.toStack;
import static modtweaker2.helpers.StackHelper.areEqual;

import java.util.Map;

import mekanism.api.AdvancedInput;
import mekanism.api.gas.GasRegistry;
import mekanism.common.recipe.RecipeHandler.Recipe;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import modtweaker2.mods.mekanism.util.AddMekanismRecipe;
import modtweaker2.utils.BaseMapRemoval;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.mekanism.Purification")
public class Purification {
    @ZenMethod
    public static void addRecipe(IItemStack input, IItemStack output) {
        AdvancedInput aInput = new AdvancedInput(toStack(input), GasRegistry.getGas("oxygen"));
        MineTweakerAPI.apply(new AddMekanismRecipe("PURIFICATION_CHAMBER", Recipe.PURIFICATION_CHAMBER.get(), aInput, toStack(output)));
    }

    @ZenMethod
    public static void removeRecipe(IItemStack output) {
        MineTweakerAPI.apply(new Remove(toStack(output)));
    }

    private static class Remove extends BaseMapRemoval {
        public Remove(ItemStack stack) {
            super("Purification Chamber", Recipe.PURIFICATION_CHAMBER.get(), stack);
        }

        //We must search through the recipe entries so that we can assign the correct key for removal
        @Override
        public void apply() {
            for (Map.Entry<AdvancedInput, ItemStack> entry : ((Map<AdvancedInput, ItemStack>) map).entrySet()) {
                if (entry.getValue() != null && areEqual(entry.getValue(), (ItemStack) stack)) {
                    key = entry.getKey();
                    break;
                }
            }

            super.apply();
        }
    }
}
