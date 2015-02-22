package modtweaker2.mods.thermalexpansion.handlers;

import static modtweaker2.helpers.InputHelper.toFluid;
import static modtweaker2.helpers.InputHelper.toStack;
import static modtweaker2.mods.thermalexpansion.ThermalHelper.removeCrucibleRecipe;
import minetweaker.IUndoableAction;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import minetweaker.api.liquid.ILiquidStack;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import cofh.thermalexpansion.util.crafting.CrucibleManager;
import cofh.thermalexpansion.util.crafting.CrucibleManager.RecipeCrucible;

@ZenClass("mods.thermalexpansion.Crucible")
public class Crucible {
    @ZenMethod
    public static void addRecipe(int energy, IItemStack input, ILiquidStack output) {
        MineTweakerAPI.apply(new Add(energy, toStack(input), toFluid(output)));
    }

    private static class Add implements IUndoableAction {
        ItemStack input;
        FluidStack output;
        int energy;
        boolean applied = false;

        public Add(int rf, ItemStack inp, FluidStack out) {
            energy = rf;
            input = inp;
            output = out;
        }

        public void apply() {
            applied = CrucibleManager.addRecipe(energy, input, output);
        }

        public boolean canUndo() {
            return input != null && applied;
        }

        public String describe() {
            return "Adding TE Magma Crucible Recipe using " + input.getDisplayName();
        }

        public void undo() {
            removeCrucibleRecipe(input);
        }

        public String describeUndo() {
            return "Removing TE Magma Crucible Recipe using " + input.getDisplayName();
        }

        public Object getOverrideKey() {
            return null;
        }

    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @ZenMethod
    public static void removeRecipe(IItemStack input) {
        MineTweakerAPI.apply(new Remove(toStack(input)));
    }

    private static class Remove implements IUndoableAction {
        ItemStack input;
        RecipeCrucible removed;

        public Remove(ItemStack inp) {
            input = inp;
        }

        public void apply() {
            removed = CrucibleManager.getRecipe(input);
            removeCrucibleRecipe(input);
        }

        public boolean canUndo() {
            return removed != null;
        }

        public String describe() {
            return "Removing TE Magma Crucible Recipe using " + input.getDisplayName();
        }

        public void undo() {
            CrucibleManager.addRecipe(removed.getEnergy(), removed.getInput(), removed.getOutput());
        }

        public String describeUndo() {
            return "Restoring TE Magma Crucible Recipe using " + input.getDisplayName();
        }

        public Object getOverrideKey() {
            return null;
        }

    }

}
