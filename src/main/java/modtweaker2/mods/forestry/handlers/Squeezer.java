package modtweaker2.mods.forestry.handlers;

import static modtweaker2.helpers.InputHelper.getFluid;
import static modtweaker2.helpers.InputHelper.toFluid;
import static modtweaker2.helpers.InputHelper.toStack;
import static modtweaker2.helpers.InputHelper.toStacks;

import java.util.Arrays;
import java.util.List;

import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import minetweaker.api.liquid.ILiquidStack;
import modtweaker2.utils.BaseListAddition;
import modtweaker2.utils.BaseListRemoval;
import net.minecraftforge.fluids.FluidStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import forestry.factory.gadgets.MachineSqueezer;
import forestry.factory.gadgets.MachineSqueezer.Recipe;
import forestry.factory.gadgets.MachineSqueezer.RecipeManager;

@ZenClass("mods.forestry.Squeezer")
public class Squeezer {

	@ZenMethod
	public static void addRecipe(int timePerItem, IItemStack[] resources, ILiquidStack liquid, IItemStack remnants, int chance) {
		MineTweakerAPI.apply(new Add(new Recipe(timePerItem, toStacks(resources), toFluid(liquid), toStack(remnants), chance)));
		MachineSqueezer.RecipeManager.recipeFluids.add(getFluid(liquid));
		MachineSqueezer.RecipeManager.recipeInputs.addAll(Arrays.asList(toStacks(resources)));
	}

	private static class Add extends BaseListAddition {
		public Add(Recipe recipe) {
			super("Forestry Squeezer", MachineSqueezer.RecipeManager.recipes, recipe);
		}

		@Override
		public String getRecipeInfo() {
			return ((Recipe) recipe).liquid.getLocalizedName();
		}
	}

	@ZenMethod
	public static void removeRecipe(ILiquidStack liquid) {
		MineTweakerAPI.apply(new Remove(MachineSqueezer.RecipeManager.recipes, toFluid(liquid)));
	}

	private static class Remove extends BaseListRemoval {

		public Remove(List list, FluidStack liquid) {
			super(list, liquid);

		}

		@Override
		public void apply() {
			for (Recipe r : RecipeManager.recipes) {
				if (r.liquid != null && r.liquid.isFluidEqual(fluid)) {
					recipe = r;
					break;
				}
			}
			RecipeManager.recipes.remove(recipe);

		}

	}
}
