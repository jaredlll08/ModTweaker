package modtweaker2.utils;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;

import java.util.List;

import static modtweaker2.helpers.StackHelper.areEqual;

public class BaseCraftingRemoval extends BaseListRemoval {
	public BaseCraftingRemoval(String name, List list, ItemStack stack) {
		super(name, list, stack);
	}

	@Override
	public void apply() {
		for (IRecipe r : (List<IRecipe>) list) {
			if (r.getRecipeOutput() != null && r.getRecipeOutput() instanceof ItemStack && areEqual(r.getRecipeOutput(), stack)) {
				recipe = r;
				break;
			}
		}

		list.remove(recipe);
	}

	@Override
	public String getRecipeInfo() {
		return stack.getDisplayName();
	}
}
