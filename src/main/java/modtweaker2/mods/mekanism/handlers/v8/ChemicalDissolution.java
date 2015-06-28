package modtweaker2.mods.mekanism.handlers.v8;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import mekanism.api.gas.GasStack;
import mekanism.common.recipe.RecipeHandler;
import mekanism.common.recipe.RecipeHandler.Recipe;
import mekanism.common.recipe.inputs.ItemStackInput;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import modtweaker2.helpers.InputHelper;
import modtweaker2.mods.mekanism.MekanismHelper;
import modtweaker2.mods.mekanism.gas.IGasStack;
import modtweaker2.utils.BaseMapAddition;
import modtweaker2.utils.BaseMapRemoval;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.mekanism.chemical.Dissolution")
public class ChemicalDissolution {

	@ZenMethod
	public static void addRecipe(IGasStack output, IItemStack input) {
		MineTweakerAPI.apply(new Add(new DissolutionRecipe(MekanismHelper.toGas(output), InputHelper.toStack(input))));
	}

	private static class Add extends BaseMapAddition {

		public DissolutionRecipe recipe;

		public Add(DissolutionRecipe recipe) {
			super("Dissolution Infuser", Recipe.CHEMICAL_DISSOLUTION_CHAMBER.get(), recipe.input, recipe.output);
			this.recipe = recipe;
		}

		@Override
		public void apply() {
			RecipeHandler.addChemicalDissolutionChamberRecipe(recipe.input, recipe.output);

		}
	}

	private static class DissolutionRecipe {
		public GasStack output;
		public ItemStack input;

		@SuppressWarnings("unused")
		public DissolutionRecipe(GasStack output, ItemStack input) {
			this.output = output;
			this.input = input;
		}

	}

	@ZenMethod
	public static void removeRecipe(IGasStack output) {
		MineTweakerAPI.apply(new Remove(MekanismHelper.toGas(output)));
	}

	private static class Remove extends BaseMapRemoval {

		public Remove(GasStack stack) {
			super("Dissolution Infuser", Recipe.CHEMICAL_DISSOLUTION_CHAMBER.get(), stack);
		}

		@SuppressWarnings("unchecked")
		@Override
		public void apply() {
			List<ItemStackInput> toRemove = new ArrayList<ItemStackInput>();
			for (ItemStackInput stack : (Set<ItemStackInput>) Recipe.CHEMICAL_DISSOLUTION_CHAMBER.get().keySet()) {
				mekanism.common.recipe.machines.DissolutionRecipe recipe = (mekanism.common.recipe.machines.DissolutionRecipe) Recipe.CHEMICAL_DISSOLUTION_CHAMBER.get().get(stack);
				if (recipe.getOutput().output.isGasEqual((GasStack) this.stack)) {
					toRemove.add(stack);
				}
			}
			for (ItemStackInput stack : toRemove) {
				Recipe.CHEMICAL_DISSOLUTION_CHAMBER.get().remove(stack);
			}
		}

	}
}
