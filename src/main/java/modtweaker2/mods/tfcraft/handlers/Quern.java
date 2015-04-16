package modtweaker2.mods.tfcraft.handlers;

import static modtweaker2.helpers.InputHelper.toStack;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import modtweaker2.mods.tfcraft.TFCHelper;
import modtweaker2.utils.BaseListAddition;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import com.bioxx.tfc.api.Crafting.QuernRecipe;

@ZenClass("mods.tfcraft.Quern")
public class Quern {

	@ZenMethod
	public static void add(IItemStack output, IItemStack input) {
		MineTweakerAPI.apply(new AddRecipe(new QuernRecipe(toStack(input), toStack(output))));
	}
	
	//@ZenMethod
	//public static void remove(IItemStack output, IItemStack input){
	//	MineTweakerAPI.apply(new RemoveRecipe(input, output));
	//}

	private static class AddRecipe extends BaseListAddition{

		public AddRecipe(QuernRecipe recipe) {
			super("Quern", TFCHelper.quernRecipes, recipe);
			TFCHelper.quernVaildItems.add(recipe.getInItem());
		}
		
	}
	
}
