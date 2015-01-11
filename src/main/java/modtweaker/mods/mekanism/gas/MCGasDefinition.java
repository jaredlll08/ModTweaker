package modtweaker.mods.mekanism.gas;

import mekanism.api.gas.Gas;
import mekanism.api.gas.GasStack;

public class MCGasDefinition implements IGasDefinition {
    private final Gas gas;

    public MCGasDefinition(Gas gas) {
        this.gas = gas;
    }

    @Override
    public String getName() {
        return gas.getName();
    }

    @Override
    public String getDisplayName() {
        return gas.getLocalizedName();
    }

    @Override
    public IGasStack asStack(int millibuckets) {
        return new MCGasStack(new GasStack(gas, millibuckets));
    }
}