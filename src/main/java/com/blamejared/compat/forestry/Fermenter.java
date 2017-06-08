package com.blamejared.compat.forestry;

import com.blamejared.api.annotations.*;
import com.blamejared.compat.forestry.util.*;
import com.blamejared.mtlib.helpers.LogHelper;
import com.blamejared.mtlib.utils.*;
import forestry.api.fuels.*;
import forestry.api.recipes.*;
import forestry.factory.recipes.FermenterRecipe;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.*;
import minetweaker.api.liquid.ILiquidStack;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.*;

import java.util.*;
import java.util.Map.Entry;

import static com.blamejared.mtlib.helpers.InputHelper.*;
import static com.blamejared.mtlib.helpers.StackHelper.matches;

@ZenClass("mods.forestry.Fermenter")
@Handler("forestry")
public class Fermenter {
    
    public static final String name = "Forestry Fermenter";
    public static final String nameFuel = name + " (Fuel)";
    
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    /**
     * Adds recipe to fermenter
     * Amount of fluid output: fermentationValue * fluidOutputModifier
     * Note: the actual consumption of fluid input depends on the fermentation fuel
     *
     * @param fluidOutput         type of fluid produced
     * @param resource            organic item
     * @param fluidInput          type of fluid required in input
     * @param fermentationValue   amount of inputFluid on organic item requires
     * @param fluidOutputModifier Output multiplier (this is usually a from the input fluid)
     */
    @ZenMethod
    @Document({"fluidOutput", "resource", "fluidInput", "fermentationValue", "fluidOutputModifier"})
    public static void addRecipe(ILiquidStack fluidOutput, IItemStack resource, ILiquidStack fluidInput, int fermentationValue, float fluidOutputModifier) {
        MineTweakerAPI.apply(new Add(new FermenterRecipe(toStack(resource), fermentationValue, fluidOutputModifier, getFluid(fluidOutput), toFluid(fluidInput))));
    }
    
    private static class Add extends ForestryListAddition<IFermenterRecipe> {
        
        public Add(IFermenterRecipe recipe) {
            super(Fermenter.name, RecipeManagers.fermenterManager);
            recipes.add(recipe);
        }
        
        @Override
        public String getRecipeInfo(IFermenterRecipe recipe) {
            return LogHelper.getStackDescription(recipe.getOutput());
        }
    
        @Override
        public String getJEICategory(IFermenterRecipe recipe) {
            return "forestry.fermenter";
        }
    }
    
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    /**
     * Removes recipe from Fermenter
     *
     * @param input type of item in input
     */
    @ZenMethod
    @Document({"input"})
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
    
        @Override
        public String getJEICategory(IFermenterRecipe recipe) {
            return "forestry.fermenter";
        }
    }
    
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    /**
     * Adds fermenter fuel
     * Note: the actual consumption of fluid input depends on the fermentation fuel
     *
     * @param item            Item that is a valid fuel for the fermenter
     * @param fermentPerCycle How much is fermented per work cycle, i.e. how much fluid of the input is consumed.
     * @param burnDuration    Amount of work cycles a single item of this fuel lasts before expiring.
     */
    @ZenMethod
    @Document({"item", " fermentPerCycle", "burnDuration"})
    public static void addFuel(IItemStack item, int fermentPerCycle, int burnDuration) {
        MineTweakerAPI.apply(new AddFuel(new FermenterFuel(toStack(item), fermentPerCycle, burnDuration)));
    }
    
    private static class AddFuel extends BaseMapAddition<ItemStack, FermenterFuel> {
        
        public AddFuel(FermenterFuel fuelEntry) {
            super(Fermenter.nameFuel, FuelManager.fermenterFuel);
            recipes.put(fuelEntry.getItem(), fuelEntry);
        }
        
        @Override
        public String getRecipeInfo(Entry<ItemStack, FermenterFuel> fuelEntry) {
            return LogHelper.getStackDescription(fuelEntry.getKey());
        }
    }
    
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    /**
     * Removes fermenter fuel
     *
     * @param fermenterItem Item that is a valid fuel for the fermenter
     */
    @ZenMethod
    @Document({"fermenterItem"})
    public static void removeFuel(IIngredient fermenterItem) {
        Map<ItemStack, FermenterFuel> fuelItems = new HashMap<ItemStack, FermenterFuel>();
        
        for(Entry<ItemStack, FermenterFuel> fuelItem : FuelManager.fermenterFuel.entrySet()) {
            if(fuelItem != null && matches(fermenterItem, toIItemStack(fuelItem.getValue().getItem()))) {
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
