package com.blamejared.compat.forestry;

import com.blamejared.ModTweaker;
import com.blamejared.mtlib.helpers.LogHelper;
import com.blamejared.mtlib.utils.BaseAddForestry;
import com.blamejared.mtlib.utils.BaseMapAddition;
import com.blamejared.mtlib.utils.BaseMapRemoval;
import com.blamejared.mtlib.utils.BaseRemoveForestry;
import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import forestry.api.fuels.FuelManager;
import forestry.api.fuels.MoistenerFuel;
import forestry.api.recipes.IMoistenerRecipe;
import forestry.api.recipes.RecipeManagers;
import forestry.factory.recipes.MoistenerRecipe;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import static com.blamejared.mtlib.helpers.InputHelper.toIItemStack;
import static com.blamejared.mtlib.helpers.InputHelper.toStack;
import static com.blamejared.mtlib.helpers.StackHelper.matches;

@ZenClass("mods.forestry.Moistener")
@ModOnly("forestry")
@ZenRegister
public class Moistener {
    
    public static final String name = "Forestry Moistener";
    public static final String nameFuel = name + " (Fuel)";
    
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    /**
     * Adds recipe to Moistener
     *
     * @param output        recipe product
     * @param input         required item
     * @param packagingTime amount of ticks per crafting operation
     */
    @ZenMethod
    public static void addRecipe(IItemStack output, IItemStack input, int packagingTime) {
        ModTweaker.LATE_ADDITIONS.add(new Add(new MoistenerRecipe(toStack(input), toStack(output), packagingTime)));
    }
    
    private static class Add extends BaseAddForestry<IMoistenerRecipe> {
        
        public Add(IMoistenerRecipe recipe) {
            super(Moistener.name, RecipeManagers.moistenerManager, recipe);
        }
        
        @Override
        public String getRecipeInfo() {
            return LogHelper.getStackDescription(recipe.getProduct());
        }
    }
    
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    /**
     * Removes recipe from Fermenter
     *
     * @param output recipe product
     */
    @ZenMethod
    public static void removeRecipe(IIngredient output) {
        ModTweaker.LATE_REMOVALS.add(new Remove(output));
    }
    
    private static class Remove extends BaseRemoveForestry<IMoistenerRecipe> {
        
        private IIngredient output;
        
        public Remove(IIngredient output) {
            super(Moistener.name, RecipeManagers.moistenerManager);
            this.output = output;
        }
        
        @Override
        public String getRecipeInfo() {
            return output.toString();
        }
        
        @Override
        public boolean checkIsRecipe(IMoistenerRecipe recipe) {
            return recipe != null && matches(output, toIItemStack(recipe.getProduct()));
        }
    }
    
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    /**
     * Adds Moistener fuel
     *
     * @param item           The item to use
     * @param product        The item that leaves the moistener's working slot (i.e. mouldy wheat, decayed wheat, mulch)
     * @param moistenerValue How much this item contributes to the final product of the moistener (i.e. mycelium)
     * @param stage          What stage this product represents. Resources with lower stage value will be consumed first. (First Stage is 0)
     */
    @ZenMethod
    public static void addFuel(IItemStack item, IItemStack product, int moistenerValue, int stage) {
        if(stage >= 0) {
            ModTweaker.LATE_ADDITIONS.add(new AddFuel(new MoistenerFuel(toStack(item), toStack(product), stage, moistenerValue)));
        } else {
            LogHelper.logWarning(String.format("No %s Recipe add for %s. Stage parameter must positive!", Moistener.name, item.toString()));
        }
    }
    
    private static class AddFuel extends BaseMapAddition<ItemStack, MoistenerFuel> {
        
        public AddFuel(MoistenerFuel fuelEntry) {
            super(Moistener.nameFuel, FuelManager.moistenerResource);
            recipes.put(fuelEntry.getItem(), fuelEntry);
        }
        
        @Override
        public String getRecipeInfo(Entry<ItemStack, MoistenerFuel> fuelEntry) {
            return LogHelper.getStackDescription(fuelEntry.getKey());
        }
    }
    
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    /**
     * Removes Moistener fuel.
     *
     * @param moistenerItem Item that is a valid fuel for the moistener
     */
    @ZenMethod
    public static void removeFuel(IIngredient moistenerItem) {
        Map<ItemStack, MoistenerFuel> fuelItems = new HashMap<ItemStack, MoistenerFuel>();
        
        for(Entry<ItemStack, MoistenerFuel> fuelItem : FuelManager.moistenerResource.entrySet()) {
            if(fuelItem != null && matches(moistenerItem, toIItemStack(fuelItem.getValue().getItem()))) {
                fuelItems.put(fuelItem.getKey(), fuelItem.getValue());
            }
        }
        
        if(!fuelItems.isEmpty()) {
            ModTweaker.LATE_REMOVALS.add(new RemoveFuel(fuelItems));
        } else {
            LogHelper.logWarning(String.format("No %s Recipe found for %s. Command ignored!", Moistener.name, moistenerItem.toString()));
        }
    }
    
    private static class RemoveFuel extends BaseMapRemoval<ItemStack, MoistenerFuel> {
        
        public RemoveFuel(Map<ItemStack, MoistenerFuel> recipes) {
            super(Moistener.nameFuel, FuelManager.moistenerResource, recipes);
        }
        
        @Override
        public String getRecipeInfo(Entry<ItemStack, MoistenerFuel> fuelEntry) {
            return LogHelper.getStackDescription(fuelEntry.getKey());
        }
    }
}
