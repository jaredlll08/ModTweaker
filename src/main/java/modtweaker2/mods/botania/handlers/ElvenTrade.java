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
import vazkii.botania.api.recipe.RecipeElvenTrade;
import static modtweaker2.helpers.InputHelper.*;
import static modtweaker2.helpers.StackHelper.*;

@ZenClass("mods.botania.ElvenTrade")
public class ElvenTrade {
    @ZenMethod
    public static void addRecipe(IItemStack output, IIngredient[] input) {
        MineTweakerAPI.apply(new Add(new RecipeElvenTrade(toStack(output), toObjects(input))));
    }

    private static class Add extends BaseListAddition {
        public Add(RecipeElvenTrade recipe) {
            super("Botania Eleven Trade", BotaniaAPI.elvenTradeRecipes, recipe);
        }

        @Override
        public String getRecipeInfo() {
            return ((RecipeElvenTrade) recipe).getOutput().getDisplayName();
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @ZenMethod
    public static void removeRecipe(IItemStack output) {
        MineTweakerAPI.apply(new Remove(toStack(output)));
    }

    private static class Remove extends BaseListRemoval {
        public Remove(ItemStack stack) {
            super("Botania Elven Trade", BotaniaAPI.elvenTradeRecipes, stack);
        }

        @Override
        public void apply() {
            for (RecipeElvenTrade r : BotaniaAPI.elvenTradeRecipes) {
                if (r.getOutput() != null && areEqual(r.getOutput(), stack)) {
                    recipe = r;
                    break;
                }
            }

            BotaniaAPI.elvenTradeRecipes.remove(recipe);
        }

        @Override
        public String getRecipeInfo() {
            return stack.getDisplayName();
        }
    }
}
