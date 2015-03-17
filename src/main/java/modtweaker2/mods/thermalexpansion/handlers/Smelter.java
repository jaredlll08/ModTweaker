package modtweaker2.mods.thermalexpansion.handlers;

import static modtweaker2.helpers.InputHelper.toStack;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import modtweaker2.mods.thermalexpansion.ThermalHelper;
import modtweaker2.utils.BaseDescriptionAddition;
import modtweaker2.utils.BaseDescriptionRemoval;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import cofh.lib.inventory.ComparableItemStackSafe;
import cofh.thermalexpansion.util.crafting.SmelterManager.RecipeSmelter;

@ZenClass("mods.thermalexpansion.Smelter")
public class Smelter {
	private static boolean removeValidated(ComparableItemStackSafe stack) {
		for (Map.Entry entry : ThermalHelper.getSmelterMap().entrySet()) {
			if (entry != null && entry.getValue() != null) {
				RecipeSmelter recipe = (RecipeSmelter) entry.getValue();
				if (recipe != null) {
					if (stack.equals(new ComparableItemStackSafe(recipe.getPrimaryInput()))) {
						return false;
					} else if (stack.equals(new ComparableItemStackSafe(recipe.getSecondaryInput()))) {
						return false;
					}
				}
			}
		}
		return ThermalHelper.smelterValid.remove(stack);
	}

	@ZenMethod
	public static void addRecipe(int energy, IItemStack input, IItemStack input2, IItemStack output) {
		addRecipe(energy, input, input2, output, null, 0);
	}

	@ZenMethod
	public static void addRecipe(int energy, IItemStack input, IItemStack input2, IItemStack output, IItemStack output2, int chance) {
		ItemStack in1 = toStack(input);
		ItemStack in2 = toStack(input2);
		ItemStack out1 = toStack(output);
		ItemStack out2 = toStack(output2);
		RecipeSmelter recipe = (RecipeSmelter) ThermalHelper.getTERecipe(ThermalHelper.smelterRecipe, in1, in2, out1, out2, chance, energy);
		MineTweakerAPI.apply(new Add(in1, in2, recipe));
	}

	private static class Add extends BaseDescriptionAddition {
		private final ComparableItemStackSafe input1;
		private final ComparableItemStackSafe input2;
		private final List key;
		private final RecipeSmelter recipe;

		public Add(ItemStack input1, ItemStack input2, RecipeSmelter recipe) {
			super("Induction Smelter");
			this.input1 = new ComparableItemStackSafe(input1);
			this.input2 = new ComparableItemStackSafe(input2);
			this.key = (Arrays.asList(new ComparableItemStackSafe[] { this.input1, this.input2 }));
			this.recipe = recipe;
		}

		@Override
		public void apply() {
			ThermalHelper.getSmelterMap().put(key, recipe);
			ThermalHelper.smelterValid.add(input1);
			ThermalHelper.smelterValid.add(input2);
		}

		@Override
		public boolean canUndo() {
			return ThermalHelper.getSmelterMap() != null;
		}

		@Override
		public void undo() {
			ThermalHelper.getSmelterMap().remove(key);
			removeValidated(input1);
			removeValidated(input2);
		}

		@Override
		public String getRecipeInfo() {
			return ((RecipeSmelter) recipe).getPrimaryOutput().getDisplayName();
		}
	}

	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@ZenMethod
	public static void removeRecipe(IItemStack input, IItemStack input2) {
		MineTweakerAPI.apply(new Remove(toStack(input), toStack(input2)));
	}

	private static class Remove extends BaseDescriptionRemoval {
		private final ComparableItemStackSafe input1;
		private final ComparableItemStackSafe input2;
		private final List key;
		private RecipeSmelter recipe;

		public Remove(ItemStack input1, ItemStack input2) {
			super("Induction Smelter");
			this.input1 = new ComparableItemStackSafe(input1);
			this.input2 = new ComparableItemStackSafe(input2);
			this.key = (Arrays.asList(new ComparableItemStackSafe[] { this.input1, this.input2 }));
		}

		@Override
		public void apply() {
			recipe = ThermalHelper.getSmelterMap().get(key);
			ThermalHelper.getSmelterMap().remove(key);
			removeValidated(input1);
			removeValidated(input2);
		}

		@Override
		public boolean canUndo() {
			return ThermalHelper.getSmelterMap() != null;
		}

		@Override
		public void undo() {
			ThermalHelper.getSmelterMap().put(key, recipe);
			ThermalHelper.smelterValid.add(input1);
			ThermalHelper.smelterValid.add(input2);
		}

		@Override
		public String getRecipeInfo() {
			return input1.toItemStack().getDisplayName() + " + " + input2.toItemStack().getDisplayName();
		}
	}
}
