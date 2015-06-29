//package modtweaker2.mods.mekanism.util;
//
//import java.util.Map;
//
//import mekanism.api.gas.GasStack;
//import mekanism.common.recipe.inputs.ChemicalPairInput;
//import mekanism.common.recipe.inputs.InfusionInput;
//import mekanism.common.recipe.machines.PressurizedRecipe;
//import mekanism.common.recipe.outputs.ChanceOutput;
//import mekanism.common.recipe.outputs.ChemicalPairOutput;
//import mekanism.common.recipe.outputs.PressurizedOutput;
//import modtweaker2.mods.mekanism.Mekanism;
//import modtweaker2.utils.BaseMapAddition;
//import net.minecraft.item.ItemStack;
//import net.minecraftforge.fluids.FluidStack;
//
//public class AddMekanismRecipe extends BaseMapAddition {
//	public AddMekanismRecipe(String str, Map map, Object key, Object recipe) {
//		super(str.toLowerCase(), map, key, recipe);
//	}
//
//	@Override
//	public String describe() {
//		if (Mekanism.v7) {
//			if (recipe instanceof ItemStack)
//				return "Adding " + description + " Recipe for : " + ((ItemStack) recipe).getDisplayName();
//			else if (recipe instanceof FluidStack)
//				return "Adding " + description + " Recipe for : " + ((FluidStack) recipe).getFluid().getLocalizedName();
//			else if (recipe instanceof ChemicalPairInput)
//				return "Adding " + description + " Recipe for : " + ((ChemicalPairInput) recipe).leftGas.getGas().getLocalizedName();
//			else if (recipe instanceof ChanceOutput)
//				return "Adding " + description + " Recipe for : " + ((ChanceOutput) recipe).primaryOutput.getDisplayName();
//			else if (recipe instanceof GasStack)
//				return "Adding " + description + " Recipe for : " + ((GasStack) recipe).getGas().getLocalizedName();
//			else if (recipe instanceof PressurizedRecipe)
//				return "Adding " + description + " Recipe for : " + ((PressurizedRecipe) recipe).getOutput().getItemOutput().getDisplayName();
//			else if (recipe instanceof InfusionInput)
//				return "Adding " + description + " Recipe for : " + ((InfusionInput) recipe).inputStack.getDisplayName();
//			else
//				return super.getRecipeInfo();
//		}
//		return "";
//	}
//
//	@Override
//	public String describeUndo() {
//		if (Mekanism.isV7()) {
//			if (recipe instanceof ItemStack)
//				return "Removing " + description + " Recipe for : " + ((ItemStack) recipe).getDisplayName();
//			else if (recipe instanceof FluidStack)
//				return "Removing " + description + " Recipe for : " + ((FluidStack) recipe).getFluid().getLocalizedName();
//			else if (recipe instanceof ChemicalPairOutput)
//				return "Removing " + description + " Recipe for : " + ((ChemicalPairOutput) recipe).leftGas.getGas().getLocalizedName();
//			else if (recipe instanceof ChanceOutput)
//				return "Removing " + description + " Recipe for : " + ((ChanceOutput) recipe).primaryOutput.getDisplayName();
//			else if (recipe instanceof GasStack)
//				return "Removing " + description + " Recipe for : " + ((GasStack) recipe).getGas().getLocalizedName();
//			else if (recipe instanceof PressurizedOutput)
//				return "Removing " + description + " Recipe for : " + ((PressurizedOutput) recipe).getItemOutput().getDisplayName();
//			else if (recipe instanceof InfusionInput)
//				return "Removing " + description + " Recipe for : " + ((InfusionInput) recipe).inputStack.getDisplayName();
//			else
//				return super.getRecipeInfo();
//		}
//		return "";
//	}
//}
