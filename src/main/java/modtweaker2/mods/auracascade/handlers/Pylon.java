package modtweaker2.mods.auracascade.handlers;

import static modtweaker2.helpers.InputHelper.toIItemStack;
import static modtweaker2.helpers.InputHelper.toStack;

import java.util.LinkedList;
import java.util.List;

import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import modtweaker2.helpers.LogHelper;
import modtweaker2.mods.auracascade.AuraCascadeHelper;
import modtweaker2.mods.auracascade.aura.IAuraStack;
import modtweaker2.utils.BaseListAddition;
import modtweaker2.utils.BaseListRemoval;
import pixlepix.auracascade.data.recipe.PylonRecipe;
import pixlepix.auracascade.data.recipe.PylonRecipeComponent;
import pixlepix.auracascade.data.recipe.PylonRecipeRegistry;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.auraCascade.Pylon")
public class Pylon {
    
    protected static final String name = "Aura Cascade Pylon";

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
	@ZenMethod
	public static void addRecipe(IItemStack ouput, IAuraStack stack, IItemStack input) {
		addRecipe(ouput, stack, input, stack, input, stack, input, stack, input);
	}

	@ZenMethod
	public static void addRecipe(IItemStack ouput, IAuraStack aura1, IItemStack input1, IAuraStack aura2, IItemStack input2, IAuraStack aura3, IItemStack input3, IAuraStack aura4, IItemStack input4) {
	    List<PylonRecipe> recipes = new LinkedList<PylonRecipe>();
	    
	    recipes.add(new PylonRecipe(toStack(ouput), new PylonRecipeComponent(AuraCascadeHelper.toAura(aura1), toStack(input1)), new PylonRecipeComponent(AuraCascadeHelper.toAura(aura2), toStack(input2)), new PylonRecipeComponent(AuraCascadeHelper.toAura(aura3), toStack(input3)), new PylonRecipeComponent(AuraCascadeHelper.toAura(aura1), toStack(input4))));
	    
		MineTweakerAPI.apply(new Add(recipes));
	}

	private static class Add extends BaseListAddition<PylonRecipe> {

		public Add(List<PylonRecipe> recipes) {
			super(Pylon.name, PylonRecipeRegistry.recipes, recipes);
		}
		
        @Override
        protected String getRecipeInfo(PylonRecipe recipe) {
            return LogHelper.getStackDescription(recipe.result);
        }
	}
	
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@ZenMethod
	public static void removeRecipe(IIngredient output) {
        List<PylonRecipe> recipes = new LinkedList<PylonRecipe>();
        
        for (PylonRecipe r : PylonRecipeRegistry.recipes) {
            if (output.matches(toIItemStack(r.result))) {
                recipes.add(r);
            }
        }
	    
        // Check if we found the recipes and apply the action
        if(!recipes.isEmpty()) {
            MineTweakerAPI.apply(new Remove(recipes));
        } else {
            LogHelper.logWarning(String.format("No %s Recipe found for %s. Command ignored!", Pylon.name, output.toString()));
        }
	}

	private static class Remove extends BaseListRemoval<PylonRecipe> {
		public Remove(List<PylonRecipe> recipes) {
			super(Pylon.name, PylonRecipeRegistry.recipes, recipes);
		}

        @Override
        protected String getRecipeInfo(PylonRecipe recipe) {
            return LogHelper.getStackDescription(recipe.result);
        }
	}
}
