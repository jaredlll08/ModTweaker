package modtweaker2.mods.tfcraft.handlers;

import static com.bioxx.tfc.api.Crafting.AnvilReq.getReqFromInt;
import static modtweaker2.helpers.InputHelper.toIItemStack;
import static modtweaker2.helpers.InputHelper.toStack;
import static modtweaker2.helpers.StackHelper.matches;

import java.util.LinkedList;
import java.util.List;

import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import modtweaker2.helpers.LogHelper;
import modtweaker2.mods.tfcraft.TFCHelper;
import modtweaker2.utils.BaseListAddition;
import modtweaker2.utils.BaseListRemoval;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import com.bioxx.tfc.api.Crafting.AnvilManager;
import com.bioxx.tfc.api.Crafting.AnvilRecipe;


@ZenClass("mods.tfcraft.Anvil")
public class Anvil {
    
    public static final String nameRegular = "TFC Anvil-Regular";
    public static final String nameWeld = "TFC Anvil-Weld";
	
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
	@ZenMethod
	public static void add(IItemStack input1, IItemStack input2, String plan, int value, boolean flux, int req, IItemStack output){
	    addRecipe(nameRegular, TFCHelper.anvilRecipes, new AnvilRecipe(toStack(input1), toStack(input2), plan, value, flux, req, toStack(output)));
	}
	
	/**
	 * For parameter <code>req</code>:<p> 1==copper;<p> 2==bronze;<p> 3==Wrought Iron;<p>
	 * 4==Steel;<p> 5==Black Steel;<p> 6==Red Steel;<p> 7==Blue Steel;<p> default is stone.
	 */
	@ZenMethod
	public static void addWeld(IItemStack input1, IItemStack input2, int req, IItemStack output){
		addRecipe(nameWeld, TFCHelper.anvilWeldRecipes, new AnvilRecipe(toStack(input1), toStack(input2), true, getReqFromInt(req), toStack(output)));
	}
	
    public static void addRecipe(String name, List<AnvilRecipe> list, AnvilRecipe recipe) {
        List<AnvilRecipe> recipes = new LinkedList<AnvilRecipe>();
        recipes.add(recipe);
        MineTweakerAPI.apply(new Add(name, list, recipes));
    }

	private static class Add extends BaseListAddition<AnvilRecipe> {
		public Add(String name, List<AnvilRecipe> list, List<AnvilRecipe> recipes) {
			super(name, list, recipes);
		}
		
        @Override
        protected String getRecipeInfo(AnvilRecipe recipe) {
            return LogHelper.getStackDescription(recipe.getCraftingResult());
        }
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
    @ZenMethod
    public static void remove(IIngredient ingredient) {
        removeRecipe(nameRegular, TFCHelper.anvilRecipes, ingredient);
    }
    
    @ZenMethod
    public static void removeWeld(IIngredient ingredient) {
        removeRecipe(nameWeld, TFCHelper.anvilWeldRecipes, ingredient);
    }

	
	public static void removeRecipe(String name, List<AnvilRecipe> list, IIngredient ingredient) {
	    List<AnvilRecipe> recipes = new LinkedList<AnvilRecipe>();
	    
        for (AnvilRecipe recipe : AnvilManager.getInstance().getRecipeList()){
            if (recipe.getCraftingResult() != null && matches(ingredient, toIItemStack(recipe.getCraftingResult()))) {
                recipes.add(recipe);
            }
        }

        if(!recipes.isEmpty()) {
            MineTweakerAPI.apply(new Remove(name, list, recipes));
        } else {
            LogHelper.logWarning(String.format("No %s Recipes found for %s. Command ignored.", name, ingredient.toString()));
        }
	}
	
	private static class Remove extends BaseListRemoval<AnvilRecipe> {
		public Remove(String name, List<AnvilRecipe> list, List<AnvilRecipe> recipes){
			super(name, list, recipes);
		}
		
		@Override
		protected String getRecipeInfo(AnvilRecipe recipe) {
		    return LogHelper.getStackDescription(recipe.getCraftingResult());
		}
	}
}
