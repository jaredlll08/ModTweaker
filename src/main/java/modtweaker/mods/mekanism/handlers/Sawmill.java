package modtweaker.mods.mekanism.handlers;

import static modtweaker.helpers.InputHelper.toStack;
import static modtweaker.helpers.StackHelper.areEqual;

import java.util.Map;

import mekanism.api.ChanceOutput;
import mekanism.common.recipe.RecipeHandler.Recipe;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import modtweaker.mods.mekanism.util.AddMekanismRecipe;
import modtweaker.util.BaseMapRemoval;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.mekanism.Sawmill")
public class Sawmill {
    @ZenMethod
    public static void addRecipe(IItemStack input, IItemStack output1, @Optional IItemStack output2, @Optional double chance) {
        ChanceOutput chanceOutput = new ChanceOutput(toStack(output1), toStack(output2), chance);
        MineTweakerAPI.apply(new AddMekanismRecipe("PRECISION_SAWMILL", Recipe.PRECISION_SAWMILL.get(), toStack(input), chanceOutput));
        Minecraft.getMinecraft().getLanguageManager().getCurrentLanguage().getLanguageCode();
    }

    @ZenMethod
    public static void removeRecipe(IItemStack input) {
        MineTweakerAPI.apply(new Remove(toStack(input)));
    }

    private static class Remove extends BaseMapRemoval {
        public Remove(ItemStack stack) {
            super("Precision Sawmill", Recipe.PRECISION_SAWMILL.get(), stack);
        }

        //We must search through the recipe entries so that we can assign the correct key for removal
        @Override
        public void apply() {
            for (Map.Entry<ItemStack, ChanceOutput> entry : ((Map<ItemStack, ChanceOutput>) map).entrySet()) {
                if (entry.getKey() != null && areEqual(entry.getKey(), (ItemStack) stack)) {
                    key = entry.getKey();
                    break;
                }
            }

            super.apply();
        }
    }
}
