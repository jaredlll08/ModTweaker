package modtweaker2.utils;

import static modtweaker2.helpers.InputHelper.toIItemStack;
import static modtweaker2.helpers.StackHelper.matches;

import java.util.LinkedList;
import java.util.List;

import minetweaker.api.item.IIngredient;
import modtweaker2.helpers.LogHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;

public class BaseCraftingRemoval extends BaseListRemoval<IRecipe> {
	public BaseCraftingRemoval(String name, List<IRecipe> list, List<IRecipe> recipes) {
		super(name, list, recipes);
	}
	
    @Override
    public String getRecipeInfo(IRecipe recipe) {
        return LogHelper.getStackDescription(recipe.getRecipeOutput());
    }
    
    public static List<IRecipe> getRecipes(List<IRecipe> list, IIngredient ingredient) {
        List<IRecipe> recipes = new LinkedList<IRecipe>();
        
        for (IRecipe r : list) {
            if (r != null && r.getRecipeOutput() != null && r.getRecipeOutput() instanceof ItemStack && matches(ingredient, toIItemStack(r.getRecipeOutput()))) {
                recipes.add(r);
            }
        }
        
        return recipes;
    }
}