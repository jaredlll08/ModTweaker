package modtweaker2.mods.mekanism.handlers;

import static modtweaker2.helpers.InputHelper.toFluid;
import static modtweaker2.mods.mekanism.MekanismHelper.toGas;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import mekanism.common.recipe.RecipeHandler.Recipe;
import mekanism.common.recipe.inputs.FluidInput;
import mekanism.common.recipe.inputs.MachineInput;
import mekanism.common.recipe.machines.MachineRecipe;
import mekanism.common.recipe.machines.SeparatorRecipe;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
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

@ZenClass("mods.mekanism.Separator")
public class Separator {
    
    public static final String name = "Mekanism Separator";
    
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    @SuppressWarnings("unchecked")
    @ZenMethod
    public static void addRecipe(ILiquidStack liquidInput, double energy, IGasStack leftGasOutput, IGasStack rightGasOutput) {
        if(liquidInput == null || leftGasOutput == null || rightGasOutput == null) {
            LogHelper.logError(String.format("Required parameters missing for %s Recipe.", name));
            return;
        }
        
        SeparatorRecipe recipe = new SeparatorRecipe(toFluid(liquidInput), energy, toGas(leftGasOutput), toGas(rightGasOutput));
        
        MineTweakerAPI.apply(new AddMekanismRecipe(name, Recipe.ELECTROLYTIC_SEPARATOR.get(), recipe));
    }
    
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @ZenMethod
    public static void removeRecipe(IIngredient liquidInput, @Optional IIngredient leftGasInput, @Optional IIngredient rightGasInput) {
        if(liquidInput == null) {
            LogHelper.logError(String.format("Required parameters missing for %s Recipe.", name));
            return;
        }
        
        if(leftGasInput == null) leftGasInput = IngredientAny.INSTANCE;
        if(rightGasInput == null) leftGasInput = IngredientAny.INSTANCE;
        
        Map<MachineInput, MachineRecipe> recipes = new HashMap<MachineInput, MachineRecipe>();
        
        for(Entry<FluidInput, SeparatorRecipe> entry : ((Map<FluidInput, SeparatorRecipe>)Recipe.ELECTROLYTIC_SEPARATOR.get()).entrySet() ) {
            ILiquidStack inputLiquid = InputHelper.toILiquidStack(entry.getKey().ingredient);
            IGasStack outputItemLeft = new MCGasStack(entry.getValue().recipeOutput.leftGas);
            IGasStack outputItemRight = new MCGasStack(entry.getValue().recipeOutput.rightGas);
            
            if(!StackHelper.matches(liquidInput, inputLiquid)) continue;
            if(!StackHelper.matches(leftGasInput, outputItemLeft)) continue;
            if(!StackHelper.matches(rightGasInput, outputItemRight)) continue;
            
            recipes.put(entry.getKey(), entry.getValue());
        }
        
        if(!recipes.isEmpty()) {
            MineTweakerAPI.apply(new RemoveMekanismRecipe(name, Recipe.ELECTROLYTIC_SEPARATOR.get(), recipes));
        } else {
            LogHelper.logWarning(String.format("No %s recipe found for %s, %s and %s. Command ignored!", name, liquidInput.toString(), leftGasInput.toString(), rightGasInput.toString()));
        }
    }
}
