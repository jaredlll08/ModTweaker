package modtweaker.mods.thermalexpansion.handlers;

import static modtweaker.helpers.InputHelper.toFluid;
import static modtweaker.helpers.InputHelper.toStack;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import minetweaker.api.liquid.ILiquidStack;
import modtweaker.mods.thermalexpansion.ThermalHelper;
import modtweaker.util.BaseDescriptionAddition;
import modtweaker.util.BaseDescriptionRemoval;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import thermalexpansion.util.crafting.TransposerManager.RecipeTransposer;
import cofh.lib.inventory.ComparableItemStackSafe;

@ZenClass("mods.thermalexpansion.Transposer")
public class Transposer {
    private static boolean removeValidated(ComparableItemStackSafe stack) {
        //Check if there is a recipe that requires this in the validation set for filling
        for (Map.Entry entry : ThermalHelper.getFillMap().entrySet()) {
            RecipeTransposer recipe = (RecipeTransposer) entry.getValue();
            if (stack.equals(new ComparableItemStackSafe(recipe.getInput()))) {
                return false;
            }
        }

        //Check if there is a recipe that requires this in the validation set for extracting
        for (Map.Entry entry : ThermalHelper.getExtractMap().entrySet()) {
            RecipeTransposer recipe = (RecipeTransposer) entry.getValue();
            if (stack.equals(new ComparableItemStackSafe(recipe.getInput()))) {
                return false;
            }
        }

        return ThermalHelper.transposerValid.remove(stack);
    }

    @ZenMethod
    public static void addFillRecipe(int energy, IItemStack input, IItemStack output, ILiquidStack liquid) {
        addRecipe(energy, input, output, liquid, 100, true);
    }

    @ZenMethod
    public static void addExtractRecipe(int energy, IItemStack input, IItemStack output, ILiquidStack liquid, int chance) {
        addRecipe(energy, input, output, liquid, chance, false);
    }

    @ZenMethod
    public static void addRecipe(int energy, IItemStack input, IItemStack output, ILiquidStack liquid, int chance, boolean isFillRecipe) {
        ItemStack in = toStack(input);
        ItemStack out = toStack(output);
        FluidStack fluid = toFluid(liquid);
        RecipeTransposer recipe = (RecipeTransposer) ThermalHelper.getTERecipe(ThermalHelper.transposerRecipe, in, out, fluid, energy, chance);
        MineTweakerAPI.apply(new Add(in, fluid, recipe, isFillRecipe));
    }

    private static class Add extends BaseDescriptionAddition {
        private List key;
        private boolean isFillRecipe;
        private final ComparableItemStackSafe input;
        private final RecipeTransposer recipe;

        public Add(ItemStack input, FluidStack fluid, RecipeTransposer recipe, boolean isFillRecipe) {
            super("Transposer");
            this.isFillRecipe = isFillRecipe;
            this.input = new ComparableItemStackSafe(input);

            this.recipe = recipe;
            if (this.isFillRecipe) {
                this.key = Arrays.asList(new Integer[] { Integer.valueOf(new ComparableItemStackSafe(input).hashCode()), Integer.valueOf(fluid.fluidID) });
            }
        }

        @Override
        public void apply() {
            if (isFillRecipe) {
                ThermalHelper.getFillMap().put(key, recipe);
            } else {
                ThermalHelper.getExtractMap().put(input, recipe);
            }

            ThermalHelper.transposerValid.add(input);
        }

        @Override
        public boolean canUndo() {
            return input != null;
        }

        @Override
        public void undo() {
            if (isFillRecipe) {
                ThermalHelper.getFillMap().remove(key);
            } else {
                ThermalHelper.getExtractMap().remove(input);
            }

            removeValidated(input);
        }

        @Override
        public String getRecipeInfo() {
            return ((RecipeTransposer) recipe).getOutput().getDisplayName();
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @ZenMethod
    public static void removeFillRecipe(IItemStack input, ILiquidStack liquid) {
        removeRecipe(input, liquid, true);
    }

    @ZenMethod
    public static void removeExtractRecipe(IItemStack input) {
        removeRecipe(input, null, false);
    }

    @ZenMethod
    public static void removeRecipe(IItemStack input, ILiquidStack liquid, boolean isFillRecipe) {
        MineTweakerAPI.apply(new Remove(toStack(input), toFluid(liquid), isFillRecipe));
    }

    private static class Remove extends BaseDescriptionRemoval {
        private final boolean isFillRecipe;
        private final ComparableItemStackSafe input;
        private List key;
        private RecipeTransposer recipe;

        public Remove(ItemStack input, FluidStack fluid, boolean isFillRecipe) {
            super("Transposer");
            this.isFillRecipe = isFillRecipe;
            this.input = new ComparableItemStackSafe(input);
            if (this.isFillRecipe) {
                this.key = Arrays.asList(new Integer[] { Integer.valueOf(new ComparableItemStackSafe(input).hashCode()), Integer.valueOf(fluid.fluidID) });
            }
        }

        @Override
        public void apply() {
            if (isFillRecipe) {
                recipe = ThermalHelper.getFillMap().get(key);
                ThermalHelper.getFillMap().remove(key);
            } else {
                recipe = ThermalHelper.getExtractMap().get(input);
                ThermalHelper.getExtractMap().remove(input);
            }

            removeValidated(input);
        }

        @Override
        public boolean canUndo() {
            return input != null;
        }

        @Override
        public void undo() {
            if (isFillRecipe) {
                ThermalHelper.getFillMap().put(key, recipe);
            } else {
                ThermalHelper.getExtractMap().put(input, recipe);
            }

            ThermalHelper.transposerValid.add(input);
        }

        @Override
        public String getRecipeInfo() {
            return input.toItemStack().getDisplayName();
        }
    }
}
