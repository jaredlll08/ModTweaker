package modtweaker2.mods.mekanism.handlers;

import static modtweaker2.mods.mekanism.MekanismHelper.toGas;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import mekanism.common.recipe.RecipeHandler.Recipe;
import mekanism.common.recipe.inputs.GasInput;
import mekanism.common.recipe.inputs.MachineInput;
import mekanism.common.recipe.machines.MachineRecipe;
import mekanism.common.recipe.machines.WasherRecipe;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IngredientAny;
import modtweaker2.helpers.LogHelper;
import modtweaker2.helpers.StackHelper;
import modtweaker2.mods.mekanism.gas.IGasStack;
import modtweaker2.mods.mekanism.gas.MCGasStack;
import modtweaker2.mods.mekanism.util.AddMekanismRecipe;
import modtweaker2.mods.mekanism.util.RemoveMekanismRecipe;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.mekanism.chemical.Washer")
public class ChemicalWasher {
    
    public static final String name = "Mekanism Washer";
    
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    @SuppressWarnings("unchecked")
    @ZenMethod
    public static void addRecipe(IGasStack gasInput, IGasStack gasOutput) {
        if(gasInput == null || gasOutput == null) {
            LogHelper.logError(String.format("Required parameters missing for %s Recipe.", name));
            return;
        }
        
        WasherRecipe recipe = new WasherRecipe(toGas(gasInput), toGas(gasOutput));
        
        MineTweakerAPI.apply(new AddMekanismRecipe(name, Recipe.CHEMICAL_WASHER.get(), recipe));
    }
    
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @ZenMethod
    public static void removeRecipe(IIngredient gasOutput, @Optional IIngredient gasInput) {
        if(gasOutput == null) {
            LogHelper.logError(String.format("Required parameters missing for %s Recipe.", name));
            return;
        }
        
        if(gasInput == null) gasInput = IngredientAny.INSTANCE;
        
        Map<MachineInput, MachineRecipe> recipes = new HashMap<MachineInput, MachineRecipe>();
        
        for(Entry<GasInput, WasherRecipe> entry : ((Map<GasInput, WasherRecipe>)Recipe.CHEMICAL_WASHER.get()).entrySet()) {
            IGasStack inputGas = new MCGasStack(entry.getKey().ingredient);
            IGasStack outputGas = new MCGasStack(entry.getValue().recipeOutput.output);
            
            if(!StackHelper.matches(gasInput, inputGas)) continue;
            if(!StackHelper.matches(gasOutput, outputGas)) continue;
            
            recipes.put(entry.getKey(), entry.getValue());
        }
        
        if(!recipes.isEmpty()) {
            MineTweakerAPI.apply(new RemoveMekanismRecipe(name, Recipe.CHEMICAL_WASHER.get(), recipes));
        } else {
            LogHelper.logWarning(String.format("No %s recipe found for %s and %s. Command ignored!", name, gasOutput.toString(), gasInput.toString()));
        }
    }
}
