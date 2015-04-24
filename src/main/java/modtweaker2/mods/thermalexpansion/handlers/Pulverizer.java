package modtweaker2.mods.thermalexpansion.handlers;

import static modtweaker2.helpers.InputHelper.toStack;
import minetweaker.IUndoableAction;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import modtweaker2.utils.TweakerPlugin;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import cofh.thermalexpansion.util.crafting.PulverizerManager;
import cofh.thermalexpansion.util.crafting.PulverizerManager.RecipePulverizer;

@ZenClass("mods.thermalexpansion.Pulverizer")
public class Pulverizer {
	@ZenMethod
	public static void addRecipe(int energy, IItemStack input, IItemStack output) {
		if (!TweakerPlugin.hasInit())
			MineTweakerAPI.apply(new Add(energy, toStack(input), toStack(output), null, 0));
	}

	@ZenMethod
	public static void addRecipe(int energy, IItemStack input, IItemStack output, IItemStack secondary, int secondaryChance) {
		if (!TweakerPlugin.hasInit())
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
			applied = PulverizerManager.addRecipe(energy, input, output, secondary, secondaryChance);
		}

		public boolean canUndo() {
			return input != null && applied;
		}

		public String describe() {
			return "Adding Pulverizer Recipe using " + input.getDisplayName();
		}

		public void undo() {
			PulverizerManager.removeRecipe(input);
		}

		public String describeUndo() {
			return "Removing Pulverizer Recipe using " + input.getDisplayName();
		}

		public Object getOverrideKey() {
			return null;
		}

	}

	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@ZenMethod
	public static void removeRecipe(IItemStack input) {
		if (!TweakerPlugin.hasInit())
			MineTweakerAPI.apply(new Remove(toStack(input)));
	}

	private static class Remove implements IUndoableAction {
		ItemStack input;
		RecipePulverizer removed;

		public Remove(ItemStack inp) {
			input = inp;
		}

		public void apply() {
			removed = PulverizerManager.getRecipe(input);
			PulverizerManager.removeRecipe(input);
		}

		public boolean canUndo() {
			return removed != null;
		}

		public String describe() {
			return "Removing Pulverizer Recipe using " + input.getDisplayName();
		}

		public void undo() {
			PulverizerManager.addRecipe(removed.getEnergy(), removed.getInput(), removed.getPrimaryOutput(), removed.getSecondaryOutput(), removed.getSecondaryOutputChance());
		}

		public String describeUndo() {
			return "Restoring Pulverizer Recipe using " + input.getDisplayName();
		}

		public Object getOverrideKey() {
			return null;
		}

	}

}
