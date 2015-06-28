package modtweaker2.mods.mekanism.handlers.v8;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import mekanism.api.gas.GasStack;
import mekanism.common.recipe.RecipeHandler;
import mekanism.common.recipe.RecipeHandler.Recipe;
import mekanism.common.recipe.inputs.ChemicalPairInput;
import mekanism.common.recipe.inputs.InfusionInput;
import mekanism.common.recipe.machines.ChemicalInfuserRecipe;
import mekanism.common.recipe.outputs.GasOutput;
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

@ZenClass("mods.mekanism.chemical.Infuser")
public class ChemicalInfuser {

	@ZenMethod
	public static void addRecipe(IGasStack output, IGasStack inputLeft, IGasStack inputRight) {
		MineTweakerAPI.apply(new Add(new InfuserRecipe(MekanismHelper.toGas(output), MekanismHelper.toGas(inputLeft), MekanismHelper.toGas(inputRight))));
	}

	private static class Add extends BaseMapAddition {

		public InfuserRecipe recipe;

		public Add(InfuserRecipe recipe) {
			super("Chemical Infuser", Recipe.CHEMICAL_INFUSER.get(), new ChemicalPairInput(recipe.inputLeft, recipe.inputRight), new GasOutput(recipe.output));
			this.recipe = recipe;
		}

		@Override
		public void apply() {
			RecipeHandler.addChemicalInfuserRecipe(recipe.inputLeft, recipe.inputRight, recipe.output);

		}
	}

	private static class InfuserRecipe {
		public GasStack output;
		public GasStack inputLeft;
		public GasStack inputRight;

		public InfuserRecipe(GasStack output, GasStack inputLeft, GasStack inputRight) {
			this.output = output;
			this.inputLeft = inputLeft;
			this.inputRight = inputRight;
		}

	}

	@ZenMethod
	public static void removeRecipe(IGasStack output) {
		MineTweakerAPI.apply(new Remove(MekanismHelper.toGas(output)));
	}

	private static class Remove extends BaseMapRemoval {

		public Remove(GasStack stack) {
			super("Chemical Infuser", Recipe.CHEMICAL_INFUSER.get(), stack);
		}

		@SuppressWarnings("unchecked")
		@Override
		public void apply() {
			List<ChemicalPairInput> toRemove = new ArrayList<ChemicalPairInput>();
			for (ChemicalPairInput stack : (Set<ChemicalPairInput>) Recipe.CHEMICAL_INFUSER.get().keySet()) {
				ChemicalInfuserRecipe recipe = (ChemicalInfuserRecipe) Recipe.CHEMICAL_INFUSER.get().get(stack);
				if (recipe.getOutput().output.isGasEqual((GasStack)this.stack)) {
					toRemove.add(stack);
				}
			}
			for (ChemicalPairInput stack : toRemove) {
				Recipe.CHEMICAL_INFUSER.get().remove(stack);
			}
		}

	}
}
