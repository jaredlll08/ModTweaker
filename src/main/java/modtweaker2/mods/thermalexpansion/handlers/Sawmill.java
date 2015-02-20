package modtweaker2.mods.thermalexpansion.handlers;

import minetweaker.IUndoableAction;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import thermalexpansion.util.crafting.SawmillManager;
import thermalexpansion.util.crafting.SawmillManager.RecipeSawmill;
import static modtweaker2.helpers.InputHelper.*;
import static modtweaker2.helpers.StackHelper.*;

@ZenClass("mods.thermalexpansion.Sawmill")
public class Sawmill {
    @ZenMethod
    public static void addRecipe(int energy, IItemStack input, IItemStack output) {
        MineTweakerAPI.apply(new Add(energy, toStack(input), toStack(output), null, 0));
    }

    @ZenMethod
    public static void addRecipe(int energy, IItemStack input, IItemStack output, IItemStack secondary, int secondaryChance) {
        MineTweakerAPI.apply(new Add(energy, toStack(input), toStack(output), toStack(secondary), secondaryChance));
    }

    private static class Add implements IUndoableAction {
        ItemStack input;
        ItemStack output;
        ItemStack secondary;
        int secondaryChance;
        int energy;
        boolean applied = false;

        public Add(int rf, ItemStack inp, ItemStack out, ItemStack sec, int chance) {
            energy = rf;
            input = inp;
            output = out;
            secondary = sec;
            secondaryChance = chance;
        }

        public void apply() {
            applied = SawmillManager.addRecipe(energy, input, output, secondary, secondaryChance);
        }

        public boolean canUndo() {
            return input != null && applied;
        }

        public String describe() {
            return "Adding Sawmill Recipe using " + input.getDisplayName();
        }

        public void undo() {
            SawmillManager.removeRecipe(input);
        }

        public String describeUndo() {
            return "Removing Sawmill Recipe using " + input.getDisplayName();
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
        RecipeSawmill removed;

        public Remove(ItemStack inp) {
            input = inp;
        }

        public void apply() {
            removed = SawmillManager.getRecipe(input);
            SawmillManager.removeRecipe(input);
        }

        public boolean canUndo() {
            return removed != null;
        }

        public String describe() {
            return "Removing Sawmill Recipe using " + input.getDisplayName();
        }

        public void undo() {
            SawmillManager.addRecipe(removed.getEnergy(), removed.getInput(), removed.getPrimaryOutput(), removed.getSecondaryOutput(), removed.getSecondaryOutputChance());
        }

        public String describeUndo() {
            return "Restoring Sawmill Recipe using " + input.getDisplayName();
        }

        public Object getOverrideKey() {
            return null;
        }

    }

}
