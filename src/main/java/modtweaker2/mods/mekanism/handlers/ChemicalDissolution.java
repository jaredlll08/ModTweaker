package modtweaker2.mods.mekanism.handlers;

import static modtweaker2.helpers.InputHelper.toStack;
import static modtweaker2.mods.mekanism.MekanismHelper.toGas;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import mekanism.common.recipe.RecipeHandler.Recipe;
import mekanism.common.recipe.inputs.ItemStackInput;
import mekanism.common.recipe.inputs.MachineInput;
import mekanism.common.recipe.machines.DissolutionRecipe;
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

@ZenClass("mods.mekanism.chemical.Dissolution")
public class ChemicalDissolution {
    
    public static final String name = "Mekanism Dissolution";
    
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @SuppressWarnings("unchecked")
    @ZenMethod
    public static void addRecipe(IItemStack itemInput, IGasStack gasOutput) {
        if(itemInput == null || gasOutput == null) {
            LogHelper.logError(String.format("Required parameters missing for %s Recipe.", name));
            return;
        }
        
        DissolutionRecipe recipe = new DissolutionRecipe(toStack(itemInput), toGas(gasOutput));
        
        MineTweakerAPI.apply(new AddMekanismRecipe(name, Recipe.CHEMICAL_DISSOLUTION_CHAMBER.get(), recipe));
    }
    
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @ZenMethod
    public static void removeRecipe(IIngredient gasOutput, @Optional IIngredient itemInput) {
        if(gasOutput == null) {
            LogHelper.logError(String.format("Required parameters missing for %s Recipe.", name));
            return;
        }
        
        if(itemInput == null) itemInput = IngredientAny.INSTANCE;
        
        Map<MachineInput, MachineRecipe> recipes = new HashMap<MachineInput, MachineRecipe>();
        
        for(Entry<ItemStackInput, DissolutionRecipe> entry : ((Map<ItemStackInput, DissolutionRecipe>)Recipe.CHEMICAL_DISSOLUTION_CHAMBER.get()).entrySet()) {
            IItemStack inputItem = InputHelper.toIItemStack(entry.getKey().ingredient);
            IGasStack outputGas = new MCGasStack(entry.getValue().recipeOutput.output);
            
            if(!StackHelper.matches(gasOutput, outputGas)) continue;
            if(!StackHelper.matches(itemInput, inputItem)) continue;

            recipes.put(entry.getKey(), entry.getValue());
        }
        
        if(!recipes.isEmpty()) {
            MineTweakerAPI.apply(new RemoveMekanismRecipe(name, Recipe.CHEMICAL_DISSOLUTION_CHAMBER.get(), recipes));
        } else {
            LogHelper.logWarning(String.format("No %s recipe found for %s and %s. Command ignored!", name, gasOutput.toString(), itemInput.toString()));
        }
    }
}
