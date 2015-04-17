package modtweaker2.mods.tfcraft.handlers;

import static modtweaker2.helpers.InputHelper.toStack;

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
			ArrayList<KilnRecipe> toRemove = new ArrayList<KilnRecipe>();
			for (KilnRecipe recipe : KilnCraftingManager.getInstance().getRecipeList()){
				if (recipe.getCraftingResult() != null && recipe.getCraftingResult() == stack){
					toRemove.add(recipe);
				}
			}
			for (KilnRecipe aRecipe : toRemove){
				TFCHelper.kilnRecipes.remove(aRecipe);
			}
		}
	}
		
}
