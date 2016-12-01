package modtweaker.mods.forestry;

import com.blamejared.mtlib.helpers.LogHelper;
import com.blamejared.mtlib.utils.BaseListAddition;
import forestry.api.recipes.IForestryRecipe;

import java.util.*;

public abstract class ForestryListAddition<T extends IForestryRecipe> extends BaseListAddition<T> {
	private final List<T> recipeList;

	protected ForestryListAddition(String name, List<T> recipeList) {
		super(name, recipeList);
		this.recipeList = recipeList;
		
	}

	@Override
	protected abstract String getRecipeInfo(T recipe);

	@Override
	public void apply() {
		for (T recipe : recipes) {
			if (recipe != null) {
				if (recipeList.add(recipe)) {
					successful.add(recipe);
				} else {
					LogHelper.logError(String.format("Error adding %s Recipe for %s", name, getRecipeInfo(recipe)));
				}
			} else {
				LogHelper.logError(String.format("Error removing %s Recipe: null object", name));
			}
		}
	}

	@Override
	public final void undo() {
		for (T recipe : successful) {
			if (recipe != null) {
				if (!recipeList.remove(recipe)) {
					LogHelper.logError(String.format("Error removing %s Recipe for %s", name, this.getRecipeInfo(recipe)));
				}
			} else {
				LogHelper.logError(String.format("Error removing %s Recipe: null object", name));
			}
		}
	}
}
