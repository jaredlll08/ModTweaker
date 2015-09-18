package modtweaker2.mods.mekanism.handlers;

import static modtweaker2.helpers.InputHelper.toStack;
import static modtweaker2.mods.mekanism.MekanismHelper.toGas;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import mekanism.api.gas.GasStack;
import mekanism.common.recipe.RecipeHandler.Recipe;
import mekanism.common.recipe.inputs.AdvancedMachineInput;
import mekanism.common.recipe.inputs.MachineInput;
import mekanism.common.recipe.machines.CombinerRecipe;
import mekanism.common.recipe.machines.MachineRecipe;
import mekanism.common.recipe.outputs.ItemStackOutput;
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

@ZenClass("mods.mekanism.Combiner")
public class Combiner {
    
    public static final String name = "Mekanism Combiner";
    
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @SuppressWarnings({ "unchecked" })
    @ZenMethod
    public static void addRecipe(IItemStack itemInput, IGasStack gasInput, IItemStack itemOutput) {
        if(itemInput == null || gasInput == null || itemOutput == null) {
            LogHelper.logError(String.format("Required parameters missing for %s Recipe.", name));
            return;
        }
        
        AdvancedMachineInput input = new AdvancedMachineInput(toStack(itemInput), toGas(gasInput).getGas());
        ItemStackOutput output = new ItemStackOutput(toStack(itemOutput));
        CombinerRecipe recipe = new CombinerRecipe(input, output);
        
        MineTweakerAPI.apply(new AddMekanismRecipe(name, Recipe.COMBINER.get(), recipe));
    }
    
    @SuppressWarnings("unchecked")
    @ZenMethod
    public static void addRecipe(IItemStack itemInput, IItemStack itemOutput) {
        if(itemInput == null || itemOutput == null) {
            LogHelper.logError(String.format("Required parameters missing for %s Recipe.", name));
            return;
        }
        
        CombinerRecipe recipe = new CombinerRecipe(toStack(itemInput), toStack(itemOutput));
        
        MineTweakerAPI.apply(new AddMekanismRecipe(name, Recipe.COMBINER.get(), recipe));
    }
    
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @ZenMethod
    public static void removeRecipe(IIngredient itemOutput, @Optional IIngredient itemInput, @Optional IIngredient gasInput) {
        if(itemOutput == null) {
            LogHelper.logError(String.format("Required parameters missing for %s Recipe.", name));
            return;
        }
        
        if(gasInput == null) gasInput = IngredientAny.INSTANCE;
        if(itemInput == null) itemInput = IngredientAny.INSTANCE;
        
        Map<MachineInput, MachineRecipe> recipes = new HashMap<MachineInput, MachineRecipe>();
        
        for(Entry<AdvancedMachineInput, CombinerRecipe> entry : ((Map<AdvancedMachineInput, CombinerRecipe>)Recipe.COMBINER.get()).entrySet()) {
            IItemStack inputItem = InputHelper.toIItemStack(entry.getKey().itemStack);
            IGasStack inputGas = new MCGasStack(new GasStack(entry.getKey().gasType, 1));
            IItemStack outputItem = InputHelper.toIItemStack(entry.getValue().getOutput().output);
            
            if(!StackHelper.matches(itemInput, inputItem)) continue;
            if(!StackHelper.matches(gasInput, inputGas)) continue;
            if(!StackHelper.matches(itemOutput, outputItem)) continue;
            
            recipes.put(entry.getKey(), entry.getValue());
        }
        
        if(!recipes.isEmpty()) {
            MineTweakerAPI.apply(new RemoveMekanismRecipe(name, Recipe.COMBINER.get(), recipes));
        } else {
            LogHelper.logWarning(String.format("No %s recipe found for %s, %s and %s. Command ignored!", name, itemInput.toString(), gasInput.toString(), itemOutput.toString()));
        }
    }
}
