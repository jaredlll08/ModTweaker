package modtweaker2.mods.botania.handlers;

import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import modtweaker2.util.BaseListAddition;
import modtweaker2.util.BaseListRemoval;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.recipe.RecipeRuneAltar;
import static modtweaker2.helpers.InputHelper.*;
import static modtweaker2.helpers.StackHelper.*;

@ZenClass("mods.botania.RuneAlter")
public class RuneAltar {
    @ZenMethod
    public static void addRecipe(IItemStack output, IIngredient[] input, int mana) {
        MineTweakerAPI.apply(new Add(new RecipeRuneAltar(toStack(output), mana, toObjects(input))));
    }

    private static class Add extends BaseListAddition {
        public Add(RecipeRuneAltar recipe) {
            super("Botania Rune Altar", BotaniaAPI.runeAltarRecipes, recipe);
        }

        @Override
        public String getRecipeInfo() {
            return ((RecipeRuneAltar) recipe).getOutput().getDisplayName();
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @ZenMethod
    public static void removeRecipe(IItemStack output) {
        MineTweakerAPI.apply(new Remove(toStack(output)));
    }

    private static class Remove extends BaseListRemoval {
        public Remove(ItemStack stack) {
            super("Botania Rune Altar", BotaniaAPI.runeAltarRecipes, stack);
        }

        @Override
        public void apply() {
            for (RecipeRuneAltar r : BotaniaAPI.runeAltarRecipes) {
                if (r.getOutput() != null && areEqual(r.getOutput(), stack)) {
                    recipe = r;
                    break;
                }
            }

            BotaniaAPI.runeAltarRecipes.remove(recipe);
        }

        @Override
        public String getRecipeInfo() {
            return stack.getDisplayName();
        }
    }
}
