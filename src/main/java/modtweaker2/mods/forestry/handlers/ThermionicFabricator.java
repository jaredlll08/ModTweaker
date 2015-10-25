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
import modtweaker2.mods.forestry.ForestryListAddition;
import modtweaker2.mods.forestry.ForestryListRemoval;
import net.minecraftforge.fluids.FluidRegistry;

import modtweaker2.mods.forestry.recipes.DescriptiveRecipe;
import modtweaker2.mods.forestry.recipes.FabricatorRecipe;
import modtweaker2.mods.forestry.recipes.FabricatorSmeltingRecipe;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import forestry.api.recipes.IDescriptiveRecipe;
import forestry.api.recipes.IFabricatorManager;
import forestry.api.recipes.IFabricatorSmeltingManager;
import forestry.api.recipes.IFabricatorSmeltingRecipe;
import forestry.api.recipes.RecipeManagers;
import forestry.api.recipes.IFabricatorRecipe;

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
		MineTweakerAPI.apply(new AddSmelting(new FabricatorSmeltingRecipe(toStack(itemInput), FluidRegistry.getFluidStack("glass", fluidOutput), meltingPoint)));
	}
	
	@Deprecated
	@ZenMethod
	public static void addSmelting(IItemStack itemInput, int meltingPoint, int fluidOutput) {
		//The machines internal tank accept only liquid glass, therefor this function only accept the amount and hardcode the fluid to glass 
		MineTweakerAPI.apply(new AddSmelting(new FabricatorSmeltingRecipe(toStack(itemInput), FluidRegistry.getFluidStack("glass", fluidOutput), meltingPoint)));
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
		IDescriptiveRecipe recipe = new DescriptiveRecipe(3, 3, toShapedObjects(ingredients), toStack(output), false);
		MineTweakerAPI.apply(new AddCast(new FabricatorRecipe(toStack(plan), FluidRegistry.getFluidStack("glass", fluidInput), recipe)));
	}
	
	@Deprecated
	@ZenMethod
	public static void addCast(ILiquidStack fluidInput, IIngredient[][] ingredients, IItemStack plan, IItemStack output) {
		IDescriptiveRecipe recipe = new DescriptiveRecipe(3, 3, toShapedObjects(ingredients), toStack(output), false);
		MineTweakerAPI.apply(new AddCast(new FabricatorRecipe(toStack(plan), toFluid(fluidInput), recipe)));
	}
	
	/*
	Implements the actions to add a recipe
	Since the machine has two crafting Steps, this is a constructors for both
	*/
	private static class AddSmelting extends ForestryListAddition<IFabricatorSmeltingRecipe, IFabricatorSmeltingManager> {
		
		public AddSmelting(IFabricatorSmeltingRecipe recipe) {
			super(ThermionicFabricator.nameSmelting, RecipeManagers.fabricatorSmeltingManager);
			recipes.add(recipe);
		}
		
		@Override
		public String getRecipeInfo(IFabricatorSmeltingRecipe recipe) {
		    return LogHelper.getStackDescription(recipe.getResource());
		}
	}
	
	private static class AddCast extends ForestryListAddition<IFabricatorRecipe, IFabricatorManager> {
		
		public AddCast(IFabricatorRecipe recipe) {
			super(ThermionicFabricator.nameCasting, RecipeManagers.fabricatorManager);
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
		List<IFabricatorSmeltingRecipe> recipes = new LinkedList<IFabricatorSmeltingRecipe>();
		
		for (IFabricatorSmeltingRecipe r : RecipeManagers.fabricatorSmeltingManager.recipes()) {
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
		
		for (IFabricatorRecipe r : RecipeManagers.fabricatorManager.recipes()) {
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
		
		for (IFabricatorRecipe r : RecipeManagers.fabricatorManager.recipes()) {
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
	
	private static class RemoveSmelting extends ForestryListRemoval<IFabricatorSmeltingRecipe, IFabricatorSmeltingManager> {
		public RemoveSmelting(List<IFabricatorSmeltingRecipe> recipes) {
			super(ThermionicFabricator.nameSmelting, RecipeManagers.fabricatorSmeltingManager, recipes);
		}
		
		@Override
		public String getRecipeInfo(IFabricatorSmeltingRecipe recipe) {
			return LogHelper.getStackDescription(recipe.getResource());
		}
	}
	
	private static class RemoveCasts extends ForestryListRemoval<IFabricatorRecipe, IFabricatorManager> {
		public RemoveCasts(List<IFabricatorRecipe> recipes) {
			super(ThermionicFabricator.nameCasting, RecipeManagers.fabricatorManager, recipes);
		}
		
		@Override
		public String getRecipeInfo(IFabricatorRecipe recipe) {
			return LogHelper.getStackDescription(recipe.getRecipeOutput());
		}
	}
}
