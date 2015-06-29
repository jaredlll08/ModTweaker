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

import com.bioxx.tfc.api.Crafting.QuernManager;
import com.bioxx.tfc.api.Crafting.QuernRecipe;

@ZenClass("mods.tfcraft.Quern")
public class Quern {
    
    public static final String name = "TFC Quern";
    
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@ZenMethod
	public static void add(IItemStack input, IItemStack output) {
		MineTweakerAPI.apply(new AddRecipe(new QuernRecipe(toStack(input), toStack(output))));
	}
	
	private static class AddRecipe extends BaseListAddition<QuernRecipe> {
		public AddRecipe(QuernRecipe recipe) {
			super(Quern.name, TFCHelper.quernRecipes);
			recipes.add(recipe);
		}
		
		@Override
		public void apply() {
		    super.apply();
		    
            for (QuernRecipe recipe : successful){
                TFCHelper.quernVaildItems.add(recipe.getInItem());
            }
		}
		
		@Override
		public void undo() {
            for (QuernRecipe recipe : successful){
                TFCHelper.quernVaildItems.remove(recipe.getInItem());
            }
            
            super.undo();
		}
		
		@Override
		protected String getRecipeInfo(QuernRecipe recipe) {
		    return InputHelper.getStackDescription(recipe.getResult());
		}
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
    @ZenMethod
    public static void remove(IIngredient output) {
        List<QuernRecipe> recipes = new LinkedList<QuernRecipe>();
        
        for (QuernRecipe recipe : QuernManager.getInstance().getRecipes()) {
            if (recipe != null && recipe.getResult() != null && matches(output, toIItemStack(recipe.getResult()))){
                recipes.add(recipe);
            }
        }
        
        MineTweakerAPI.apply(new RemoveRecipe(recipes));
    }
	
	private static class RemoveRecipe extends BaseListRemoval<QuernRecipe> {
		public RemoveRecipe(List<QuernRecipe> recipes) {
			super(Quern.name, TFCHelper.quernRecipes, recipes);
		}

		@Override
		public void apply() {
			for (QuernRecipe recipe : recipes) {
			    TFCHelper.quernVaildItems.remove(recipe.getInItem());
			}
			
			super.apply();
		}
		
        @Override
        public void undo() {
            super.undo();

            for (QuernRecipe recipe : successful){
                TFCHelper.quernVaildItems.add(recipe.getInItem());
            }
        }
        
        @Override
        protected String getRecipeInfo(QuernRecipe recipe) {
            return InputHelper.getStackDescription(recipe.getResult());
        }
	}
	
}
