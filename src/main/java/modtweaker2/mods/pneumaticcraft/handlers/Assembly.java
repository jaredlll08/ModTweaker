package modtweaker2.mods.pneumaticcraft.handlers;

import static modtweaker2.helpers.InputHelper.toStack;
import static modtweaker2.helpers.StackHelper.areEqual;

import java.util.List;

import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import modtweaker2.utils.BaseListAddition;
import modtweaker2.utils.BaseListRemoval;
import net.minecraft.item.ItemStack;
import pneumaticCraft.api.recipe.AssemblyRecipe;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.pneumaticcraft.Assembly")
public class Assembly {
    @ZenMethod
    public static void addDrillRecipe(IItemStack input, IItemStack output) {
        MineTweakerAPI.apply(new Add(new AssemblyRecipe(toStack(input), toStack(output)), AssemblyRecipe.drillRecipes));
    }

    @ZenMethod
    public static void addLaserRecipe(IItemStack input, IItemStack output) {
        MineTweakerAPI.apply(new Add(new AssemblyRecipe(toStack(input), toStack(output)), AssemblyRecipe.laserRecipes));
    }

    @ZenMethod
    public static void addLaserDrillRecipe(IItemStack input, IItemStack output) {
        MineTweakerAPI.apply(new Add(new AssemblyRecipe(toStack(input), toStack(output)), AssemblyRecipe.drillLaserRecipes));
    }

    private static class Add extends BaseListAddition {
        public Add(AssemblyRecipe recipe, List list) {
            super("Pneumaticraft Assembly", list, recipe);
        }

        @Override
        public String getRecipeInfo() {
            return ((AssemblyRecipe) recipe).getOutput().getDisplayName();
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @ZenMethod
    public static void removeDrillRecipe(IItemStack output) {
        MineTweakerAPI.apply(new Remove(toStack(output), AssemblyRecipe.drillRecipes));
    }

    @ZenMethod
    public static void removeLaserRecipe(IItemStack output) {
        MineTweakerAPI.apply(new Remove(toStack(output), AssemblyRecipe.laserRecipes));
    }

    @ZenMethod
    public static void removeLaserDrillRecipe(IItemStack output) {
        MineTweakerAPI.apply(new Remove(toStack(output), AssemblyRecipe.drillLaserRecipes));
    }

    private static class Remove extends BaseListRemoval {
        public Remove(ItemStack stack, List list) {
            super("Pneumaticraft Assembly", list, stack);
        }

        @Override
        public void apply() {
            for (AssemblyRecipe r : (List<AssemblyRecipe>) list) {
                if (r.getOutput() != null && areEqual(r.getOutput(), stack)) {
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
