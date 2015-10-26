package modtweaker2.mods.forestry.handlers;

import static modtweaker2.helpers.InputHelper.getFluid;
import static modtweaker2.helpers.InputHelper.toFluid;
import static modtweaker2.helpers.InputHelper.toIItemStack;
import static modtweaker2.helpers.InputHelper.toILiquidStack;
import static modtweaker2.helpers.InputHelper.toStack;
import static modtweaker2.helpers.StackHelper.matches;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import minetweaker.api.liquid.ILiquidStack;
import modtweaker2.helpers.LogHelper;
import modtweaker2.mods.forestry.ForestryListAddition;
import modtweaker2.mods.forestry.ForestryListRemoval;
import modtweaker2.mods.forestry.recipes.FermenterRecipe;
import modtweaker2.utils.BaseMapAddition;
import modtweaker2.utils.BaseMapRemoval;
import net.minecraft.item.ItemStack;

import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import forestry.api.fuels.FermenterFuel;
import forestry.api.fuels.FuelManager;
import forestry.api.recipes.IFermenterManager;
import forestry.api.recipes.IFermenterRecipe;
import forestry.api.recipes.RecipeManagers;

@ZenClass("mods.forestry.Fermenter")
public class Fermenter {

	public static final String name = "Forestry Fermenter";
	public static final String nameFuel = name + " (Fuel)";
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Adds a fermenter recipe. Amount of fluid output is calculated: fermentationValue * fluidOutputModifier
	 * Note: the actual consumption of fluid input depends on the fermentation fuel
	 * 
	 * @param fluidOutput type of fluid produced
	 * @param resource organic item
	 * @param fluidInput type of fluid required in input
	 * @param fermentationValue amount of inputFluid on organic item requires
	 * @param fluidOutputModifier Output multiplier (this is usually a from the input fluid
	 */
	@ZenMethod
	public static void addRecipe(ILiquidStack fluidOutput, IItemStack resource, ILiquidStack fluidInput, int fermentationValue, float fluidOutputModifier) {
		MineTweakerAPI.apply(new Add(new FermenterRecipe(toStack(resource), fermentationValue, fluidOutputModifier, getFluid(fluidOutput), toFluid(fluidInput))));
	}
	
	@Deprecated
	@ZenMethod
	public static void addRecipe(IItemStack resource, ILiquidStack fluidInput, int fermentationValue, float fluidOutputModifier, ILiquidStack fluidOutput) {
		MineTweakerAPI.apply(new Add(new FermenterRecipe(toStack(resource), fermentationValue, fluidOutputModifier, getFluid(fluidOutput), toFluid(fluidInput))));
	}

	private static class Add extends ForestryListAddition<IFermenterRecipe, IFermenterManager> {
		public Add(IFermenterRecipe recipe) {
			super(Fermenter.name, RecipeManagers.fermenterManager);
			recipes.add(recipe);
		}

		@Override
		public String getRecipeInfo(IFermenterRecipe recipe) {
			return LogHelper.getStackDescription(recipe.getOutput());
		}
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@ZenMethod
	public static void removeRecipe(IIngredient input) {
		List<IFermenterRecipe> recipes = new LinkedList<IFermenterRecipe>();
		
		for(IFermenterRecipe recipe : RecipeManagers.fermenterManager.recipes()) {
			// check for input items
			if(recipe != null && recipe.getResource() != null && matches(input, toIItemStack(recipe.getResource()))) {
				recipes.add(recipe);
			}
			
			// check for input liquids
			if(recipe != null && recipe.getResource() != null && matches(input, toILiquidStack(recipe.getFluidResource()))) {
				recipes.add(recipe);
			}
		}
		
		if(!recipes.isEmpty()) {
			MineTweakerAPI.apply(new Remove(recipes));
		} else {
			LogHelper.logWarning(String.format("No %s Recipe found for %s. Command ignored!", Fermenter.name, input.toString()));
		}
	}

	private static class Remove extends ForestryListRemoval<IFermenterRecipe, IFermenterManager> {

		public Remove(List<IFermenterRecipe> recipes) {
			super(Fermenter.name, RecipeManagers.fermenterManager, recipes);
		}

		@Override
		protected String getRecipeInfo(IFermenterRecipe recipe) {
			return LogHelper.getStackDescription(recipe.getOutput());
		}
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Adds fermenter fuel.  
	 * Note: the actual consumption of fluid input depends on the fermentation fuel
	 * 
	 * @param item Item that is a valid fuel for the fermenter
	 * @param fermentPerCycle How much is fermented per work cycle, i.e. how much fluid of the input is consumed.
	 * @param burnDuration Amount of work cycles a single item of this fuel lasts before expiring.
	 */
	@ZenMethod
	public static void addFuel(IItemStack item, int fermentPerCycle, int burnDuration) {
		MineTweakerAPI.apply(new AddFuel(new FermenterFuel(toStack(item), fermentPerCycle, burnDuration)));
	}
	
	private static class AddFuel extends BaseMapAddition<ItemStack, FermenterFuel> {
		public AddFuel(FermenterFuel fuelEntry) {
			super(Fermenter.nameFuel, FuelManager.fermenterFuel);
			recipes.put(fuelEntry.item, fuelEntry);
		}
		
		@Override
		public String getRecipeInfo(Entry<ItemStack, FermenterFuel> fuelEntry) {
			return LogHelper.getStackDescription(fuelEntry.getKey());
		}
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Removes a fermenter fuel.  
	 * 
	 * @param fermenterItem Item that is a valid fuel for the fermenter
	 */
	@ZenMethod
	public static void removeFuel(IIngredient fermenterItem) {
		Map<ItemStack, FermenterFuel> fuelItems = new HashMap<ItemStack, FermenterFuel>();
		
		for(Entry<ItemStack, FermenterFuel> fuelItem : FuelManager.fermenterFuel.entrySet()) {
			if(fuelItem != null && matches(fermenterItem, toIItemStack(fuelItem.getValue().item))) {
				fuelItems.put(fuelItem.getKey(), fuelItem.getValue());
			}
		}
		
		if(!fuelItems.isEmpty()) {
			MineTweakerAPI.apply(new RemoveFuel(fuelItems));
		} else {
			LogHelper.logWarning(String.format("No %s Recipe found for %s. Command ignored!", Fermenter.name, fermenterItem.toString()));
		}
	}
	
	private static class RemoveFuel extends BaseMapRemoval<ItemStack, FermenterFuel> {
		public RemoveFuel(Map<ItemStack, FermenterFuel> recipes) {
			super(Fermenter.nameFuel, FuelManager.fermenterFuel, recipes);
		}
		
		@Override
		public String getRecipeInfo(Entry<ItemStack, FermenterFuel> fuelEntry) {
			return LogHelper.getStackDescription(fuelEntry.getKey());
		}
	}
}

