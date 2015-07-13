package modtweaker2.mods.mekanism.handlers;

import static modtweaker2.helpers.InputHelper.toFluid;
import static modtweaker2.helpers.InputHelper.toStack;
import static modtweaker2.mods.mekanism.MekanismHelper.toGas;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import mekanism.common.recipe.RecipeHandler.Recipe;
import mekanism.common.recipe.inputs.MachineInput;
import mekanism.common.recipe.inputs.PressurizedInput;
import mekanism.common.recipe.machines.MachineRecipe;
import mekanism.common.recipe.machines.PressurizedRecipe;
import mekanism.common.recipe.outputs.PressurizedOutput;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import minetweaker.api.item.IngredientAny;
import minetweaker.api.liquid.ILiquidStack;
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

@ZenClass("mods.mekanism.Reaction")
public class Reaction {
    
    public static final String name = "Mekanism Reaction";
    
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    @SuppressWarnings("unchecked")
    @ZenMethod
    public static void addRecipe(IItemStack itemInput, ILiquidStack liquidInput, IGasStack gasInput, IItemStack itemOutput, IGasStack gasOutput, double energy, int duration) {
        if(itemInput == null || liquidInput == null || gasInput == null || itemOutput == null || gasOutput == null) {
            LogHelper.logError(String.format("Required parameters missing for %s Recipe.", name));
            return;
        }
        
        PressurizedInput input = new PressurizedInput(toStack(itemInput), toFluid(liquidInput), toGas(gasInput));
        PressurizedOutput output = new PressurizedOutput(toStack(itemOutput), toGas(gasOutput));
        
        PressurizedRecipe recipe = new PressurizedRecipe(input, output, energy, duration);
        
        MineTweakerAPI.apply(new AddMekanismRecipe(name, Recipe.PRESSURIZED_REACTION_CHAMBER.get(), recipe));
    }
    
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @ZenMethod
    public static void removeRecipe(IIngredient itemOutput, IIngredient gasOutput, @Optional IIngredient itemInput, @Optional IIngredient liquidInput, IIngredient gasInput) {
        if(itemOutput == null || gasOutput == null) {
            LogHelper.logError(String.format("Required parameters missing for %s Recipe.", name));
            return;
        }
        
        if(itemInput == null) itemInput = IngredientAny.INSTANCE;
        if(liquidInput == null) liquidInput = IngredientAny.INSTANCE;
        if(gasInput == null) gasInput = IngredientAny.INSTANCE;
        
        Map<MachineInput, MachineRecipe> recipes = new HashMap<MachineInput, MachineRecipe>();
        
        for(Entry<PressurizedInput, PressurizedRecipe> entry : ((Map<PressurizedInput, PressurizedRecipe>)Recipe.PRESSURIZED_REACTION_CHAMBER.get()).entrySet() ) {
            IItemStack inputItem = InputHelper.toIItemStack(entry.getKey().getSolid());
            ILiquidStack inputLiquid = InputHelper.toILiquidStack(entry.getKey().getFluid());
            IGasStack inputGas = new MCGasStack(entry.getKey().getGas());
            IItemStack outputItem = InputHelper.toIItemStack(entry.getValue().recipeOutput.getItemOutput());
            IGasStack outputGas = new MCGasStack(entry.getValue().recipeOutput.getGasOutput());
            
            if(!StackHelper.matches(itemInput, inputItem)) continue;
            if(!StackHelper.matches(liquidInput, inputLiquid)) continue;
            if(!StackHelper.matches(gasInput, inputGas)) continue;
            if(!StackHelper.matches(itemOutput, outputItem)) continue;
            if(!StackHelper.matches(gasOutput, outputGas)) continue;            
            
            recipes.put(entry.getKey(), entry.getValue());
        }
        
        if(!recipes.isEmpty()) {
            MineTweakerAPI.apply(new RemoveMekanismRecipe(name, Recipe.PRESSURIZED_REACTION_CHAMBER.get(), recipes));
        } else {
            LogHelper.logWarning(String.format("No %s recipe found for %s, %s, %s, %s and %s. Command ignored!", name, itemOutput.toString(), gasOutput.toString(), itemInput.toString(), liquidInput.toString(), gasInput.toString()));
        }
    }
    
}
