package modtweaker.mods.forestry.handlers;

import static modtweaker.helpers.InputHelper.toStack;
import static modtweaker.helpers.InputHelper.toFluid;
import static modtweaker.helpers.InputHelper.getFluid;

import java.util.List;

import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import minetweaker.api.liquid.ILiquidStack;
import modtweaker.helpers.InputHelper;
import modtweaker.util.BaseListAddition;
import modtweaker.util.BaseListRemoval;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import forestry.Forestry;
import forestry.api.core.ForestryAPI;
import forestry.core.utils.LiquidHelper;
import forestry.factory.gadgets.MachineFermenter;
import forestry.factory.gadgets.MachineFermenter.Recipe;
import forestry.factory.gadgets.MachineFermenter.RecipeManager;

@ZenClass("mods.forestry.Fermenter")
public class Fermenter {

	@ZenMethod
	public static void addRecipe(IItemStack resource, int fermentationValue, float modifier, ILiquidStack output, ILiquidStack liquid) {
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
	public static void removeRecipe(ILiquidStack output) {
		MineTweakerAPI.apply(new Remove(MachineFermenter.RecipeManager.recipes, toFluid(output)));
	}

	private static class Remove extends BaseListRemoval {

		public Remove(List list, FluidStack stack) {
			super(list, stack);

		}

		@Override
		public void apply() {
			for (Recipe r : RecipeManager.recipes) {
				if (r.output != null && r.output.isFluidEqual(fluid)) {
					recipe = r;
					break;
				}
			}
			RecipeManager.recipes.remove(recipe);

		}

	}
}
