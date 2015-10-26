package modtweaker2.mods.forestry;

import java.util.ArrayList;
import java.util.List;

import forestry.api.recipes.ICraftingProvider;
import forestry.api.recipes.IForestryRecipe;

import modtweaker2.helpers.LogHelper;
import modtweaker2.utils.BaseListAddition;

public abstract class ForestryListAddition<T extends IForestryRecipe, C extends ICraftingProvider<T>> extends BaseListAddition<T> {
	private final C craftingProvider;

	protected ForestryListAddition(String name, C craftingProvider) {
		super(name, new ArrayList<T>(craftingProvider.recipes()));
		this.craftingProvider = craftingProvider;
	}

	protected ForestryListAddition(String name, C craftingProvider, List<T> recipes) {
		super(name, new ArrayList<T>(craftingProvider.recipes()), recipes);
		this.craftingProvider = craftingProvider;
	}

	@Override
	protected abstract String getRecipeInfo(T recipe);

	@Override
	public final void apply() {
		for (T recipe : recipes) {
			if (recipe != null) {
				if (craftingProvider.addRecipe(recipe)) {
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
				if (!craftingProvider.removeRecipe(recipe)) {
					LogHelper.logError(String.format("Error removing %s Recipe for %s", name, this.getRecipeInfo(recipe)));
				}
			} else {
				LogHelper.logError(String.format("Error removing %s Recipe: null object", name));
			}
		}
	}
}
