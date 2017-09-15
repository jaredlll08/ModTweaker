package forestry_old;

import com.blamejared.mtlib.helpers.LogHelper;
import com.blamejared.mtlib.utils.BaseListRemoval;
import forestry.api.recipes.ICraftingProvider;
import forestry.api.recipes.IForestryRecipe;

import java.util.ArrayList;
import java.util.List;

public abstract class ForestryListRemoval<T extends IForestryRecipe, C extends ICraftingProvider<T>> extends BaseListRemoval<T> {
	private final C craftingProvider;

	public ForestryListRemoval(String name, C craftingProvider, List<T> recipes) {
		super(name, new ArrayList<T>(craftingProvider.recipes()), recipes);
		this.craftingProvider = craftingProvider;
	}

	@Override
	protected abstract String getRecipeInfo(T recipe);

	@Override
	public void apply() {
		for (T recipe : recipes) {
			if (recipe != null) {
				craftingProvider.removeRecipe(recipe);
			} else {
				LogHelper.logError(String.format("Error removing %s Recipe: null object", name));
			}
		}
	}
}
