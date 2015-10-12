package modtweaker2.mods.forestry.handlers;

import static modtweaker2.helpers.InputHelper.*;
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
import net.minecraftforge.fluids.FluidRegistry;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import forestry.core.recipes.ShapedRecipeCustom;
import forestry.api.recipes.IFabricatorRecipe;
import forestry.factory.recipes.FabricatorRecipe;
import forestry.factory.tiles.TileFabricator.RecipeManager;
import forestry.factory.tiles.TileFabricator.Smelting;

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
		//The machines internal tank accept only liquid glass, therefor this function only accept the amount and hardcode the fluid to glass
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
	 * @param output recipe output item
	 * @param ingredients list of input items
	 * @param fluidInput recipe fluid input
	 * @param plan recipe plan item
	 */
	@ZenMethod
	public static void addCast(IItemStack output, IIngredient[][] ingredients, int fluidInput, @Optional IItemStack plan) {
		MineTweakerAPI.apply(new AddCast(new FabricatorRecipe(toStack(plan), FluidRegistry.getFluidStack("glass", fluidInput), ShapedRecipeCustom.createShapedRecipe(toStack(output), toShapedObjects(ingredients)))));
	}
	
	@Deprecated
	@ZenMethod
	public static void addCast(ILiquidStack fluidInput, IIngredient[][] ingredients, IItemStack plan, IItemStack output) {
		MineTweakerAPI.apply(new AddCast(new FabricatorRecipe(toStack(plan), toFluid(fluidInput), ShapedRecipeCustom.createShapedRecipe(toStack(output), toShapedObjects(ingredients)))));
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
	
	private static class AddCast extends BaseListAddition<IFabricatorRecipe> {
		
		public AddCast(IFabricatorRecipe recipe) {
			super(ThermionicFabricator.nameCasting, RecipeManager.recipes);
			recipes.add(recipe);
		}
		
		@Override
		public String getRecipeInfo(IFabricatorRecipe recipe) {
			return LogHelper.getStackDescription(recipe.getRecipeOutput());
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
		List<IFabricatorRecipe> recipes = new LinkedList<IFabricatorRecipe>();
		
		for (IFabricatorRecipe r : RecipeManager.recipes) {
			if (r != null && r.getRecipeOutput() != null && matches(product, toIItemStack(r.getRecipeOutput()))) {
				recipes.add(r);
			}
		}
		
		if(!recipes.isEmpty()) {
			MineTweakerAPI.apply(new RemoveCasts(recipes));
		} else {
			LogHelper.logWarning(String.format("No %s Recipe found for %s. Command ignored!", ThermionicFabricator.nameSmelting, product.toString()));
		}
	}
	
	@Deprecated
	@ZenMethod
	public static void removeCasts(IIngredient product) {
		List<IFabricatorRecipe> recipes = new LinkedList<IFabricatorRecipe>();
		
		for (IFabricatorRecipe r : RecipeManager.recipes) {
			if (r != null && r.getRecipeOutput() != null && matches(product, toIItemStack(r.getRecipeOutput()))) {
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
	
	private static class RemoveCasts extends BaseListRemoval<IFabricatorRecipe> {
		public RemoveCasts(List<IFabricatorRecipe> recipes) {
			super(ThermionicFabricator.nameCasting, RecipeManager.recipes, recipes);
		}
		
		@Override
		public String getRecipeInfo(IFabricatorRecipe recipe) {
			return LogHelper.getStackDescription(recipe.getRecipeOutput());
		}
	}
}
