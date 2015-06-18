package modtweaker2.mods.tfcraft.handlers;

import static modtweaker2.helpers.InputHelper.toStack;
import static modtweaker2.helpers.StackHelper.areEqual;

import java.util.ArrayList;

import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import modtweaker2.mods.tfcraft.TFCHelper;
import modtweaker2.utils.BaseListAddition;
import modtweaker2.utils.BaseListRemoval;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import com.bioxx.tfc.api.Crafting.KilnCraftingManager;
import com.bioxx.tfc.api.Crafting.KilnRecipe;

@ZenClass("mods.tfcraft.Kiln")
public class Kiln {

	/** Deprecated, since pottery kiln will check <code>x instanceof ItemPotteryBase</code>
	 *  <p>If you have a such item, you might consider to use it
	 */
	@Deprecated
	@ZenMethod
	public static void add(IItemStack input, int lv, IItemStack output){
		MineTweakerAPI.apply(new AddRecipe(new KilnRecipe(toStack(input), lv, toStack(output))));
	}

	@ZenMethod
	public static void remove(IItemStack stack){
		MineTweakerAPI.apply(new RemoveRecipe(toStack(stack)));
	}
	
	private static class AddRecipe extends BaseListAddition{	
		public AddRecipe(KilnRecipe recipe) {
			super("Kiln", TFCHelper.kilnRecipes, recipe);
		}
	}
	
	private static class RemoveRecipe extends BaseListRemoval{
		public RemoveRecipe(ItemStack out){
			super("Kiln-Remove", TFCHelper.kilnRecipes, out);
		}

        @Override
		public void apply() {
			for (KilnRecipe recipe : KilnCraftingManager.getInstance().getRecipeList()){
				if (recipe.getCraftingResult() != null && areEqual(recipe.getCraftingResult(), stack)){
					recipes.add(recipe);
				}
			}
			super.apply();
		}
	}
		
}
