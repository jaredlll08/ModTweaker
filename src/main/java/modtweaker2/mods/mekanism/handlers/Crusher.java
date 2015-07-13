package modtweaker2.mods.mekanism.handlers;

import static modtweaker2.helpers.InputHelper.toStack;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import mekanism.common.recipe.RecipeHandler.Recipe;
import mekanism.common.recipe.inputs.ItemStackInput;
import mekanism.common.recipe.inputs.MachineInput;
import mekanism.common.recipe.machines.CrusherRecipe;
import mekanism.common.recipe.machines.MachineRecipe;
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

@ZenClass("mods.mekanism.Crusher")
public class Crusher {
    
    public static final String name = "Mekanism Crusher";
    
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    @SuppressWarnings("unchecked")
    @ZenMethod
    public static void addRecipe(IItemStack itemInput, IItemStack itemOutput) {
        if(itemInput == null || itemOutput == null) {
            LogHelper.logError(String.format("Required parameters missing for %s Recipe.", name));
            return;
        }
        
        CrusherRecipe recipe = new CrusherRecipe(toStack(itemInput), toStack(itemOutput));
        
        MineTweakerAPI.apply(new AddMekanismRecipe(name, Recipe.CRUSHER.get(), recipe));
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @ZenMethod
    public static void removeRecipe(IIngredient itemOutput, @Optional IIngredient itemInput) {
        if(itemOutput == null) {
            LogHelper.logError(String.format("Required parameters missing for %s Recipe.", name));
            return;
        }
        
        if(itemInput == null) itemInput = IngredientAny.INSTANCE;
        
        Map<MachineInput, MachineRecipe> recipes = new HashMap<MachineInput, MachineRecipe>();
        
        for(Entry<ItemStackInput, CrusherRecipe> entry : ((Map<ItemStackInput, CrusherRecipe>)Recipe.CRUSHER.get()).entrySet() ) {
            IItemStack inputItem = InputHelper.toIItemStack(entry.getKey().ingredient);
            IItemStack outputItem = InputHelper.toIItemStack(entry.getValue().recipeOutput.output);
            
            if(!StackHelper.matches(itemOutput, outputItem)) continue;
            if(!StackHelper.matches(itemInput, inputItem)) continue;
            
            recipes.put(entry.getKey(), entry.getValue());
        }
        
        if(!recipes.isEmpty()) {
            MineTweakerAPI.apply(new RemoveMekanismRecipe(name, Recipe.CRUSHER.get(), recipes));
        } else {
            LogHelper.logWarning(String.format("No %s recipe found for %s and %s. Command ignored!", name, itemInput.toString(), itemOutput.toString()));
        }
    }
}
