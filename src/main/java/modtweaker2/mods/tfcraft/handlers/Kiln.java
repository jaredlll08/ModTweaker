package modtweaker2.mods.tfcraft.handlers;

import com.bioxx.tfc.api.Crafting.KilnRecipe;

import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import modtweaker2.mods.tfcraft.TFCHelper;
import modtweaker2.utils.BaseListAddition;
import static modtweaker2.helpers.InputHelper.toStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.tfcraft.Kiln")
public class Kiln {

	@ZenMethod
	public static void addRecipe(IItemStack input, int lv, IItemStack output){
		MineTweakerAPI.apply(new AddRecipe(new KilnRecipe(toStack(input), lv, toStack(output))));
	}
	
	private static class AddRecipe extends BaseListAddition{
		
		public AddRecipe(KilnRecipe recipe) {
			super("Kiln", TFCHelper.kilnRecipes, recipe);
		}
	}
		
}
