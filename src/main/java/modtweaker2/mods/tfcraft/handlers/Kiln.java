package modtweaker2.mods.tfcraft.handlers;

import static modtweaker2.helpers.InputHelper.toIItemStack;
import static modtweaker2.helpers.InputHelper.toStack;
import static modtweaker2.helpers.StackHelper.matches;

import java.util.LinkedList;
import java.util.List;

import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import modtweaker2.helpers.InputHelper;
import modtweaker2.mods.tfcraft.TFCHelper;
import modtweaker2.utils.BaseListAddition;
import modtweaker2.utils.BaseListRemoval;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import com.bioxx.tfc.api.Crafting.KilnCraftingManager;
import com.bioxx.tfc.api.Crafting.KilnRecipe;

@ZenClass("mods.tfcraft.Kiln")
public class Kiln {
    
    public static final String name = "TFC Kiln";

	/** Deprecated, since pottery kiln will check <code>x instanceof ItemPotteryBase</code>
	 *  <p>If you have a such item, you might consider to use it
	 */
	@Deprecated
	@ZenMethod
	public static void add(IItemStack input, int lv, IItemStack output){
		MineTweakerAPI.apply(new AddRecipe(new KilnRecipe(toStack(input), lv, toStack(output))));
	}

	private static class AddRecipe extends BaseListAddition<KilnRecipe> {	
		public AddRecipe(KilnRecipe recipe) {
			super(Kiln.name, TFCHelper.kilnRecipes);
			recipes.add(recipe);
		}
		
		@Override
		protected String getRecipeInfo(KilnRecipe recipe) {
		    return InputHelper.getStackDescription(recipe.getCraftingResult());
		}
	}

	@ZenMethod
    public static void remove(IIngredient output){
	    List<KilnRecipe> recipes = new LinkedList<KilnRecipe>();
	    
        for (KilnRecipe recipe : KilnCraftingManager.getInstance().getRecipeList()){
            if (recipe.getCraftingResult() != null && matches(output, toIItemStack(recipe.getCraftingResult()))) {
                recipes.add(recipe);
            }
        }
        
        MineTweakerAPI.apply(new RemoveRecipe(recipes));
    }
   
	private static class RemoveRecipe extends BaseListRemoval<KilnRecipe> {
		public RemoveRecipe(List<KilnRecipe> recipes) {
			super(Kiln.name, TFCHelper.kilnRecipes, recipes);
		}
        
        @Override
        protected String getRecipeInfo(KilnRecipe recipe) {
            return InputHelper.getStackDescription(recipe.getCraftingResult());
        }
	}
		
}
