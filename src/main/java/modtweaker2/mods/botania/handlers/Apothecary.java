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
import vazkii.botania.api.recipe.RecipePetals;
import vazkii.botania.common.item.block.ItemBlockSpecialFlower;
import static modtweaker2.helpers.InputHelper.*;
import static modtweaker2.helpers.StackHelper.*;

@ZenClass("mods.botania.Apothecary")
public class Apothecary {
    @ZenMethod
    public static void addRecipe(IItemStack output, IIngredient[] input) {
        MineTweakerAPI.apply(new Add(new RecipePetals(toStack(output), toObjects(input))));
    }

    @ZenMethod
    public static void addRecipe(String output, IIngredient[] input) {
        MineTweakerAPI.apply(new Add(new RecipePetals(ItemBlockSpecialFlower.ofType(output), toObjects(input))));
    }

    private static class Add extends BaseListAddition {
        public Add(RecipePetals recipe) {
            super("Botania Petal Recipe", BotaniaAPI.petalRecipes, recipe);
        }

        @Override
        public String getRecipeInfo() {
            return ((RecipePetals) recipe).getOutput().getDisplayName();
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @ZenMethod
    public static void removeRecipe(IItemStack output) {
        MineTweakerAPI.apply(new Remove(toStack(output)));
    }

    @ZenMethod
    public static void removeRecipe(String output) {
        MineTweakerAPI.apply(new Remove(ItemBlockSpecialFlower.ofType(output)));
    }

    private static class Remove extends BaseListRemoval {
        public Remove(ItemStack stack) {
            super("Botania Petal recipe", BotaniaAPI.petalRecipes, stack);
        }

        @Override
        public void apply() {
            for (RecipePetals r : BotaniaAPI.petalRecipes) {
                if (areEqual(r.getOutput(), stack)) {
                    recipe = r;
                    break;
                }
            }

            BotaniaAPI.petalRecipes.remove(recipe);
        }

        @Override
        public String getRecipeInfo() {
            return stack.getDisplayName();
        }
    }
}
