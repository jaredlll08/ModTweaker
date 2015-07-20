package modtweaker2.mods.mekanism.handlers;

import static modtweaker2.helpers.InputHelper.toStack;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import mekanism.api.infuse.InfuseRegistry;
import mekanism.common.recipe.RecipeHandler.Recipe;
import mekanism.common.recipe.inputs.InfusionInput;
import mekanism.common.recipe.inputs.MachineInput;
import mekanism.common.recipe.machines.MachineRecipe;
import mekanism.common.recipe.machines.MetallurgicInfuserRecipe;
import mekanism.common.recipe.outputs.ItemStackOutput;
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

@ZenClass("mods.mekanism.Infuser")
public class Infuser {
    
    public static final String name = "Mekanism Infuser";
    
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    @SuppressWarnings("unchecked")
    @ZenMethod
    public static void addRecipe(String infuseType, int infuseAmount, IItemStack itemInput, IItemStack itemOutput) {
        if(itemInput == null || itemOutput == null || infuseType == null || infuseType.isEmpty()) {
            LogHelper.logError(String.format("Required parameters missing for %s Recipe.", name));
            return;
        }
        
        InfusionInput input = new InfusionInput(InfuseRegistry.get(infuseType), infuseAmount, toStack(itemInput));
        ItemStackOutput output = new ItemStackOutput(toStack(itemOutput));
        
        MetallurgicInfuserRecipe recipe = new MetallurgicInfuserRecipe(input, output);
        
        MineTweakerAPI.apply(new AddMekanismRecipe(name, Recipe.METALLURGIC_INFUSER.get(), recipe));
    }
    
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @ZenMethod
    public static void removeRecipe(IIngredient itemOutput, @Optional IIngredient itemInput, @Optional String infuseType) {
        if(itemOutput == null) {
            LogHelper.logError(String.format("Required parameters missing for %s Recipe.", name));
            return;
        }
        
        if(itemInput == null) itemInput = IngredientAny.INSTANCE;
        if(infuseType == null) infuseType = "";
        
        Map<MachineInput, MachineRecipe> recipes = new HashMap<MachineInput, MachineRecipe>();
        
        for(Entry<InfusionInput, MetallurgicInfuserRecipe> entry : ((Map<InfusionInput, MetallurgicInfuserRecipe>)Recipe.METALLURGIC_INFUSER.get()).entrySet() ) {
            IItemStack inputItem = InputHelper.toIItemStack(entry.getKey().inputStack);
            String typeInfuse = entry.getKey().infuse.type.name;
            IItemStack outputItem = InputHelper.toIItemStack(entry.getValue().recipeOutput.output);
            
            if(!StackHelper.matches(itemOutput, outputItem)) continue;
            if(!StackHelper.matches(itemInput, inputItem)) continue;
            if(!infuseType.isEmpty() && !infuseType.equalsIgnoreCase(typeInfuse)) continue;
            
            recipes.put(entry.getKey(), entry.getValue());
        }
        
        if(!recipes.isEmpty()) {
            MineTweakerAPI.apply(new RemoveMekanismRecipe(name, Recipe.METALLURGIC_INFUSER.get(), recipes));
        } else {
            LogHelper.logWarning(String.format("No %s recipe found for %s and %s. Command ignored!", name, itemInput.toString(), itemOutput.toString()));
        }
    }
}
