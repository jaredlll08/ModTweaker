package modtweaker.mods.forestry.handlers;

import java.util.List;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidRegistry;

import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import minetweaker.api.liquid.ILiquidStack;
import minetweaker.api.item.IIngredient;
import modtweaker.util.BaseListAddition;
import modtweaker.util.BaseListRemoval;
import static modtweaker.helpers.InputHelper.toFluid;
import static modtweaker.helpers.InputHelper.toStack;
import static modtweaker.helpers.InputHelper.toObject;

import forestry.core.utils.ShapedRecipeCustom;
import forestry.factory.gadgets.MachineFabricator;
import forestry.factory.gadgets.MachineFabricator.RecipeManager;
import forestry.factory.gadgets.MachineFabricator.Recipe;
import forestry.factory.gadgets.MachineFabricator.Smelting;


@ZenClass("mods.forestry.ThermionicFabricator")
public class ThermionicFabricator {
	
	//first production step: smelting item into liquid
	@ZenMethod
	public static void addSmelting(IItemStack itemInput, int fluidOutput, int meltingPoint) {
		//The machines internal tank accept only liquid glass, therefor this function only accept the amount and hardcode the fluid to glass 
		MineTweakerAPI.apply(new Add(new Smelting(toStack(itemInput), FluidRegistry.getFluidStack("glass", fluidOutput), meltingPoint)));
	}

	@ZenMethod
	public static void removeSmelting(IItemStack itemInput) {
		MineTweakerAPI.apply(new RemoveSmelting(toStack(itemInput)));
	}

	//second step: casting liquid + objetcs + plan into result
	@ZenMethod
	public static void addCast(ILiquidStack fluidInput, IItemStack[][] ingredients, IItemStack plan, IItemStack product) {
		ItemStack[] flatList = new ItemStack[9];
		for ( int i = 0; i < 3; i++) {
			for ( int j = 0; j < 3; j++) {
				flatList[i*3 + j] = toStack(ingredients[i][j]);
			}
		}

		MineTweakerAPI.apply(new Add(new Recipe(toStack(plan), toFluid(fluidInput), new ShapedRecipeCustom(3, 3, flatList, toStack(product)))));
	}

	@ZenMethod
	public static void removeCasts(IItemStack product) {
		MineTweakerAPI.apply(new RemoveCastings(toStack(product)));
	}

	/*
	Implements the actions to add a recipe
	Since the machine has two crafting Steps, this is a constructors for both
	*/
	private static class Add extends BaseListAddition {

		public Add(Smelting recipe) {
			super("Forestry Thermionic Fabricator (Smelting)", RecipeManager.smeltings, recipe);
		}

		public Add(Recipe recipe) {
			super("Forestry Thermionic Fabricator (Casting)", RecipeManager.recipes, recipe);
		}

		@Override
		public String getRecipeInfo() {
			if (recipe instanceof Smelting)
				return " Input:" + ((Smelting) recipe).getResource().getDisplayName();
			else
				return "Output: " + ((Recipe) recipe).asIRecipe().getRecipeOutput().getDisplayName();
		}
	}

	/*
	Implements the actions to remove a smelting recipe
	*/
	private static class RemoveSmelting extends BaseListRemoval {
		
		public RemoveSmelting(ItemStack input) {
			super("Forestry Thermionic Fabricator (Smelting)", RecipeManager.smeltings, input);
		}

		@Override
		public void apply() {
			for (Smelting r : RecipeManager.smeltings) {
				if (r.getResource() != null && r.getResource().isItemEqual(stack)) {
					RecipeManager.smeltings.remove(r);
					recipe = r;
					break;
				}
			}
		}

		@Override
		public String getRecipeInfo() {
			return " Input:" + stack.getDisplayName(); // + " -- Output:" + ((Smelting) recipe).getProduct().getLocalizedName();
		}
	}

	/*
	Implements the actions to remove a casting recipe
	*/
	private static class RemoveCastings extends BaseListRemoval {
	
		public RemoveCastings(ItemStack output) {
			super("Forestry Thermionic Fabricator (Casting)", RecipeManager.recipes, output);
		}

		@Override
		public void apply() {
			for (Recipe r : RecipeManager.recipes) {
				if (r.asIRecipe().getRecipeOutput() != null && r.asIRecipe().getRecipeOutput().isItemEqual(stack)) {
					RecipeManager.recipes.remove(r);
					recipe = r;
				}
			}
		}

		@Override
		public String getRecipeInfo() {
			return " Output:" + stack.getDisplayName(); // + " -- Output:" + ((Recipe) recipe).asIRecipe().getIngredients()[0].getDisplayName();
		}
	}
}
