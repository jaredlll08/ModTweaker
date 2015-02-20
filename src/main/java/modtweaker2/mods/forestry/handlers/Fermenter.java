package modtweaker2.mods.forestry.handlers;

import java.util.ArrayList;
import java.util.List;

import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import minetweaker.api.liquid.ILiquidStack;
import modtweaker2.util.BaseListAddition;
import modtweaker2.util.BaseListRemoval;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import forestry.factory.gadgets.MachineFermenter;
import forestry.factory.gadgets.MachineFermenter.Recipe;
import forestry.factory.gadgets.MachineFermenter.RecipeManager;
import static modtweaker2.helpers.InputHelper.*;
import static modtweaker2.helpers.StackHelper.*;

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
			ArrayList<Recipe> recipes = new ArrayList<Recipe>();
			for (Recipe r : RecipeManager.recipes) {
				if (r != null && r.resource != null && r.resource.isItemEqual(stack)) {
					recipes.add(r);

				}
			}
			for (Recipe r : recipes) {
				RecipeManager.recipes.remove(r);
			}
		}

	}
}
