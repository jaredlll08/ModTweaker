package modtweaker2.mods.forestry.handlers;

import static modtweaker2.helpers.InputHelper.toFluid;
import static modtweaker2.helpers.InputHelper.toIItemStack;
import static modtweaker2.helpers.InputHelper.toStack;
import static modtweaker2.helpers.StackHelper.matches;

import java.util.LinkedList;
import java.util.List;

import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import minetweaker.api.liquid.ILiquidStack;
import modtweaker2.helpers.LogHelper;
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
    
    public static final String nameSmelting = "Forestry Thermionic Fabricator (Smelting)";
    public static final String nameCasting = "Forestry Thermionic Fabricator (Casting)";
    
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Adds a smelting recipe for the Thermionic Fabricator
	 * 
	 * @param fluidOutput recipe fluid amount
	 * @param itemInput recipe input input
	 * @param meltingPoint point at where itemInput melts down
	 */
	@ZenMethod
	public static void addSmelting(int fluidOutput, IItemStack itemInput, int meltingPoint) {
		//fluidOutput hardcoded to Liquid Glass 
		MineTweakerAPI.apply(new AddSmelting(new Smelting(toStack(itemInput), FluidRegistry.getFluidStack("glass", fluidOutput), meltingPoint)));
	}
    
	@Deprecated
	@ZenMethod
	public static void addSmelting(IItemStack itemInput, int meltingPoint, int fluidOutput) {
		//The machines internal tank accept only liquid glass, therefor this function only accept the amount and hardcode the fluid to glass 
		MineTweakerAPI.apply(new AddSmelting(new Smelting(toStack(itemInput), FluidRegistry.getFluidStack("glass", fluidOutput), meltingPoint)));
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Adds a casting recipe for the Thermionic Fabricator
	 * 
	 * @param product recipe output item
	 * @param ingredients list of input items
	 * @param fluidInput recipe fluid input
	 * @param plan recipe plan item
	 */
	@ZenMethod
	public static void addCast(IItemStack product, IItemStack[][] ingredients, int fluidInput, IItemStack plan) {
		ItemStack[] flatList = new ItemStack[9];
		for ( int i = 0; i < 3; i++) {
			for ( int j = 0; j < 3; j++) {
				flatList[i*3 + j] = toStack(ingredients[i][j]);
			}
		}

		MineTweakerAPI.apply(new AddCast(new Recipe(toStack(plan), FluidRegistry.getFluidStack("glass", fluidInput), new ShapedRecipeCustom(3, 3, flatList, toStack(product)))));
	}
	
	@Deprecated
	@ZenMethod
	public static void addCast(ILiquidStack fluidInput, IItemStack[][] ingredients, IItemStack plan, IItemStack product) {
		ItemStack[] flatList = new ItemStack[9];
		for ( int i = 0; i < 3; i++) {
			for ( int j = 0; j < 3; j++) {
				flatList[i*3 + j] = toStack(ingredients[i][j]);
			}
		}

		MineTweakerAPI.apply(new AddCast(new Recipe(toStack(plan), toFluid(fluidInput), new ShapedRecipeCustom(3, 3, flatList, toStack(product)))));
	}

	/*
	Implements the actions to add a recipe
	Since the machine has two crafting Steps, this is a constructors for both
	*/
	private static class AddSmelting extends BaseListAddition<Smelting> {

		public AddSmelting(Smelting recipe) {
			super(ThermionicFabricator.nameSmelting, RecipeManager.smeltings);
			recipes.add(recipe);
		}

        @Override
        public String getRecipeInfo(Smelting recipe) {
            return LogHelper.getStackDescription(recipe.getResource());
        }
	}
	
    private static class AddCast extends BaseListAddition<Recipe> {

        public AddCast(Recipe recipe) {
            super(ThermionicFabricator.nameCasting, RecipeManager.recipes);
            recipes.add(recipe);
        }

        @Override
        public String getRecipeInfo(Recipe recipe) {
            return LogHelper.getStackDescription(recipe.asIRecipe().getRecipeOutput());
        }
    }
    
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @ZenMethod
    public static void removeSmelting(IIngredient itemInput) {
        List<Smelting> recipes = new LinkedList<Smelting>();
        
        for (Smelting r : RecipeManager.smeltings) {
            if (r != null && r.getResource() != null && matches(itemInput, toIItemStack(r.getResource()))) {
                recipes.add(r);
            }
        }
        
        if(!recipes.isEmpty()) {
            MineTweakerAPI.apply(new RemoveSmelting(recipes));
        } else {
            LogHelper.logWarning(String.format("No %s Recipe found for %s. Command ignored!", ThermionicFabricator.nameSmelting, itemInput.toString()));
        }
    }

    @ZenMethod
    public static void removeCast(IIngredient product) {
        List<Recipe> recipes = new LinkedList<Recipe>();
        
        for (Recipe r : RecipeManager.recipes) {
            if (r != null && r.asIRecipe().getRecipeOutput() != null && matches(product, toIItemStack(r.asIRecipe().getRecipeOutput()))) {
                recipes.add(r);
            }
        }
        
        if(!recipes.isEmpty()) {
            MineTweakerAPI.apply(new RemoveCasts(recipes));
        } else {
            LogHelper.logWarning(String.format("No %s Recipe found for %s. Command ignored!", ThermionicFabricator.nameSmelting, product.toString()));
        }
    }
    
    @Deprecated //Not sure why this is called Casts, Cast aint an array
    @ZenMethod
    public static void removeCasts(IIngredient product) {
        List<Recipe> recipes = new LinkedList<Recipe>();
        
        for (Recipe r : RecipeManager.recipes) {
            if (r != null && r.asIRecipe().getRecipeOutput() != null && matches(product, toIItemStack(r.asIRecipe().getRecipeOutput()))) {
                recipes.add(r);
            }
        }
        
        if(!recipes.isEmpty()) {
            MineTweakerAPI.apply(new RemoveCasts(recipes));
        } else {
            LogHelper.logWarning(String.format("No %s Recipe found for %s. Command ignored!", ThermionicFabricator.nameSmelting, product.toString()));
        }
    }

	private static class RemoveSmelting extends BaseListRemoval<Smelting> {
		public RemoveSmelting(List<Smelting> recipes) {
			super(ThermionicFabricator.nameSmelting, RecipeManager.smeltings, recipes);
		}

		@Override
		public String getRecipeInfo(Smelting recipe) {
		    return LogHelper.getStackDescription(recipe.getResource());
		}
	}
	
    private static class RemoveCasts extends BaseListRemoval<Recipe> {
        public RemoveCasts(List<Recipe> recipes) {
            super(ThermionicFabricator.nameCasting, RecipeManager.recipes, recipes);
        }

        @Override
        public String getRecipeInfo(Recipe recipe) {
            return LogHelper.getStackDescription(recipe.asIRecipe().getRecipeOutput());
        }
    }
}
