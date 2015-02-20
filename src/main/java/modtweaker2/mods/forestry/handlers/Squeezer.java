package modtweaker2.mods.forestry.handlers;

import java.util.List;

import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import minetweaker.api.liquid.ILiquidStack;
import modtweaker2.util.BaseListAddition;
import modtweaker2.util.BaseListRemoval;
import net.minecraftforge.fluids.FluidStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import forestry.factory.gadgets.MachineSqueezer;
import forestry.factory.gadgets.MachineSqueezer.Recipe;
import forestry.factory.gadgets.MachineSqueezer.RecipeManager;
import static modtweaker2.helpers.InputHelper.*;
import static modtweaker2.helpers.StackHelper.*;

@ZenClass("mods.forestry.Squeezer")
public class Squeezer {

	@ZenMethod
	public static void addRecipe(int timePerItem, IItemStack[] resources, ILiquidStack liquid, IItemStack remnants, int chance) {
		MineTweakerAPI.apply(new Add(new Recipe(timePerItem, toStacks(resources), toFluid(liquid), toStack(remnants), chance)));
		MachineSqueezer.RecipeManager.recipeFluids.add(getFluid(liquid));
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
