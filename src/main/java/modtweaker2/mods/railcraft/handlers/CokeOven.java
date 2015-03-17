package modtweaker2.mods.railcraft.handlers;

import static modtweaker2.helpers.InputHelper.toFluid;
import static modtweaker2.helpers.InputHelper.toStack;
import static modtweaker2.helpers.StackHelper.areEqual;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import minetweaker.api.liquid.ILiquidStack;
import mods.railcraft.api.crafting.ICokeOvenRecipe;
import modtweaker2.mods.railcraft.RailcraftHelper;
import modtweaker2.utils.BaseListAddition;
import modtweaker2.utils.BaseListRemoval;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.railcraft.CokeOven")
public class CokeOven {
    @ZenMethod
    public static void addRecipe(IItemStack input, boolean matchDamage, boolean matchNBT, IItemStack output, ILiquidStack fluidOutput, int cookTime) {
        MineTweakerAPI.apply(new Add(RailcraftHelper.getCokeOvenRecipe(toStack(input), matchDamage, matchNBT, toStack(output), toFluid(fluidOutput), cookTime)));
    }

    private static class Add extends BaseListAddition {
        public Add(ICokeOvenRecipe recipe) {
            super("Coke Oven", RailcraftHelper.oven, recipe);
        }

        @Override
        public String getRecipeInfo() {
            return ((ICokeOvenRecipe) recipe).getOutput().getDisplayName();
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @ZenMethod
    public static void removeRecipe(IItemStack output) {
        MineTweakerAPI.apply(new Remove(toStack(output)));
    }

    private static class Remove extends BaseListRemoval {
        public Remove(ItemStack stack) {
            super("Coke Oven", RailcraftHelper.oven, stack);
        }

        @Override
        public void apply() {
            for (ICokeOvenRecipe r : RailcraftHelper.oven) {
                if (r.getOutput() != null && areEqual(r.getOutput(), stack)) {
                    recipe = r;
                    break;
                }
            }

            RailcraftHelper.oven.remove(recipe);
        }

        @Override
        public String getRecipeInfo() {
            return stack.getDisplayName();
        }
    }
}
