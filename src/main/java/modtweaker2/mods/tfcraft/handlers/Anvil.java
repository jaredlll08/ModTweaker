package modtweaker2.mods.tfcraft.handlers;

import static modtweaker2.helpers.InputHelper.toStack;

import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import modtweaker2.mods.tfcraft.TFCHelper;
import modtweaker2.utils.BaseListAddition;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import com.bioxx.tfc.api.Crafting.AnvilRecipe;

@ZenClass("mods.tfcraft.Anvil")
public class Anvil {
	
	@ZenMethod
	public static void add(IItemStack output, IItemStack input1, IItemStack input2, String plan, int value, boolean flux, int req){
		MineTweakerAPI.apply(new Add(new AnvilRecipe(toStack(input1), toStack(input2), plan, value, flux, req, toStack(output))));
	}
	
	/**
	 * For weld recipe, flux == true, definitely.
	 */
	@ZenMethod
	public static void addWeld(IItemStack output, IItemStack input1, IItemStack input2, String plan, int value, int req){
		MineTweakerAPI.apply(new AddWeld(new AnvilRecipe(toStack(input1), toStack(input2), plan, value, true, req, toStack(output))));
	}
	
	private static class Add extends BaseListAddition{

		public Add(AnvilRecipe recipe) {
			super("Anvil-Regular", TFCHelper.anvilRecipes, recipe);
		}
	}
	
	private static class AddWeld extends BaseListAddition{

		public AddWeld(AnvilRecipe recipe) {
			super("Anvil-Weld", TFCHelper.anvilWeldRecipes, recipe);
		}
		
	}

}
