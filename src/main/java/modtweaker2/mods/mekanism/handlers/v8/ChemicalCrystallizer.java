package modtweaker2.mods.mekanism.handlers.v8;

import static modtweaker2.mods.mekanism.MekanismHelper.toGas;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import mekanism.api.gas.GasStack;
import mekanism.common.KeySync.KeySet;
import mekanism.common.recipe.RecipeHandler;
import mekanism.common.recipe.RecipeHandler.Recipe;
import mekanism.common.recipe.inputs.GasInput;
import mekanism.common.recipe.machines.CrystallizerRecipe;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import modtweaker2.helpers.InputHelper;
import modtweaker2.mods.mekanism.gas.IGasStack;
import modtweaker2.utils.BaseMapAddition;
import modtweaker2.utils.BaseMapRemoval;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.mekanism.chemical.Crystallizer")
public class ChemicalCrystallizer {

	@ZenMethod
	public static void addRecipe(IItemStack output, IGasStack input) {
		MineTweakerAPI.apply(new Add(new ChemicalCrystallizerRecipe(toGas(input), InputHelper.toStack(output))));
	}

	private static class Add extends BaseMapAddition {

		public Add(ChemicalCrystallizerRecipe recipe) {
			super("Chemical Crystallizer", Recipe.CHEMICAL_CRYSTALLIZER.get(), recipe.inputGas, recipe.outputItem);
		}

		@Override
		public void apply() {
			RecipeHandler.addChemicalCrystallizerRecipe((GasStack) key, (ItemStack) recipe);
		}

	}

	private static class ChemicalCrystallizerRecipe {
		public GasStack inputGas;
		public ItemStack outputItem;

		public ChemicalCrystallizerRecipe(GasStack inputGas, ItemStack outputItem) {
			this.inputGas = inputGas;
			this.outputItem = outputItem;

		}
	}

	@ZenMethod
	public static void removeRecipe(IItemStack output) {
		MineTweakerAPI.apply(new Remove(InputHelper.toStack(output)));
	}

	private static class Remove extends BaseMapRemoval {

		public Remove(ItemStack stack) {
			super("Chemical Crystallizer", Recipe.CHEMICAL_CRYSTALLIZER.get(), stack);
		}
		
		@SuppressWarnings("unchecked")
		@Override
		public void apply() {
			List<GasInput> gasesToRemove = new ArrayList<GasInput>();
			for(GasInput stack : (Set<GasInput>)Recipe.CHEMICAL_CRYSTALLIZER.get().keySet()){
				CrystallizerRecipe crystalRecipe= (CrystallizerRecipe)Recipe.CHEMICAL_CRYSTALLIZER.get().get(stack);
				if(crystalRecipe.getOutput().output.isItemEqual((ItemStack) this.stack)){
					gasesToRemove.add(stack);
				}
			}
			for(GasInput stack : gasesToRemove){
				Recipe.CHEMICAL_CRYSTALLIZER.get().remove(stack);
			}
		}

	}
}
