package modtweaker.mods.thaumcraft.handlers;

import static modtweaker.helpers.InputHelper.toObjects;
import static modtweaker.helpers.InputHelper.toShapedObjects;
import static modtweaker.helpers.InputHelper.toStack;
import static modtweaker.helpers.StackHelper.areEqual;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import modtweaker.mods.thaumcraft.ThaumcraftHelper;
import modtweaker.util.BaseListAddition;
import modtweaker.util.BaseListRemoval;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.crafting.IArcaneRecipe;
import thaumcraft.api.crafting.ShapedArcaneRecipe;
import thaumcraft.api.crafting.ShapelessArcaneRecipe;

@ZenClass("mods.thaumcraft.Arcane")
public class Arcane {
    @ZenMethod
    public static void addShaped(String key, IItemStack output, String aspects, IIngredient[][] ingredients) {
        MineTweakerAPI.apply(new Add(new ShapedArcaneRecipe(key, toStack(output), ThaumcraftHelper.parseAspects(aspects), toShapedObjects(ingredients))));
    }

    @ZenMethod
    public static void addShapeless(String key, IItemStack output, String aspects, IIngredient[] ingredients) {
        MineTweakerAPI.apply(new Add(new ShapelessArcaneRecipe(key, toStack(output), ThaumcraftHelper.parseAspects(aspects), toObjects(ingredients))));
    }

    private static class Add extends BaseListAddition {
        public Add(IArcaneRecipe recipe) {
            super("Thaumcraft Arcane Worktable", ThaumcraftApi.getCraftingRecipes(), recipe);
        }

        @Override
        public String getRecipeInfo() {
            Object out = ((IArcaneRecipe) recipe).getRecipeOutput();
            if (out instanceof ItemStack) {
                return ((ItemStack) out).getDisplayName();
            } else return super.getRecipeInfo();
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @ZenMethod
    public static void removeRecipe(IItemStack output) {
        MineTweakerAPI.apply(new Remove(toStack(output)));
    }

    private static class Remove extends BaseListRemoval {
        public Remove(ItemStack stack) {
            super("Thaumcraft Infusion", ThaumcraftApi.getCraftingRecipes(), stack);
        }

        @Override
        public void apply() {
            for (Object o : ThaumcraftApi.getCraftingRecipes()) {
                if (o instanceof IArcaneRecipe) {
                    IArcaneRecipe r = (IArcaneRecipe) o;
                    if (r.getRecipeOutput() != null && r.getRecipeOutput() instanceof ItemStack && areEqual((ItemStack) r.getRecipeOutput(), stack)) {
                        recipe = r;
                        break;
                    }
                }
            }

            ThaumcraftApi.getCraftingRecipes().remove(recipe);
        }

        @Override
        public String getRecipeInfo() {
            return stack.getDisplayName();
        }
    }
}
