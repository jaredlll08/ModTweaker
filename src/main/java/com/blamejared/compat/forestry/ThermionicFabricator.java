package com.blamejared.compat.forestry;

import com.blamejared.api.annotations.Handler;
import com.blamejared.compat.forestry.util.*;
import com.blamejared.mtlib.helpers.LogHelper;
import forestry.api.recipes.*;
import forestry.factory.recipes.FabricatorSmeltingRecipe;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.*;
import net.minecraftforge.fluids.FluidRegistry;
import stanhebben.zenscript.annotations.*;

import java.util.*;

import static com.blamejared.mtlib.helpers.InputHelper.*;
import static com.blamejared.mtlib.helpers.StackHelper.matches;

@ZenClass("mods.forestry.ThermionicFabricator")
@Handler("forestry")
public class ThermionicFabricator {
    
    public static final String nameSmelting = "Forestry Thermionic Fabricator (Smelting)";
    public static final String nameCasting = "Forestry Thermionic Fabricator (Casting)";
    
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    /**
     * Adds smelting recipe to Thermionic Fabricator
     *
     * @param fluidOutput  recipe fluid amount
     * @param itemInput    recipe input input
     * @param meltingPoint point at where itemInput melts down
     */
    @ZenMethod
    public static void addSmelting(int fluidOutput, IItemStack itemInput, int meltingPoint) {
        //The machines internal tank accept only liquid glass, therefor this function only accept the amount and hardcode the fluid to glass
        MineTweakerAPI.apply(new AddSmelting(new FabricatorSmeltingRecipe(toStack(itemInput), FluidRegistry.getFluidStack("glass", fluidOutput), meltingPoint)));
    }
    
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    
    /*
    Implements the actions to add a recipe
    Since the machine has two crafting Steps, this is a constructors for both
    */
    private static class AddSmelting extends ForestryListAddition<IFabricatorSmeltingRecipe> {
        
        public AddSmelting(IFabricatorSmeltingRecipe recipe) {
            super(ThermionicFabricator.nameSmelting, RecipeManagers.fabricatorSmeltingManager);
            recipes.add(recipe);
        }
        
        @Override
        public String getRecipeInfo(IFabricatorSmeltingRecipe recipe) {
            return LogHelper.getStackDescription(recipe.getResource());
        }
        
        @Override
        public String getJEICategory(IFabricatorSmeltingRecipe recipe) {
            return "forestry.fabricator";
        }
    }
    
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    /**
     * Removes smelting recipe from Thermionic Fabricator
     *
     * @param itemInput = item input
     */
    @ZenMethod
    public static void removeSmelting(IIngredient itemInput) {
        List<IFabricatorSmeltingRecipe> recipes = new LinkedList<IFabricatorSmeltingRecipe>();
        
        for(IFabricatorSmeltingRecipe r : RecipeManagers.fabricatorSmeltingManager.recipes()) {
            if(r != null && r.getResource() != null && matches(itemInput, toIItemStack(r.getResource()))) {
                recipes.add(r);
            }
        }
        
        if(!recipes.isEmpty()) {
            MineTweakerAPI.apply(new RemoveSmelting(recipes));
        } else {
            LogHelper.logWarning(String.format("No %s Recipe found for %s. Command ignored!", ThermionicFabricator.nameSmelting, itemInput.toString()));
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
        
        @Override
        public String getJEICategory(IFabricatorSmeltingRecipe recipe) {
            return "forestry.fabricator";
        }
    }
    
}