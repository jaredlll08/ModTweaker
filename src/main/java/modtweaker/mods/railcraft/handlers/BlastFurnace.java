package modtweaker.mods.railcraft.handlers;

import static modtweaker.helpers.InputHelper.toStack;
import static modtweaker.helpers.StackHelper.areEqual;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import mods.railcraft.api.crafting.IBlastFurnaceRecipe;
import modtweaker.mods.railcraft.RailcraftHelper;
import modtweaker.util.BaseListAddition;
import modtweaker.util.BaseListRemoval;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.railcraft.BlastFurnace")
public class BlastFurnace {
    @ZenMethod
    public static void addRecipe(IItemStack input, boolean matchDamage, boolean matchNBT, int cookTime, IItemStack output) {
        MineTweakerAPI.apply(new Add(RailcraftHelper.getBlastFurnaceRecipe(toStack(input), matchDamage, matchNBT, cookTime, toStack(output))));
    }

    private static class Add extends BaseListAddition {
        public Add(IBlastFurnaceRecipe recipe) {
            super("Blast Furnace", RailcraftHelper.furnace, recipe);
        }

        @Override
        public String getRecipeInfo() {
            return ((IBlastFurnaceRecipe) recipe).getOutput().getDisplayName();
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @ZenMethod
    public static void removeRecipe(IItemStack output) {
        MineTweakerAPI.apply(new Remove(toStack(output)));
    }

    private static class Remove extends BaseListRemoval {
        public Remove(ItemStack stack) {
            super("Blast Furnace", RailcraftHelper.furnace, stack);
        }

        @Override
        public void apply() {
            for (IBlastFurnaceRecipe r : RailcraftHelper.furnace) {
                if (r != null) {
                    if (r.getOutput() != null && areEqual(r.getOutput(), stack)) {
                        recipe = r;
                        break;
                    }
                }
            }

            RailcraftHelper.furnace.remove(recipe);
        }

        @Override
        public String getRecipeInfo() {
            return stack.getDisplayName();
        }
    }
}
