package modtweaker2.mods.mekanism.util;

import java.util.Map;
import java.util.Map.Entry;

import mekanism.common.recipe.inputs.MachineInput;
import mekanism.common.recipe.machines.CombinerRecipe;
import mekanism.common.recipe.machines.CrusherRecipe;
import mekanism.common.recipe.machines.CrystallizerRecipe;
import mekanism.common.recipe.machines.MachineRecipe;
import mekanism.common.recipe.outputs.ItemStackOutput;
import mekanism.common.recipe.outputs.MachineOutput;
import modtweaker2.helpers.LogHelper;
import modtweaker2.utils.BaseMapRemoval;

@SuppressWarnings("rawtypes")
public class RemoveMekanismRecipe extends BaseMapRemoval<MachineInput, MachineRecipe> {
    public RemoveMekanismRecipe(String name, Map<MachineInput, MachineRecipe> map, Map<MachineInput, MachineRecipe> recipes) {
		super(name, map, recipes);
	}
    
    @Override
    protected String getRecipeInfo(Entry<MachineInput, MachineRecipe> recipe) {
        MachineRecipe machineRecipe = recipe.getValue();
        MachineOutput output = recipe.getValue().recipeOutput;
        
        if(machineRecipe instanceof CombinerRecipe) {
            return LogHelper.getStackDescription(((ItemStackOutput)output).output);
        } else if (machineRecipe instanceof CrusherRecipe) {
            return LogHelper.getStackDescription(((ItemStackOutput)output).output);
        } else if (machineRecipe instanceof CrystallizerRecipe) {
            return LogHelper.getStackDescription(((ItemStackOutput)output).output);
        }
        
        return null;
    }
}
