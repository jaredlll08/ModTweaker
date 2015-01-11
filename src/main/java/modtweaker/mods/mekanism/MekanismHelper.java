package modtweaker.mods.mekanism;

import mekanism.api.gas.GasRegistry;
import mekanism.api.gas.GasStack;
import minetweaker.api.item.IIngredient;
import modtweaker.mods.mekanism.gas.IGasStack;

public class MekanismHelper {
    private MekanismHelper() {}

    public static GasStack toGas(IGasStack iStack) {
        if (iStack == null) {
            return null;
        } else return new GasStack(GasRegistry.getGas(iStack.getName()), iStack.getAmount());
    }

    public static GasStack[] toGases(IIngredient[] input) {
        return toGases((IGasStack[]) input);
    }

    public static GasStack[] toGases(IGasStack[] iStack) {
        GasStack[] stack = new GasStack[iStack.length];
        for (int i = 0; i < stack.length; i++)
            stack[i] = toGas(iStack[i]);
        return stack;
    }
}
