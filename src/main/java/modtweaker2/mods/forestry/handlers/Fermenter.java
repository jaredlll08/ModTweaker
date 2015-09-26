package modtweaker2.mods.forestry.handlers;

import static modtweaker2.helpers.InputHelper.toFluid;
import static modtweaker2.helpers.InputHelper.toIItemStack;
import static modtweaker2.helpers.InputHelper.toILiquidStack;
import static modtweaker2.helpers.InputHelper.toStack;
import static modtweaker2.helpers.StackHelper.matches;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import minetweaker.api.liquid.ILiquidStack;
import modtweaker2.helpers.LogHelper;
import modtweaker2.utils.BaseListAddition;
import modtweaker2.utils.BaseListRemoval;
import modtweaker2.utils.BaseMapAddition;
import modtweaker2.utils.BaseMapRemoval;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import forestry.api.fuels.FermenterFuel;
import forestry.api.fuels.FuelManager;
import forestry.factory.gadgets.MachineFermenter.Recipe;
import forestry.factory.gadgets.MachineFermenter.RecipeManager;

@ZenClass("mods.forestry.Fermenter")
public class Fermenter {

    public static final String name = "Forestry Fermenter";
    
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
		MineTweakerAPI.apply(new Add(new Recipe(toStack(resource), fermentationValue, fluidOutputModifier, toFluid(fluidOutput), toFluid(fluidInput))));
	}
    
    @Deprecated
    @ZenMethod
	public static void addRecipe(IItemStack resource, ILiquidStack fluidInput, int fermentationValue, float fluidOutputModifier, ILiquidStack fluidOutput) {
		MineTweakerAPI.apply(new Add(new Recipe(toStack(resource), fermentationValue, fluidOutputModifier, toFluid(fluidOutput), toFluid(fluidInput))));
	}

	private static class Add extends BaseListAddition<Recipe> {
		public Add(Recipe recipe) {
			super(Fermenter.name, RecipeManager.recipes);
			recipes.add(recipe);
		}
		
		@Override
		public void apply() {
		    // add liquids to valid input / output
		    for(Recipe recipe : successful) {
		        RecipeManager.recipeFluidInputs.add(recipe.liquid.getFluid());
		        RecipeManager.recipeFluidOutputs.add(recipe.output.getFluid());
		    }
		    
		    super.apply();
		}
		
		@Override
		public void undo() {
		    super.undo();
		    
		    // Tidy up valid inputs
		    for(Iterator<Fluid> iter = RecipeManager.recipeFluidInputs.iterator(); iter.hasNext();) {
		        boolean found = false;
		        Fluid fluid = iter.next();
		        for(Recipe recipe : list) {
		            if(recipe != null && recipe.liquid != null && recipe.liquid.getFluid().equals(fluid)) {
		                found = true;
		            }
		        }
		        
                if(!found) {
                    iter.remove();
                }
		    }
		    
		    // Tidy up valid outputs
            for(Iterator<Fluid> iter = RecipeManager.recipeFluidOutputs.iterator(); iter.hasNext();) {
                boolean found = false;
                Fluid fluid = iter.next();
                for(Recipe recipe : list) {
                    if(recipe != null && recipe.output != null && recipe.output.getFluid().equals(fluid)) {
                        found = true;
                    }
                }
                
                if(!found) {
                    iter.remove();
                }
            }
		}

		@Override
		public String getRecipeInfo(Recipe recipe) {
			return LogHelper.getStackDescription(recipe.output);
		}
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@ZenMethod
	public static void removeRecipe(IIngredient input) {
	    List<Recipe> recipes = new LinkedList<Recipe>();
	    
	    for(Recipe recipe : RecipeManager.recipes) {
	        // check for input items
	        if(recipe != null && recipe.resource != null && matches(input, toIItemStack(recipe.resource))) {
	            recipes.add(recipe);
	        }
	        
	        // check for input liquids
            if(recipe != null && recipe.resource != null && matches(input, toILiquidStack(recipe.liquid))) {
                recipes.add(recipe);
            }	        
	    }
	    
	    if(!recipes.isEmpty()) {
	        MineTweakerAPI.apply(new Remove(recipes));
	    } else {
	        LogHelper.logWarning(String.format("No %s Recipe found for %s. Command ignored!", Fermenter.name, input.toString()));
	    }
	}

	private static class Remove extends BaseListRemoval<Recipe> {

		public Remove(List<Recipe> recipes) {
			super(Fermenter.name, RecipeManager.recipes, recipes);
		}
		
		@Override
		public void apply() {
		    super.apply();
		    
            // Tidy up valid inputs
            for(Iterator<Fluid> iter = RecipeManager.recipeFluidInputs.iterator(); iter.hasNext();) {
                boolean found = false;
                Fluid fluid = iter.next();
                for(Recipe recipe : list) {
                    if(recipe != null && recipe.liquid != null && recipe.liquid.getFluid().equals(fluid)) {
                        found = true;
                    }
                }
                
                if(!found) {
                    iter.remove();
                }
            }
            
            // Tidy up valid outputs
            for(Iterator<Fluid> iter = RecipeManager.recipeFluidOutputs.iterator(); iter.hasNext();) {
                boolean found = false;
                Fluid fluid = iter.next();
                for(Recipe recipe : list) {
                    if(recipe != null && recipe.output != null && recipe.output.getFluid().equals(fluid)) {
                        found = true;
                    }
                }
                
                if(!found) {
                    iter.remove();
                }
            }
		}
		
		@Override
		public void undo() {
            // add liquids to valid input / output
            for(Recipe recipe : successful) {
                RecipeManager.recipeFluidInputs.add(recipe.liquid.getFluid());
                RecipeManager.recipeFluidOutputs.add(recipe.output.getFluid());
            }
            
            super.undo();
		}
		
        @Override
        public String getRecipeInfo(Recipe recipe) {
            return LogHelper.getStackDescription(recipe.output);
        }
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Adds fermenter fuel.  
	 * Note: the actual consumption of fluid input depends on the fermentation fuel
	 * 
	 * @param item Item that is a valid fuel for the fermenter
	 * @param fermentPerCycle How much is fermeted per work cycle, i.e. how much fluid of the input is consumed.
	 * @param burnDuration Amount of work cycles a single item of this fuel lasts before expiring.
	 */
    @ZenMethod
	public static void addFuel(IItemStack item, int fermentPerCycle, int burnDuration) {
		MineTweakerAPI.apply(new AddFuel(new FermenterFuel(toStack(item), fermentPerCycle, burnDuration)));
	}
    
	private static class AddFuel extends BaseMapAddition<ItemStack, FermenterFuel> {
		public AddFuel(FermenterFuel fuelEntry) {
			super(Fermenter.name, FuelManager.fermenterFuel);
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
    		super(Fermenter.name, FuelManager.fermenterFuel, recipes);
    	}

    	@Override
    	public String getRecipeInfo(Entry<ItemStack, FermenterFuel> fuelEntry) {
    		return LogHelper.getStackDescription(fuelEntry.getKey());
    	}
    }
}
	
