package modtweaker2.mods.mekanism.handlers;

import static modtweaker2.helpers.InputHelper.toStack;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import mekanism.common.recipe.RecipeHandler.Recipe;
import mekanism.common.recipe.inputs.ItemStackInput;
import mekanism.common.recipe.inputs.MachineInput;
import mekanism.common.recipe.machines.MachineRecipe;
import mekanism.common.recipe.machines.SawmillRecipe;
import mekanism.common.recipe.outputs.ChanceOutput;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import minetweaker.api.item.IngredientAny;
import modtweaker2.helpers.InputHelper;
import modtweaker2.helpers.LogHelper;
import modtweaker2.helpers.StackHelper;
import modtweaker2.mods.mekanism.util.AddMekanismRecipe;
import modtweaker2.mods.mekanism.util.RemoveMekanismRecipe;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.mekanism.Sawmill")
public class Sawmill {
    
    public static final String name = "Mekanism Sawmill";
    
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    @SuppressWarnings("unchecked")
    @ZenMethod
    public static void addRecipe(IItemStack itemInput, IItemStack itemOutput, @Optional IItemStack optionalItemOutput, @Optional double optionalChance) {
        if(itemInput == null || itemOutput == null) {
            LogHelper.logError(String.format("Required parameters missing for %s Recipe.", name));
            return;
        }
        
        ItemStackInput input = new ItemStackInput(toStack(itemInput));
        ChanceOutput output = optionalItemOutput == null ? new ChanceOutput(toStack(itemOutput)) :
            new ChanceOutput(toStack(itemOutput), toStack(optionalItemOutput), optionalChance);
        
        SawmillRecipe recipe = new SawmillRecipe(input, output);
        
        MineTweakerAPI.apply(new AddMekanismRecipe(name, Recipe.PRECISION_SAWMILL.get(), recipe));
    }
    
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static void removeRecipe(IIngredient itemInput, @Optional IIngredient itemOutput, @Optional IIngredient optionalItemOutput) {
        if(itemInput == null) {
            LogHelper.logError(String.format("Required parameters missing for %s Recipe.", name));
            return;
        }
        
        if(itemOutput == null) itemOutput = IngredientAny.INSTANCE;
        if(optionalItemOutput == null) optionalItemOutput = IngredientAny.INSTANCE;
        
        Map<MachineInput, MachineRecipe> recipes = new HashMap<MachineInput, MachineRecipe>();
        
        for(Entry<ItemStackInput, SawmillRecipe> entry : ((Map<ItemStackInput, SawmillRecipe>)Recipe.PRECISION_SAWMILL.get()).entrySet() ) {
            IItemStack inputItem = InputHelper.toIItemStack(entry.getKey().ingredient);
            IItemStack outputItem = InputHelper.toIItemStack(entry.getValue().recipeOutput.primaryOutput);
            IItemStack outputItemOptional = InputHelper.toIItemStack(entry.getValue().recipeOutput.secondaryOutput);
            
            if(!StackHelper.matches(itemOutput, outputItem)) continue;
            if(!StackHelper.matches(itemInput, inputItem)) continue;
            if(!StackHelper.matches(optionalItemOutput, outputItemOptional)) continue;
            
            recipes.put(entry.getKey(), entry.getValue());
        }
        
        if(!recipes.isEmpty()) {
            MineTweakerAPI.apply(new RemoveMekanismRecipe(name, Recipe.PRECISION_SAWMILL.get(), recipes));
        } else {
            LogHelper.logWarning(String.format("No %s recipe found for %s and %s. Command ignored!", name, itemInput.toString(), itemOutput.toString()));
        }
    }
}
