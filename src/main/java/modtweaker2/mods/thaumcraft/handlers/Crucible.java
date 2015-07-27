package modtweaker2.mods.thaumcraft.handlers;

import static modtweaker2.helpers.InputHelper.toIItemStack;
import static modtweaker2.helpers.InputHelper.toObject;
import static modtweaker2.helpers.InputHelper.toObjects;
import static modtweaker2.helpers.InputHelper.toStack;
import static modtweaker2.helpers.StackHelper.matches;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import modtweaker2.helpers.LogHelper;
import modtweaker2.mods.thaumcraft.ThaumcraftHelper;
import modtweaker2.mods.thaumcraft.aspect.AspectStack;
import modtweaker2.mods.thaumcraft.aspect.IAspectStack;
import modtweaker2.utils.BaseListAddition;
import modtweaker2.utils.BaseListRemoval;
import scala.actors.threadpool.Arrays;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.crafting.CrucibleRecipe;

@ZenClass("mods.thaumcraft.Crucible")
public class Crucible {
    
    public static final String name = "Thaumcraft Crucible";
    
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    @Deprecated
    @ZenMethod
    public static void addRecipe(String key, IItemStack result, IIngredient catalyst, String aspects) {
   		MineTweakerAPI.apply(new Add(new CrucibleRecipe(key, toStack(result), toObject(catalyst), ThaumcraftHelper.parseAspects(aspects))));
  	}

    @ZenMethod
    public static void addRecipe(String key, IItemStack itemOutput, IIngredient itemInput, IAspectStack[] aspectInput) {
        addRecipe(key, itemOutput, new IIngredient[] {itemInput}, aspectInput);
    }
    
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@ZenMethod
	public static void addRecipe(String key, IItemStack itemOutput, IIngredient[] itemInput, IAspectStack[] aspectInput) {
	    if(key == null || itemOutput == null || itemInput == null || itemInput.length == 0 || aspectInput == null || aspectInput.length == 0) {
            LogHelper.logError(String.format("Required parameters missing for %s Recipe.", name));
	        return;
	    }
	    
	    Object input;
	    
	    if(itemInput.length == 1) {
	        input = toObject(itemInput[0]);
	    } else {
	        input = new ArrayList(Arrays.asList(toObjects(itemInput)));
	    }
	    
	    MineTweakerAPI.apply(new Add(new CrucibleRecipe(key, toStack(itemOutput), input, AspectStack.join(ThaumcraftHelper.toStacks(aspectInput)))));
	}
    
	private static class Add extends BaseListAddition<CrucibleRecipe> {
	    
		@SuppressWarnings("unchecked")
        public Add(CrucibleRecipe recipe) {
			super(Crucible.name, ThaumcraftApi.getCraftingRecipes());
			recipes.add(recipe);
		}
		
		@Override
		protected String getRecipeInfo(CrucibleRecipe recipe) {
		    return LogHelper.getStackDescription(recipe.getRecipeOutput());
		}
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@ZenMethod
	public static void removeRecipe(IIngredient output) {
	    List<CrucibleRecipe> recipes = new LinkedList<CrucibleRecipe>();
	    
        for (Object o : ThaumcraftApi.getCraftingRecipes()) {
            if (o instanceof CrucibleRecipe) {
                CrucibleRecipe r = (CrucibleRecipe) o;
                if (r.getRecipeOutput() != null && matches(output, toIItemStack(r.getRecipeOutput()))) {
                    recipes.add(r);
                }
            }
        }
        
        if(!recipes.isEmpty()) {
			MineTweakerAPI.apply(new Remove(recipes));
        } else {
            LogHelper.logWarning(String.format("No %s Recipe found for %s. Command ignored!", Crucible.name, output.toString()));
        }
	}

	private static class Remove extends BaseListRemoval<CrucibleRecipe> {
	    
		@SuppressWarnings("unchecked")
        public Remove(List<CrucibleRecipe> recipes) {
			super(Crucible.name, ThaumcraftApi.getCraftingRecipes(), recipes);
		}

        @Override
        protected String getRecipeInfo(CrucibleRecipe recipe) {
            return LogHelper.getStackDescription(recipe.getRecipeOutput());
        }
	}
}
