package modtweaker2.mods.mekanism.handlers;

import static modtweaker2.mods.mekanism.MekanismHelper.toGas;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import mekanism.common.recipe.RecipeHandler.Recipe;
import mekanism.common.recipe.inputs.ChemicalPairInput;
import mekanism.common.recipe.inputs.MachineInput;
import mekanism.common.recipe.machines.ChemicalInfuserRecipe;
import mekanism.common.recipe.machines.MachineRecipe;
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

@ZenClass("mods.mekanism.chemical.Infuser")
public class ChemicalInfuser {

    public static final String name = "Mekanism Infuser";
    
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    @SuppressWarnings("unchecked")
    @ZenMethod
    public static void addRecipe(IGasStack leftGasInput, IGasStack rightGasInput, IGasStack gasOutput) {
        if(leftGasInput == null || rightGasInput == null || gasOutput == null) {
            LogHelper.logError(String.format("Required parameters missing for %s Recipe.", name));
            return;
        }
        
        ChemicalInfuserRecipe recipe = new ChemicalInfuserRecipe(toGas(leftGasInput), toGas(rightGasInput), toGas(gasOutput));
        
        MineTweakerAPI.apply(new AddMekanismRecipe(name, Recipe.CHEMICAL_INFUSER.get(), recipe));
    }
    
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @ZenMethod
    public static void removeRecipe(IIngredient gasOutput, @Optional IIngredient leftGasInput, @Optional IIngredient rightGasInput) {
        if(gasOutput == null) {
            LogHelper.logError(String.format("Required parameters missing for %s Recipe.", name));
            return;
        }
        
        if(leftGasInput == null) leftGasInput = IngredientAny.INSTANCE;
        if(rightGasInput == null) rightGasInput = IngredientAny.INSTANCE;
        
        Map<MachineInput, MachineRecipe> recipes = new HashMap<MachineInput, MachineRecipe>();
        
        for(Entry<ChemicalPairInput, ChemicalInfuserRecipe> entry : ((Map<ChemicalPairInput, ChemicalInfuserRecipe>)Recipe.CHEMICAL_INFUSER.get()).entrySet()) {
            IGasStack inputGasLeft = new MCGasStack(entry.getKey().leftGas);
            IGasStack inputGasRight = new MCGasStack(entry.getKey().rightGas);
            IGasStack outputGas = new MCGasStack(entry.getValue().recipeOutput.output);
            
            if(!StackHelper.matches(gasOutput, outputGas)) continue;
            if(!StackHelper.matches(leftGasInput, inputGasLeft)) continue;
            if(!StackHelper.matches(rightGasInput, inputGasRight)) continue;
            
            recipes.put(entry.getKey(), entry.getValue());
        }
        
        if(!recipes.isEmpty()) {
            MineTweakerAPI.apply(new RemoveMekanismRecipe(name, Recipe.CHEMICAL_INFUSER.get(), recipes));
        } else {
            LogHelper.logWarning(String.format("No %s recipe found for %s, %s and %s. Command ignored!", name, gasOutput.toString(), leftGasInput.toString(), rightGasInput.toString()));
        }
    }
}
