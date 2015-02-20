package modtweaker2.mods.forestry.handlers;

import java.util.List;

import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import modtweaker2.util.BaseListAddition;
import modtweaker2.util.BaseListRemoval;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import forestry.factory.gadgets.MachineMoistener;
import forestry.factory.gadgets.MachineMoistener.Recipe;
import forestry.factory.gadgets.MachineMoistener.RecipeManager;
import static modtweaker2.helpers.InputHelper.*;
import static modtweaker2.helpers.StackHelper.*;

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
					recipe = r;
					break;
				}
			}
			RecipeManager.recipes.remove(recipe);

		}

	}
}
