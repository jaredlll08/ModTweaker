package modtweaker2.mods.mekanism.handlers;

import static modtweaker2.helpers.InputHelper.toStack;
import static modtweaker2.mods.mekanism.MekanismHelper.toGas;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import mekanism.common.recipe.RecipeHandler.Recipe;
import mekanism.common.recipe.inputs.ItemStackInput;
import mekanism.common.recipe.inputs.MachineInput;
import mekanism.common.recipe.machines.MachineRecipe;
import mekanism.common.recipe.machines.OxidationRecipe;
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

@ZenClass("mods.mekanism.chemical.Oxidizer")
public class ChemicalOxidizer {
    
    public static final String name = "Mekanism Chemical Oxidizer";
    
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    @SuppressWarnings("unchecked")
    @ZenMethod
    public static void addRecipe(IItemStack itemInput, IGasStack gasOutput) {
        if(itemInput == null || gasOutput == null) {
            LogHelper.logError(String.format("Required parameters missing for %s Recipe.", name));
            return;
        }
        
        OxidationRecipe recipe = new OxidationRecipe(toStack(itemInput), toGas(gasOutput));
        
        MineTweakerAPI.apply(new AddMekanismRecipe(name, Recipe.CHEMICAL_OXIDIZER.get(), recipe));
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
        
        for(Entry<ItemStackInput, OxidationRecipe> entry : ((Map<ItemStackInput, OxidationRecipe>)Recipe.CHEMICAL_OXIDIZER.get()).entrySet()) {
            IItemStack inputItem = InputHelper.toIItemStack(entry.getKey().ingredient);
            IGasStack outputGas = new MCGasStack(entry.getValue().recipeOutput.output);
            
            if(!StackHelper.matches(itemInput, inputItem)) continue;
            if(!StackHelper.matches(gasOutput, outputGas)) continue;
            
            recipes.put(entry.getKey(), entry.getValue());
        }
        
        if(!recipes.isEmpty()) {
            MineTweakerAPI.apply(new RemoveMekanismRecipe(name, Recipe.CHEMICAL_OXIDIZER.get(), recipes));
        } else {
            LogHelper.logWarning(String.format("No %s recipe found for %s and %s. Command ignored!", name, gasOutput.toString(), itemInput.toString()));
        }
    }
}
