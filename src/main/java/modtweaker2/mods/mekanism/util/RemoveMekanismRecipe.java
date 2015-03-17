package modtweaker2.mods.mekanism.util;

import static modtweaker2.helpers.StackHelper.areEqual;

import java.util.Iterator;
import java.util.Map;

import mekanism.api.ChanceOutput;
import mekanism.api.ChemicalPair;
import mekanism.api.PressurizedProducts;
import mekanism.api.PressurizedReactants;
import mekanism.api.PressurizedRecipe;
import mekanism.api.gas.GasStack;
import mekanism.api.infuse.InfusionOutput;
import modtweaker2.utils.BaseMapRemoval;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class RemoveMekanismRecipe extends BaseMapRemoval {
	private Object tmp;

	public RemoveMekanismRecipe(String string, Map map, Object key) {
		super(string.toLowerCase().replace('_', ' '), map, key, null);
		this.tmp = key;
	}

	@Override
	public void apply() {
		try {
			Iterator it = map.entrySet().iterator();

			while (it.hasNext()) {
				Map.Entry pairs = (Map.Entry) it.next();
				if (pairs != null) {
					Object value = pairs.getValue();
					if (value != null && tmp != null) {
						if (tmp instanceof ItemStack && value instanceof ItemStack) {
							if (areEqual(((ItemStack) tmp), (ItemStack) value)) {
								key = pairs.getKey();
								break;
							}
						}

						if (tmp instanceof FluidStack && value instanceof FluidStack) {
							if (((FluidStack) tmp).isFluidEqual((FluidStack) value)) {
								key = pairs.getKey();
								break;
							}
						}

						if (tmp instanceof GasStack && value instanceof GasStack) {
							if (((GasStack) tmp).isGasEqual((GasStack) value)) {
								key = pairs.getKey();
								break;
							}
						}

						if (tmp instanceof ItemStack && value instanceof InfusionOutput) {
							if (areEqual(((ItemStack) tmp), ((InfusionOutput) value).resource)) {
								key = pairs.getKey();
								break;
							}
						}

						if (tmp instanceof ChemicalPair && value instanceof ChemicalPair) {
							ChemicalPair par1 = (ChemicalPair) tmp;
							ChemicalPair par2 = (ChemicalPair) value;
							if (par1.leftGas.isGasEqual(par2.leftGas) && par1.rightGas.isGasEqual(par2.rightGas)) {
								key = pairs.getKey();
								break;
							}
						}

						if (tmp instanceof ChanceOutput && value instanceof ChanceOutput) {
							ChanceOutput par1 = (ChanceOutput) tmp;
							ChanceOutput par2 = (ChanceOutput) value;
							if (areEqual(par1.primaryOutput, par2.primaryOutput)) {
								if (par1.secondaryOutput == null || (par1.secondaryOutput != null && par2.secondaryOutput != null && areEqual(par1.secondaryOutput, par2.secondaryOutput))) {
									key = pairs.getKey();
									break;
								}
							}
						}

						if (tmp instanceof PressurizedProducts && value instanceof PressurizedRecipe) {
							PressurizedProducts par1 = (PressurizedProducts) tmp;
							PressurizedProducts par2 = ((PressurizedRecipe) value).products;
							if (areEqual(par1.getItemOutput(), (par2.getItemOutput()))) {
								if (par1.getGasOutput().isGasEqual(par2.getGasOutput())) {
									key = pairs.getKey();
									break;

								}
							}
						}
					}

					it.remove();
				}
			}

			recipe = map.get(key);

		} catch (Exception e) {
			e.printStackTrace();
		}

		map.remove(key);
	}

	@Override
	public String describe() {
		if (tmp instanceof ItemStack)
			return "Removing " + description + " Recipe for : " + ((ItemStack) tmp).getDisplayName();
		else if (tmp instanceof FluidStack)
			return "Removing " + description + " Recipe for : " + ((FluidStack) tmp).getFluid().getLocalizedName();
		else if (tmp instanceof ChemicalPair)
			return "Removing " + description + " Recipe for : " + ((ChemicalPair) tmp).leftGas.getGas().getLocalizedName();
		else if (tmp instanceof ChanceOutput)
			return "Removing " + description + " Recipe for : " + ((ChanceOutput) tmp).primaryOutput.getDisplayName();
		else if (tmp instanceof GasStack)
			return "Removing " + description + " Recipe for : " + ((GasStack) tmp).getGas().getLocalizedName();
		else if (tmp instanceof PressurizedReactants)
			return "Removing " + description + " Recipe for : " + ((PressurizedReactants) tmp).getSolid().getDisplayName();
		else if (tmp instanceof InfusionOutput)
			return "Removing " + description + " Recipe for : " + ((InfusionOutput) tmp).resource.getDisplayName();
		else
			return super.getRecipeInfo();
	}

	@Override
	public String describeUndo() {
		if (tmp instanceof ItemStack)
			return "Restoring " + description + " Recipe for : " + ((ItemStack) tmp).getDisplayName();
		else if (tmp instanceof FluidStack)
			return "Restoring " + description + " Recipe for : " + ((FluidStack) tmp).getFluid().getLocalizedName();
		else if (tmp instanceof ChemicalPair)
			return "Restoring " + description + " Recipe for : " + ((ChemicalPair) tmp).leftGas.getGas().getLocalizedName();
		else if (tmp instanceof ChanceOutput)
			return "Restoring " + description + " Recipe for : " + ((ChanceOutput) tmp).primaryOutput.getDisplayName();
		else if (tmp instanceof GasStack)
			return "Restoring " + description + " Recipe for : " + ((GasStack) tmp).getGas().getLocalizedName();
		else if (tmp instanceof PressurizedReactants)
			return "Restoring " + description + " Recipe for : " + ((PressurizedReactants) tmp).getSolid().getDisplayName();
		else if (tmp instanceof InfusionOutput)
			return "Restoring " + description + " Recipe for : " + ((InfusionOutput) tmp).resource.getDisplayName();
		else
			return super.getRecipeInfo();
	}
}
