package modtweaker2.mods.tfcraft.handlers;

import static modtweaker2.helpers.InputHelper.*;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import minetweaker.api.liquid.ILiquidStack;
import modtweaker2.mods.tfcraft.TFCHelper;
import modtweaker2.utils.BaseListAddition;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import com.bioxx.tfc.api.Crafting.BarrelRecipe;

@ZenClass("mods.tfcraft.Barrel")
public class Barrel {
	
	@ZenMethod
	public static void add(IItemStack inputItem, ILiquidStack inputFluid, IItemStack outputItem, ILiquidStack outputFluid){
		BarrelRecipe aRecipe = new BarrelRecipe(toStack(inputItem), toFluid(inputFluid), toStack(outputItem), toFluid(outputFluid));
		MineTweakerAPI.apply(new AddRecipe(aRecipe));
	}

	private static class AddRecipe extends BaseListAddition{
		
		public AddRecipe(BarrelRecipe recipe){
			super("Barrel-Basic", TFCHelper.barrelRecipes, recipe);
		}
		
	}
}
