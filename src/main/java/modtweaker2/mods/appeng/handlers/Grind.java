package modtweaker2.mods.appeng.handlers;

import static modtweaker2.helpers.InputHelper.getStackDescription;
import static modtweaker2.helpers.InputHelper.toIItemStack;
import static modtweaker2.helpers.InputHelper.toStack;
import static modtweaker2.helpers.StackHelper.matches;

import java.util.LinkedList;

import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import modtweaker2.helpers.InputHelper;
import modtweaker2.helpers.LogHelper;
import modtweaker2.mods.appeng.AppliedEnergisticsHelper;
import modtweaker2.utils.BaseListAddition;
import modtweaker2.utils.BaseListRemoval;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import appeng.api.AEApi;
import appeng.api.features.IGrinderEntry;
import appeng.core.features.registries.entries.AppEngGrinderRecipe;

@ZenClass("mods.appeng.Grinder")
public class Grind {
    
    protected static final String name = "Applied Energistics 2 Grinder";

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    @ZenMethod
	public static void addRecipe(IItemStack input, IItemStack output, int energy, @Optional IItemStack output2, @Optional float chance2, @Optional IItemStack output3, @Optional float chance3) {
        // Create recipe
        IGrinderEntry recipe;
        
        if(output2 != null && output3 != null)
            recipe = new AppEngGrinderRecipe(InputHelper.toStack(input), InputHelper.toStack(output), InputHelper.toStack(output2), InputHelper.toStack(output3), chance2, chance3, energy);
        else if(output2 != null)
            recipe = new AppEngGrinderRecipe(InputHelper.toStack(input), InputHelper.toStack(output), InputHelper.toStack(output2), chance2, energy);
        else
            recipe = new AppEngGrinderRecipe(InputHelper.toStack(input), InputHelper.toStack(output), energy);
        
        // Check if the recipe is already present, we don't want to add duplicates
        for(IGrinderEntry r : AEApi.instance().registries().grinder().getRecipes()) {
            if(r != null && AppliedEnergisticsHelper.equals(r, recipe)) {
                LogHelper.logWarning(String.format("Duplicate %s Recipe found for %s. Command ignored!", Grind.name, InputHelper.getStackDescription(toStack(input))));
                return;
            }
        }

		MineTweakerAPI.apply(new Add(recipe));
	}

	private static class Add extends BaseListAddition<IGrinderEntry> {

		public Add(IGrinderEntry recipe) {
			super(Grind.name, AEApi.instance().registries().grinder().getRecipes());
			recipes.add(recipe);
		}

        @Override
        public String getRecipeInfo(IGrinderEntry recipe) {
            return getStackDescription(recipe.getInput());
        }
        
        @Override
        protected boolean equals(IGrinderEntry recipe, IGrinderEntry otherRecipe) {
            return AppliedEnergisticsHelper.equals(recipe, otherRecipe);
        }
	}
	
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


	@ZenMethod
	public static void removeRecipe(IIngredient output) {
	    // Get list of existing recipes, matching with parameter
	    LinkedList<IGrinderEntry> result = new LinkedList<IGrinderEntry>();
	    
        for(IGrinderEntry entry : AEApi.instance().registries().grinder().getRecipes()) {
            if(entry != null && entry.getOutput() != null && matches(output, toIItemStack(entry.getOutput()))) {
                result.add(entry);
            }
        }
	    
        // Check if we found the recipes and apply the action
	    if(!result.isEmpty()) {
	        MineTweakerAPI.apply(new Remove(result));
	    } else {
	        LogHelper.logWarning(String.format("No %s Recipe found for %s. Command ignored!", Grind.name, output.toString()));
	    }
	}

	private static class Remove extends BaseListRemoval<IGrinderEntry> {
		public Remove(LinkedList<IGrinderEntry> recipes) {
			super(Grind.name, AEApi.instance().registries().grinder().getRecipes(), recipes);
		}

		@Override
		public String getRecipeInfo(IGrinderEntry recipe) {
		    return getStackDescription(recipe.getInput());
		}
	}

}