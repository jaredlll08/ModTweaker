package modtweaker.mods.thermalexpansion.handlers;

import static modtweaker.helpers.InputHelper.toStack;
import minetweaker.IUndoableAction;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import thermalexpansion.util.crafting.FurnaceManager;
import thermalexpansion.util.crafting.FurnaceManager.RecipeFurnace;

@ZenClass("mods.thermalexpansion.Furnace")
public class Furnace {
    @ZenMethod
    public static void addRecipe(int energy, IItemStack input, IItemStack output) {
        MineTweakerAPI.apply(new Add(energy, toStack(input), toStack(output)));
    }

    private static class Add implements IUndoableAction {
        ItemStack input;
        ItemStack output;
        int energy;
        boolean applied = false;

        public Add(int rf, ItemStack inp, ItemStack out) {
            energy = rf;
            input = inp;
            output = out;
        }

        public void apply() {
            FurnaceManager.addRecipe(energy, input, output, false);
        }

        public boolean canUndo() {
            return input != null;
        }

        public String describe() {
            return "Adding Redstone Furnace Recipe using " + input.getDisplayName();
        }

        public void undo() {
            FurnaceManager.removeRecipe(input);
        }

        public String describeUndo() {
            return "Removing Redstone Furnace Recipe using " + input.getDisplayName();
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
        RecipeFurnace removed;

        public Remove(ItemStack inp) {
            input = inp;
        }
        public void apply() {
            removed = FurnaceManager.getRecipe(input);
            FurnaceManager.removeRecipe(input);
        }

        public boolean canUndo() {
            return removed != null;
        }

        public String describe() {
            return "Removing Redstone Furnace Recipe using " + input.getDisplayName();
        }

        public void undo() {
            FurnaceManager.addRecipe(removed.getEnergy(), removed.getInput(), removed.getOutput(), false);
        }

        public String describeUndo() {
            return "Restoring Redstone Furnace Recipe using " + input.getDisplayName();
        }

        public Object getOverrideKey() {
            return null;
        }

    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @ZenMethod
    public static void refreshRecipes() {
        MineTweakerAPI.apply(new Refresh());
    }

    private static class Refresh implements IUndoableAction {

        public void apply() {
            FurnaceManager.loadRecipes();
        }

        public boolean canUndo() {
            return false;
        }

        public String describe() {
            return "Refreshing Redstone Furnace Recipes";
        }

        public void undo() {}

        public String describeUndo() {
            return "Can't Undo Redstone Furnace Refresh";
        }

        public Object getOverrideKey() {
            return null;
        }

    }

}
