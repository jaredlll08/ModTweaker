package modtweaker2.mods.botania.handlers;

import static modtweaker2.helpers.InputHelper.toObjects;
import static modtweaker2.helpers.InputHelper.toStack;
import static modtweaker2.helpers.StackHelper.areEqual;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import modtweaker2.utils.BaseListAddition;
import modtweaker2.utils.BaseListRemoval;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.recipe.RecipePetals;
import vazkii.botania.common.item.block.ItemBlockSpecialFlower;

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
                    recipes.add(r);
                }
            }
            super.apply();
        }

        @Override
        public String getRecipeInfo() {
            return stack.getDisplayName();
        }
    }
}
