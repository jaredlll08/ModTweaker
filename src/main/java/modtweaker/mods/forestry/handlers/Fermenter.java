package modtweaker.mods.forestry.handlers;

import static modtweaker.helpers.InputHelper.getFluid;
import static modtweaker.helpers.InputHelper.toFluid;
import static modtweaker.helpers.InputHelper.toStack;

import java.util.ArrayList;
import java.util.List;

import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import minetweaker.api.liquid.ILiquidStack;
import modtweaker.util.BaseListAddition;
import modtweaker.util.BaseListRemoval;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import forestry.factory.gadgets.MachineFermenter;
import forestry.factory.gadgets.MachineFermenter.Recipe;
import forestry.factory.gadgets.MachineFermenter.RecipeManager;

@ZenClass("mods.forestry.Fermenter")
public class Fermenter {

	@ZenMethod
	public static void addRecipe(IItemStack resource, ILiquidStack liquid, int fermentationValue, float modifier, ILiquidStack output) {
		MineTweakerAPI.apply(new Add(new Recipe(toStack(resource), fermentationValue, modifier, toFluid(output), toFluid(liquid))));
		MachineFermenter.RecipeManager.recipeFluidInputs.add(getFluid(liquid));
		MachineFermenter.RecipeManager.recipeFluidOutputs.add(getFluid(output));
	}

	private static class Add extends BaseListAddition {
		public Add(Recipe recipe) {
			super("Forestry Fermenter", MachineFermenter.RecipeManager.recipes, recipe);
		}

		@Override
		public String getRecipeInfo() {
			return ((Recipe) recipe).output.getLocalizedName();
		}
	}

	@ZenMethod
	public static void removeRecipe(IItemStack input) {
		MineTweakerAPI.apply(new Remove(MachineFermenter.RecipeManager.recipes, toStack(input)));
	}

	private static class Remove extends BaseListRemoval {

		public Remove(List list, ItemStack stack) {
			super(list, stack);

		}

		@Override
		public void apply() {
			ArrayList<Recipe> recipes = RecipeManager.recipes;
			for (Recipe r : recipes) {
				if (r.resource != null && r.resource.isItemEqual(stack)) {
					recipe = r;
					RecipeManager.recipes.remove(recipe);
				}
			}

		}

	}
}
