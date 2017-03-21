package modtweaker.mods.forestry;

import com.blamejared.mtlib.helpers.LogHelper;
import com.blamejared.mtlib.utils.BaseListAddition;
import forestry.api.recipes.IForestryRecipe;
import forestry.api.recipes.ICraftingProvider;
import mezz.jei.api.recipe.*;
import minetweaker.MineTweakerAPI;

import java.util.*;

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
				if (manager.addRecipe(recipe)){
					successful.add(recipe);
					IRecipeWrapper wrapped = wrapRecipe(recipe);
					if (wrapped != null){
						MineTweakerAPI.getIjeiRecipeRegistry().addRecipe(wrapped);
					}
				} else {
					LogHelper.logError(String.format("Error adding %s Recipe for %s", name, getRecipeInfo(recipe)));
				}
			} else {
				LogHelper.logError(String.format("Error adding %s Recipe: null object", name));
			}
		}
	}
	
	@Override
	public final void undo() {
		for (T recipe : successful) {
			if (recipe != null) {
				if (!manager.removeRecipe(recipe)) {
					LogHelper.logError(String.format("Error removing %s Recipe for %s", name, this.getRecipeInfo(recipe)));
				}else{
					IRecipeWrapper wrapped = wrapRecipe(recipe);
					if (wrapped != null){
						MineTweakerAPI.getIjeiRecipeRegistry().removeRecipe(wrapped);
					}
				}
			} else {
				LogHelper.logError(String.format("Error removing %s Recipe: null object", name));
			}
		}
	}

}
