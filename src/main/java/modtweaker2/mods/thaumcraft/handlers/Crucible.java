package modtweaker2.mods.thaumcraft.handlers;

import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import modtweaker2.mods.thaumcraft.ThaumcraftHelper;
import modtweaker2.util.BaseListAddition;
import modtweaker2.util.BaseListRemoval;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.crafting.CrucibleRecipe;
import static modtweaker2.helpers.InputHelper.*;
import static modtweaker2.helpers.StackHelper.*;

@ZenClass("mods.thaumcraft.Crucible")
public class Crucible {
    @ZenMethod
    public static void addRecipe(String key, IItemStack result, IIngredient catalyst, String aspects) {
        MineTweakerAPI.apply(new Add(new CrucibleRecipe(key, toStack(result), toObject(catalyst), ThaumcraftHelper.parseAspects(aspects))));
    }

    private static class Add extends BaseListAddition {
        public Add(CrucibleRecipe recipe) {
            super("Thaumcraft Crucible", ThaumcraftApi.getCraftingRecipes(), recipe);
        }

        @Override
        public String getRecipeInfo() {
            return ((CrucibleRecipe) recipe).getRecipeOutput().getDisplayName();
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @ZenMethod
    public static void removeRecipe(IItemStack output) {
        MineTweakerAPI.apply(new Remove(toStack(output)));
    }

    private static class Remove extends BaseListRemoval {
        public Remove(ItemStack stack) {
            super("Thaumcraft Crucible", ThaumcraftApi.getCraftingRecipes(), stack);
        }

        @Override
        public void apply() {
            for (Object o : ThaumcraftApi.getCraftingRecipes()) {
                if (o instanceof CrucibleRecipe) {
                    CrucibleRecipe r = (CrucibleRecipe) o;
                    if (r.getRecipeOutput() != null && areEqual(r.getRecipeOutput(), stack)) {
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
