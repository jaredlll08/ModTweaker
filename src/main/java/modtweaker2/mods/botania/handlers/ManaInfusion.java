package modtweaker2.mods.botania.handlers;

import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import modtweaker2.helpers.InputHelper;
import modtweaker2.util.BaseListAddition;
import modtweaker2.util.BaseListRemoval;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.recipe.RecipeManaInfusion;
import static modtweaker2.helpers.InputHelper.*;
import static modtweaker2.helpers.StackHelper.*;


@ZenClass("mods.botania.ManaInfusion")
public class ManaInfusion {
    @ZenMethod
    public static void addInfusion(IItemStack output, IIngredient input, int mana) {
        MineTweakerAPI.apply(new Add(new RecipeManaInfusion(toStack(output), toObject(input), mana)));
    }

    @ZenMethod
    public static void addAlchemy(IItemStack output, IIngredient input, int mana) {
        RecipeManaInfusion recipe = new RecipeManaInfusion(toStack(output), toObject(input), mana);
        recipe.setAlchemy(true);
        MineTweakerAPI.apply(new Add(recipe));
    }

    @ZenMethod
    public static void addConjuration(IItemStack output, IIngredient input, int mana) {
        RecipeManaInfusion recipe = new RecipeManaInfusion(toStack(output), toObject(input), mana);
        recipe.setConjuration(true);
        MineTweakerAPI.apply(new Add(recipe));
    }

    private static class Add extends BaseListAddition {
        public Add(RecipeManaInfusion recipe) {
            super("Botania Mana Infusion", BotaniaAPI.manaInfusionRecipes, recipe);
        }

        @Override
        public String getRecipeInfo() {
            return ((RecipeManaInfusion) recipe).getOutput().getDisplayName();
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @ZenMethod
    public static void removeRecipe(IItemStack output) {
        MineTweakerAPI.apply(new Remove(toStack(output)));
    }

    private static class Remove extends BaseListRemoval {
        public Remove(ItemStack stack) {
            super("Botania Mana Infusion", BotaniaAPI.manaInfusionRecipes, stack);
        }

        @Override
        public void apply() {
            for (RecipeManaInfusion r : BotaniaAPI.manaInfusionRecipes) {
                if (r.getOutput() != null && areEqual(r.getOutput(), stack)) {
                    recipe = r;
                    break;
                }
            }

            BotaniaAPI.manaInfusionRecipes.remove(recipe);
        }

        @Override
        public String getRecipeInfo() {
            return stack.getDisplayName();
        }
    }
}
