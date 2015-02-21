package modtweaker2.mods.forestry.handlers;

import java.util.List;

import minetweaker.MineTweakerAPI;
import minetweaker.api.liquid.ILiquidStack;
import modtweaker2.util.BaseListAddition;
import modtweaker2.util.BaseListRemoval;
import net.minecraftforge.fluids.FluidStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import forestry.factory.gadgets.MachineStill;
import forestry.factory.gadgets.MachineStill.Recipe;
import forestry.factory.gadgets.MachineStill.RecipeManager;
import static modtweaker2.helpers.InputHelper.*;
import static modtweaker2.helpers.StackHelper.*;

@ZenClass("mods.forestry.Still")
public class Still {

	@ZenMethod
	public static void addRecipe(int timePerUnit, ILiquidStack input, ILiquidStack output) {
		output.amount(output.getAmount() / 100);
		input.amount(input.getAmount() / 100);
		
		MineTweakerAPI.apply(new Add(new Recipe(timePerUnit, toFluid(input), toFluid(output))));
		MachineStill.RecipeManager.recipeFluidInputs.add(getFluid(input));
		MachineStill.RecipeManager.recipeFluidOutputs.add(getFluid(output));
	}

	private static class Add extends BaseListAddition {
		public Add(Recipe recipe) {
			super("Forestry Still", MachineStill.RecipeManager.recipes, recipe);
		}

		@Override
		public String getRecipeInfo() {
			return ((Recipe) recipe).output.getLocalizedName();
		}
	}

	@ZenMethod
	public static void removeRecipe(ILiquidStack output, ILiquidStack input) {
		MineTweakerAPI.apply(new Remove(MachineStill.RecipeManager.recipes, toFluid(input)));
	}

	private static class Remove extends BaseListRemoval {

		public Remove(List list, FluidStack input) {
			super(list, input);

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
