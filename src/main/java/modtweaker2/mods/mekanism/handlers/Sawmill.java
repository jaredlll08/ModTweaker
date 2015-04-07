package modtweaker2.mods.mekanism.handlers;

import static modtweaker2.helpers.InputHelper.toStack;
import static modtweaker2.helpers.StackHelper.areEqual;

import java.util.Map;

import mekanism.api.ChanceOutput;
import mekanism.common.recipe.RecipeHandler.Recipe;
import mekanism.common.recipe.machines.SawmillRecipe;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import modtweaker2.mods.mekanism.Mekanism;
import modtweaker2.mods.mekanism.util.AddMekanismRecipe;
import modtweaker2.utils.BaseMapRemoval;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.mekanism.Sawmill")
public class Sawmill {
    @ZenMethod
    public static void addRecipe(IItemStack input, IItemStack output1, @Optional IItemStack output2, @Optional double chance) {
        if (Mekanism.v7)
        {
            ChanceOutput chanceOutput = new ChanceOutput(toStack(output1), toStack(output2), chance);
            MineTweakerAPI.apply(new AddMekanismRecipe("PRECISION_SAWMILL", Recipe.PRECISION_SAWMILL.get(), toStack(input), chanceOutput));
        }
        else
        {
            SawmillRecipe recipe = chance>0?new SawmillRecipe(toStack(input), toStack(output1), toStack(output2), chance) : new SawmillRecipe(toStack(input), toStack(output1));
            MineTweakerAPI.apply(new AddMekanismRecipe("PRECISION_SAWMILL", Recipe.PRECISION_SAWMILL.get(), recipe.getInput(), recipe));
        }
    }

    @ZenMethod
    public static void removeRecipe(IItemStack input) {
        if (!Mekanism.v7) throw new UnsupportedOperationException("Function not added to v8 compatibility yet");
        MineTweakerAPI.apply(new Remove(toStack(input)));
    }

    private static class Remove extends BaseMapRemoval {
        public Remove(ItemStack stack) {
            super("Precision Sawmill", Recipe.PRECISION_SAWMILL.get(), stack);
        }

        //We must search through the recipe entries so that we can assign the correct key for removal
        @Override
        public void apply() {
            for (Map.Entry<ItemStack, Object> entry : ((Map<ItemStack, Object>) map).entrySet()) {
                if (entry.getKey() != null && areEqual(entry.getKey(), (ItemStack) stack)) {
                    key = entry.getKey();
                    break;
                }
            }

            super.apply();
        }
    }
}
