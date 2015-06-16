package modtweaker2.mods.forestry.handlers;

import static modtweaker2.helpers.InputHelper.toStack;

import java.util.List;

import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import modtweaker2.utils.BaseListAddition;
import modtweaker2.utils.BaseListRemoval;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import forestry.factory.gadgets.MachineMoistener;
import forestry.factory.gadgets.MachineMoistener.Recipe;
import forestry.factory.gadgets.MachineMoistener.RecipeManager;

@ZenClass("mods.forestry.Moistener")
public class Moistener {

	@ZenMethod
	public static void addRecipe(int timePerItem, IItemStack resource, IItemStack product) {
		MineTweakerAPI.apply(new Add(new Recipe(toStack(resource), toStack(product), timePerItem)));

	}

	private static class Add extends BaseListAddition {
		public Add(Recipe recipe) {
			super("Forestry Moistener", MachineMoistener.RecipeManager.recipes, recipe);
		}

		@Override
		public String getRecipeInfo() {
			return ((Recipe) recipe).product.getDisplayName();
		}
	}

	@ZenMethod
	public static void removeRecipe(IItemStack output) {
		MineTweakerAPI.apply(new Remove(MachineMoistener.RecipeManager.recipes, toStack(output)));
	}

	private static class Remove extends BaseListRemoval {

		public Remove(List list, ItemStack stack) {
			super(list, stack);

		}

		@Override
		public void apply() {
			for (Recipe r : RecipeManager.recipes) {
				if (r.product != null && r.product.isItemEqual(stack)) {
					recipes.add(r);
				}
			}
			super.apply();
		}
	}
}
