package modtweaker2.mods.thaumcraft.handlers;

import static modtweaker2.helpers.InputHelper.toIItemStack;
import static modtweaker2.helpers.InputHelper.toObjects;
import static modtweaker2.helpers.InputHelper.toShapedObjects;
import static modtweaker2.helpers.InputHelper.toStack;
import static modtweaker2.helpers.StackHelper.matches;

import java.util.LinkedList;
import java.util.List;

import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import modtweaker2.helpers.InputHelper;
import modtweaker2.helpers.LogHelper;
import modtweaker2.mods.thaumcraft.ThaumcraftHelper;
import modtweaker2.utils.BaseListAddition;
import modtweaker2.utils.BaseListRemoval;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.crafting.IArcaneRecipe;
import thaumcraft.api.crafting.ShapedArcaneRecipe;
import thaumcraft.api.crafting.ShapelessArcaneRecipe;

@ZenClass("mods.thaumcraft.Arcane")
public class Arcane {
    
    public static final String name = "Thaumcraft Arcane Worktable";
    
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
	@ZenMethod
	public static void addShaped(String key, IItemStack output, String aspects, IIngredient[][] ingredients) {
		MineTweakerAPI.apply(new Add(new ShapedArcaneRecipe(key, toStack(output), ThaumcraftHelper.parseAspects(aspects), toShapedObjects(ingredients))));
	}

	@ZenMethod
	public static void addShapeless(String key, IItemStack output, String aspects, IIngredient[] ingredients) {
	    MineTweakerAPI.apply(new Add(new ShapelessArcaneRecipe(key, toStack(output), ThaumcraftHelper.parseAspects(aspects), toObjects(ingredients))));
	}

	private static class Add extends BaseListAddition<IArcaneRecipe> {
		public Add(IArcaneRecipe recipe) {
			super(Arcane.name, ThaumcraftApi.getCraftingRecipes());
			recipes.add(recipe);
		}

		@Override
		protected String getRecipeInfo(IArcaneRecipe recipe) {
		    ItemStack stack = recipe.getRecipeOutput();
		    
		    if(stack instanceof ItemStack)
		        return InputHelper.getStackDescription(stack);
		    else
		        return "Unknown output";
		}
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@ZenMethod
	public static void removeRecipe(IIngredient output) {
	    List<IArcaneRecipe> recipes = new LinkedList<IArcaneRecipe>();
	    
	    for(Object o : ThaumcraftApi.getCraftingRecipes()) {
	        if(o != null && o instanceof IArcaneRecipe) {
	            IArcaneRecipe recipe = (IArcaneRecipe)o;
	            
	            if(recipe.getRecipeOutput() != null && matches(output, toIItemStack(recipe.getRecipeOutput()))) {
	                recipes.add(recipe);
	            }
	        }
	    }
	    
	    if(!recipes.isEmpty()) {
	        MineTweakerAPI.apply(new Remove(recipes));
	    } else {
	        LogHelper.logWarning(String.format("No %s Recipe found for %s. Command Ignored", Arcane.name, output.toString()));
	    }
	}

	private static class Remove extends BaseListRemoval<IArcaneRecipe> {
		public Remove(List<IArcaneRecipe> recipes) {
			super(Arcane.name, ThaumcraftApi.getCraftingRecipes(), recipes);
		}

        @Override
        protected String getRecipeInfo(IArcaneRecipe recipe) {
            ItemStack stack = recipe.getRecipeOutput();
            
            if(stack instanceof ItemStack)
                return InputHelper.getStackDescription(stack);
            else
                return "Unknown output";
        }
	}
}
