package modtweaker2.mods.auracascade;

import mekanism.api.gas.GasRegistry;
import mekanism.api.gas.GasStack;
import minetweaker.api.item.IIngredient;
import modtweaker2.mods.auracascade.aura.IAuraStack;
import modtweaker2.mods.mekanism.gas.IGasStack;
import pixlepix.auracascade.data.AuraQuantity;
import pixlepix.auracascade.data.EnumAura;

public class AuraCascadeHelper {
	 public static AuraQuantity toAura(IAuraStack iStack) {
	        if (iStack == null) {
	            return null;
	        } else return new AuraQuantity(EnumAura.valueOf(iStack.getName()), iStack.getAmount());
	    }

//	    public static GasStack[] toGases(IIngredient[] input) {
//	        return toGases((IGasStack[]) input);
//	    }
//
//	    public static GasStack[] toGases(IGasStack[] iStack) {
//	        GasStack[] stack = new GasStack[iStack.length];
//	        for (int i = 0; i < stack.length; i++)
//	            stack[i] = toGas(iStack[i]);
//	        return stack;
//	    }
}
