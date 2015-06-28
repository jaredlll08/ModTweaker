package modtweaker2.mods.mekanism.handlers.v8;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import mekanism.api.gas.GasStack;
import mekanism.common.recipe.RecipeHandler;
import mekanism.common.recipe.RecipeHandler.Recipe;
import mekanism.common.recipe.inputs.AdvancedMachineInput;
import mekanism.common.recipe.outputs.ItemStackOutput;
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

@ZenClass("mods.mekanism.chemical.Injection")
public class ChemicalInjection {

	@ZenMethod
	public static void addRecipe(IItemStack output, IItemStack input, IGasStack gas) {
		MineTweakerAPI.apply(new Add(new InjectionRecipe(InputHelper.toStack(output), InputHelper.toStack(input), MekanismHelper.toGas(gas))));
	}

	private static class Add extends BaseMapAddition {

		public InjectionRecipe recipe;

		public Add(InjectionRecipe recipe) {
			super("Chemical Injection", Recipe.CHEMICAL_INJECTION_CHAMBER.get(), recipe.input, recipe.output);
			this.recipe = recipe;
		}

		@Override
		public void apply() {
			RecipeHandler.addChemicalInjectionChamberRecipe(recipe.input, recipe.gas.getGas().getLocalizedName(), recipe.output);
		}
	}

	private static class InjectionRecipe {
		public ItemStack output;
		public ItemStack input;
		public GasStack gas;

		public InjectionRecipe(ItemStack output, ItemStack input, GasStack gas) {
			this.output = output;
			this.input = input;
			this.gas = gas;
		}

	}

	@ZenMethod
	public static void removeRecipe(IItemStack output) {
		MineTweakerAPI.apply(new Remove(InputHelper.toStack(output)));
	}

	private static class Remove extends BaseMapRemoval {

		public Remove(ItemStack output) {
			super("Chemical Injection", Recipe.CHEMICAL_DISSOLUTION_CHAMBER.get(), output);
		}

		@SuppressWarnings("unchecked")
		@Override
		public void apply() {
			List<AdvancedMachineInput> toRemove = new ArrayList<AdvancedMachineInput>();
			for (AdvancedMachineInput stack : (Set<AdvancedMachineInput>) Recipe.CHEMICAL_DISSOLUTION_CHAMBER.get().keySet()) {
				ItemStackOutput recipe = (ItemStackOutput) Recipe.CHEMICAL_DISSOLUTION_CHAMBER.get().get(stack);
				if (recipe.output.isItemEqual((ItemStack)this.stack)) {
					toRemove.add(stack);
				}
			}
			for (AdvancedMachineInput stack : toRemove) {
				Recipe.CHEMICAL_DISSOLUTION_CHAMBER.get().remove(stack);
			}
		}

	}
}
