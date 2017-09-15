package forestry_old;

import com.blamejared.mtlib.utils.BaseListAddition;
import forestry.api.recipes.ICraftingProvider;
import forestry.api.recipes.IForestryRecipe;

import java.util.ArrayList;

public abstract class ForestryListAddition<T extends IForestryRecipe> extends BaseListAddition<T> {
	private final ICraftingProvider manager;
	
	protected ForestryListAddition(String name, ICraftingProvider manager) {
		super(name, new ArrayList(manager.recipes()));
		this.manager = manager;
	}

	@Override
	protected abstract String getRecipeInfo(T recipe);
	
	@Override
	public void apply() {
		for (T recipe : recipes) {
            if (recipe != null) {
                manager.addRecipe(recipe);
            }
        }
	}

}
