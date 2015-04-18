package modtweaker2.mods.tfcraft.handlers;

import static modtweaker2.helpers.InputHelper.toStack;
import static modtweaker2.helpers.StackHelper.areEqual;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import modtweaker2.mods.tfcraft.TFCHelper;
import modtweaker2.utils.BaseListAddition;
import modtweaker2.utils.BaseListRemoval;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import com.bioxx.tfc.api.Crafting.QuernManager;
import com.bioxx.tfc.api.Crafting.QuernRecipe;

@ZenClass("mods.tfcraft.Quern")
public class Quern {

	@ZenMethod
	public static void add(IItemStack input, IItemStack output) {
		MineTweakerAPI.apply(new AddRecipe(new QuernRecipe(toStack(input), toStack(output))));
	}
	
	@ZenMethod
	public static void remove(IItemStack input){
		MineTweakerAPI.apply(new RemoveRecipe(toStack(input)));
	}

	private static class AddRecipe extends BaseListAddition{
		public AddRecipe(QuernRecipe recipe) {
			super("Quern", TFCHelper.quernRecipes, recipe);
			TFCHelper.quernVaildItems.add(recipe.getInItem());
		}
	}
	
	private static class RemoveRecipe extends BaseListRemoval{
		public RemoveRecipe(ItemStack stack) {
			super("Quern-Remove", TFCHelper.quernRecipes, stack);
		}

		@Override
		public void apply() {
			ArrayList<QuernRecipe> toRemove = new ArrayList<QuernRecipe>();
			for (QuernRecipe recipe : QuernManager.getInstance().getRecipes()){
				if (recipe.getResult() !=null && areEqual(recipe.getResult(), stack)){
					toRemove.add(recipe);
				}
			}
			for (QuernRecipe aRecipe : toRemove){
				TFCHelper.quernRecipes.remove(aRecipe);
				TFCHelper.quernVaildItems.remove(aRecipe.getInItem());
			}
		}
	}
	
}
