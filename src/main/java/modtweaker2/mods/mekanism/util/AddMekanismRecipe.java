package modtweaker2.mods.mekanism.util;

import java.util.Map;

import mekanism.api.ChanceOutput;
import mekanism.api.ChemicalPair;
import mekanism.api.PressurizedReactants;
import mekanism.api.gas.GasStack;
import mekanism.api.infuse.InfusionOutput;
import modtweaker2.mods.mekanism.Mekanism;
import modtweaker2.utils.BaseMapAddition;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class AddMekanismRecipe extends BaseMapAddition {
    public AddMekanismRecipe(String str, Map map, Object key, Object recipe) {
        super(str.toLowerCase(), map, key, recipe);
    }

    @Override
    public String describe() {
        if (Mekanism.v7)
        {
            if (recipe instanceof ItemStack)
                return "Adding " + description + " Recipe for : " + ((ItemStack)recipe).getDisplayName();
            else if (recipe instanceof FluidStack)
                return "Adding " + description + " Recipe for : " + ((FluidStack)recipe).getFluid().getLocalizedName();
            else if (recipe instanceof ChemicalPair)
                return "Adding " + description + " Recipe for : " + ((ChemicalPair)recipe).leftGas.getGas().getLocalizedName();
            else if (recipe instanceof ChanceOutput)
                return "Adding " + description + " Recipe for : " + ((ChanceOutput)recipe).primaryOutput.getDisplayName();
            else if (recipe instanceof GasStack)
                return "Adding " + description + " Recipe for : " + ((GasStack)recipe).getGas().getLocalizedName();
            else if (recipe instanceof PressurizedReactants)
                return "Adding " + description + " Recipe for : " + ((PressurizedReactants)recipe).getSolid().getDisplayName();
            else if (recipe instanceof InfusionOutput)
                return "Adding " + description + " Recipe for : " + ((InfusionOutput)recipe).resource.getDisplayName();
            else return super.getRecipeInfo();
        }
        return "";
    }

    @Override
    public String describeUndo() {
        if (Mekanism.isV7())
        {
            if (recipe instanceof ItemStack)
                return "Removing " + description + " Recipe for : " + ((ItemStack)recipe).getDisplayName();
            else if (recipe instanceof FluidStack)
                return "Removing " + description + " Recipe for : " + ((FluidStack)recipe).getFluid().getLocalizedName();
            else if (recipe instanceof ChemicalPair)
                return "Removing " + description + " Recipe for : " + ((ChemicalPair)recipe).leftGas.getGas().getLocalizedName();
            else if (recipe instanceof ChanceOutput)
                return "Removing " + description + " Recipe for : " + ((ChanceOutput)recipe).primaryOutput.getDisplayName();
            else if (recipe instanceof GasStack)
                return "Removing " + description + " Recipe for : " + ((GasStack)recipe).getGas().getLocalizedName();
            else if (recipe instanceof PressurizedReactants)
                return "Removing " + description + " Recipe for : " + ((PressurizedReactants)recipe).getSolid().getDisplayName();
            else if (recipe instanceof InfusionOutput)
                return "Removing " + description + " Recipe for : " + ((InfusionOutput)recipe).resource.getDisplayName();
            else return super.getRecipeInfo();
        }
        return "";
    }
}
