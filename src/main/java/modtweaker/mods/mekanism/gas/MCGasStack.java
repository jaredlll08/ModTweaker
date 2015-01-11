package modtweaker.mods.mekanism.gas;

import mekanism.api.gas.GasStack;

public class MCGasStack implements IGasStack {
    private final GasStack stack;

    public MCGasStack(GasStack stack) {
        this.stack = stack;
    }

    @Override
    public IGasDefinition getDefinition() {
        return new MCGasDefinition(stack.getGas());
    }

    @Override
    public String getName() {
        return stack.getGas().getName();
    }

    @Override
    public String getDisplayName() {
        return stack.getGas().getLocalizedName();
    }

    @Override
    public int getAmount() {
        return stack.amount;
    }

    @Override
    public IGasStack withAmount(int amount) {
        GasStack result = new GasStack(stack.getGas(), amount);
        return new MCGasStack(result);
    }

    @Override
    public Object getInternal() {
        return stack;
    }
}