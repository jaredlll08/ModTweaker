package modtweaker.mods.pneumaticcraft.handlers;

import static modtweaker.helpers.InputHelper.toStacks;
import static modtweaker.helpers.StackHelper.areEqual;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import modtweaker.util.BaseListAddition;
import modtweaker.util.BaseListRemoval;
import net.minecraft.item.ItemStack;
import pneumaticCraft.api.recipe.PressureChamberRecipe;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.pneumaticcraft.Pressure")
public class Pressure {
    @ZenMethod
    public static void addRecipe(IItemStack[] input, double pressure, IItemStack[] output, boolean asBlock) {
        MineTweakerAPI.apply(new Add(new PressureChamberRecipe(toStacks(input), (float) pressure, toStacks(output), asBlock)));
    }

    private static class Add extends BaseListAddition {
        private ItemStack stack;

        public Add(PressureChamberRecipe recipe) {
            super("Pneumaticraft Pressure Chamber", PressureChamberRecipe.chamberRecipes, recipe);
            this.stack = recipe.output[0];
        }

        @Override
        public String getRecipeInfo() {
            return stack.getDisplayName();
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @ZenMethod
    public static void removeRecipe(IItemStack[] output) {
        MineTweakerAPI.apply(new Remove(toStacks(output)));
    }

    private static class Remove extends BaseListRemoval {
        private final ItemStack[] stacks;

        public Remove(ItemStack[] stacks) {
            super("Pneumaticraft Pressure Chamber", PressureChamberRecipe.chamberRecipes, stacks[0]);
            this.stacks = stacks;
        }

        @Override
        public void apply() {
            for (PressureChamberRecipe r : PressureChamberRecipe.chamberRecipes) {
                boolean matches = true;
                for (int i = 0; i < stacks.length; i++) {
                    if (!areEqual(stacks[i], r.output[i])) {
                        matches = false;
                        break;
                    }
                }

                if (matches) {
                    recipe = r;
                    break;
                }
            }

            PressureChamberRecipe.chamberRecipes.remove(recipe);
        }

        @Override
        public String getRecipeInfo() {
            return stack.getDisplayName();
        }
    }
}
