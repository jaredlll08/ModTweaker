package modtweaker2.helpers;

import static modtweaker2.helpers.InputHelper.toStack;
import mekanism.api.gas.GasStack;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import minetweaker.api.item.IngredientAny;
import minetweaker.api.liquid.ILiquidStack;
import modtweaker2.mods.botania.Botania;
import modtweaker2.mods.mekanism.MekanismHelper;
import modtweaker2.mods.mekanism.gas.IGasStack;
import modtweaker2.utils.TweakerPlugin;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class StackHelper {
    /**
     * Compares two ItemStack instances, trying to match the referenced item.
     */
	public static boolean areEqual(ItemStack stack1, ItemStack stack2) {
		if (stack1 == null || stack2 == null) {
			return false;
		} else if (TweakerPlugin.isLoaded("Botania") && Botania.isSubtile(stack1) && Botania.isSubtile(stack2)) {
			return Botania.subtileMatches(stack1, stack2);
		} else {
			return stack1.isItemEqual(stack2);
		}
	}
	
	public static boolean areEqual(FluidStack stack1, FluidStack stack2) {
	    if(stack1 == null || stack2 == null) {
	        return false;
	    }
	    
	    return stack1.isFluidEqual(stack2);
	}
	
	/**
	 * Compares two ItemStack instances if they reference the same object or
	 * are both null. If false, the method areEqual() is called to check if the
	 * two instances reference the same item.
	 */
    public static boolean areEqualOrNull(ItemStack stack1, ItemStack stack2) {
        // Check if they reference the same object or are are both null
        if(stack1 == stack2) {
            return true;
        }

        return(areEqual(stack1, stack2));
    }
    
    public static boolean areEqualOrNull(FluidStack stack1, FluidStack stack2) {
        // Check if they reference the same object or are are both null
        if(stack1 == stack2) {
            return true;
        }
        
        return(areEqual(stack1, stack2));
    }
    
    /**
     * Adds extra check to IIngredient matches() for Botania special flowers 
     */
    public static boolean matches(IIngredient ingredient, IItemStack itemStack) {
        if(ingredient == null) {
            return false;
        }
        
        if(!ingredient.matches(itemStack)) {
            return false;
        }
        
        // Check for Botania special flowers
        if(ingredient.getItems() != null && Botania.isSubtile(toStack(itemStack))) {
            for(IItemStack item : ingredient.getItems()) {
                if(areEqual(toStack(item), toStack(itemStack))) {
                    return true;
                }
            }
        } else {
            return true;
        }
        
        return false;
    }
    
    /**
     * This function compares an ingredient with a fluid. MCLiquidStack.matches() function
     * is currently broken in MineTweaker3, thus this function is only a workaround.
     */
    public static boolean matches(IIngredient ingredient, ILiquidStack liquidStack) {
        if(ingredient == null) {
            return false;
        }

        // Do we have a wildcard (<*>) ?
        if(ingredient.matches(liquidStack)) {
            return true;
        }
        
        // Does ingredient reference liquids?
        if(ingredient.getLiquids() != null) {
            for (ILiquidStack liquid : ingredient.getLiquids()) {
                if(InputHelper.toFluid(liquid).isFluidEqual(InputHelper.toFluid(liquidStack))) {
                    return true;
                }
            }
        }
        
        return false;
    }
    
    public static boolean matches(IIngredient ingredient, IGasStack gasStack) {
        if(ingredient == null) {
            return false;
        }
        
        if(ingredient == IngredientAny.INSTANCE) {
            return true;
        }
        
        if(ingredient instanceof IGasStack) {
            return MekanismHelper.toGas((IGasStack)ingredient).isGasEqual(MekanismHelper.toGas(gasStack));
        }

        return false;
    }
    
}
