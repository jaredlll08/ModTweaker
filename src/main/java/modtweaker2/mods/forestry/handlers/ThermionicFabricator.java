package modtweaker2.mods.forestry.handlers;

import static modtweaker2.helpers.InputHelper.toFluid;
import static modtweaker2.helpers.InputHelper.toStack;

import java.util.List;

import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import minetweaker.api.liquid.ILiquidStack;
import modtweaker2.utils.BaseListAddition;
import modtweaker2.utils.BaseListRemoval;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import forestry.core.utils.ShapedRecipeCustom;
import forestry.factory.gadgets.MachineFabricator.Recipe;
import forestry.factory.gadgets.MachineFabricator.RecipeManager;
import forestry.factory.gadgets.MachineFabricator.Smelting;

@ZenClass("mods.forestry.ThermionicFabricator")
public class ThermionicFabricator {
	
	//first production step: smelting item into liquid
	@ZenMethod
	public static void addSmelting(IItemStack itemInput, int meltingPoint, int fluidOutput) {
		//The machines internal tank accept only liquid glass, therefor this function only accept the amount and hardcode the fluid to glass 
		MineTweakerAPI.apply(new Add(new Smelting(toStack(itemInput), FluidRegistry.getFluidStack("glass", fluidOutput), meltingPoint)));
	}

	@ZenMethod
	public static void removeSmelting(IItemStack itemInput) {
		MineTweakerAPI.apply(new Remove(toStack(itemInput), RecipeManager.smeltings, RecipeType.Smelting));
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
		MineTweakerAPI.apply(new Remove(toStack(product), RecipeManager.recipes, RecipeType.Casting));
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
	Implements the actions to remove recipes
	*/
	private static class Remove extends BaseListRemoval {
	    private final RecipeType type;
		
		public Remove(ItemStack input, List list, RecipeType type) {
			super(String.format("Forestry Thermionic Fabricator (%s)", type.toString()), list, input);
			this.type = type;
		}

		@Override
		public void apply() {
		    switch(type) {
		        case Smelting:
		            for (Object r : list) {
		                if (((Smelting)r).getResource() != null && ((Smelting)r).getResource().isItemEqual(stack)) {
		                    recipes.add(r);
		                }
		            }
		            break;
		            
		        case Casting:
		            for (Object r : list) {
		                if (((Recipe)r).asIRecipe().getRecipeOutput() != null && ((Recipe)r).asIRecipe().getRecipeOutput().isItemEqual(stack)) {
		                    recipes.add(r);
		                }
		            }
		            break;
		    }
			super.apply();
		}

		@Override
		public String getRecipeInfo() {
		    switch(type) {
		        case Smelting:
		            return " Input:" + stack.getDisplayName(); // + " -- Output:" + ((Smelting) recipe).getProduct().getLocalizedName();
		            
		        case Casting:
		            return " Output:" + stack.getDisplayName(); // + " -- Output:" + ((Recipe) recipe).asIRecipe().getIngredients()[0].getDisplayName();
		    }
		    
		    return stack.getDisplayName();
		}
	}

    public enum RecipeType {
        Smelting,
        Casting
    }
}
