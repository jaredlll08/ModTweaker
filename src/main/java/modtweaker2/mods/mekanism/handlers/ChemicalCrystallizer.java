package modtweaker2.mods.mekanism.handlers;

import static modtweaker2.helpers.InputHelper.toStack;
import static modtweaker2.mods.mekanism.MekanismHelper.toGas;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import mekanism.common.recipe.RecipeHandler.Recipe;
import mekanism.common.recipe.inputs.GasInput;
import mekanism.common.recipe.inputs.MachineInput;
import mekanism.common.recipe.machines.CrystallizerRecipe;
import mekanism.common.recipe.machines.MachineRecipe;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import minetweaker.api.item.IngredientAny;
import modtweaker2.helpers.InputHelper;
import modtweaker2.helpers.LogHelper;
import modtweaker2.helpers.StackHelper;
import modtweaker2.mods.mekanism.gas.IGasStack;
import modtweaker2.mods.mekanism.gas.MCGasStack;
import modtweaker2.mods.mekanism.util.AddMekanismRecipe;
import modtweaker2.mods.mekanism.util.RemoveMekanismRecipe;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.mekanism.chemical.Crystallizer")
public class ChemicalCrystallizer {
    
    public static final String name = "Mekanism Chemical Crystallizer";
    
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    @SuppressWarnings("unchecked")
    @ZenMethod
    public static void addRecipe(IGasStack gasInput, IItemStack itemOutput) {
        if(gasInput == null || itemOutput == null) {
            LogHelper.logError(String.format("Required parameters missing for %s Recipe.", name));
            return;
        }
        
        CrystallizerRecipe recipe = new CrystallizerRecipe(toGas(gasInput), toStack(itemOutput));
        
        MineTweakerAPI.apply(new AddMekanismRecipe(name, Recipe.CHEMICAL_CRYSTALLIZER.get(), recipe));
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @ZenMethod
    public static void removeRecipe(IIngredient itemOutput, @Optional IIngredient gasInput) {
        if(itemOutput == null) {
            LogHelper.logError(String.format("Required parameters missing for %s Recipe.", name));
            return;
        }
        
        if(gasInput == null) gasInput = IngredientAny.INSTANCE;
        
        Map<MachineInput, MachineRecipe> recipes = new HashMap<MachineInput, MachineRecipe>();
        
        for(Entry<GasInput, CrystallizerRecipe> entry : ((Map<GasInput, CrystallizerRecipe>)Recipe.CHEMICAL_CRYSTALLIZER.get()).entrySet()) {
            IGasStack inputGas = new MCGasStack(entry.getKey().ingredient);
            IItemStack outputItem = InputHelper.toIItemStack(entry.getValue().recipeOutput.output);
            
            if(!StackHelper.matches(itemOutput, outputItem)) continue;
            if(!StackHelper.matches(gasInput, inputGas)) continue;
            
            recipes.put(entry.getKey(), entry.getValue());
        }
        
        if(!recipes.isEmpty()) {
            MineTweakerAPI.apply(new RemoveMekanismRecipe(name, Recipe.CHEMICAL_CRYSTALLIZER.get(), recipes));
        } else {
            LogHelper.logWarning(String.format("No %s recipe found for %s and %s. Command ignored!", name, gasInput.toString(), itemOutput.toString()));
        }
    }
}
