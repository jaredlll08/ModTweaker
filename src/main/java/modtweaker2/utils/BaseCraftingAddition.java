package modtweaker2.utils;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;

import java.util.List;

import static modtweaker2.helpers.StackHelper.areEqual;

public abstract class BaseCraftingAddition extends BaseDescriptionAddition {
	protected final List<IRecipe> list;
	protected final boolean shapeless;
	protected final ItemStack output;
	protected final Object[] recipe;

	public BaseCraftingAddition(String name, boolean shapeless, List list, ItemStack output, Object... recipe) {
		super(name);
		this.shapeless = shapeless;
		this.output = output;
		this.recipe = recipe;
		this.list = list;
		TweakerPlugin.changed.add(output);
	}

	@Override
	public void apply() {
		if (shapeless) applyShapeless();
		else applyShaped();
	}

	public abstract void applyShaped();

	public abstract void applyShapeless();

	@Override
	public boolean canUndo() {
		return list != null;
	}

	@Override
	public void undo() {
		IRecipe remove = null;
		for (IRecipe recipe : list) {
			if (recipe.getRecipeOutput() != null && areEqual(recipe.getRecipeOutput(), output)) {
				remove = recipe;
				break;
			}
		}

		list.remove(remove);
	}

	@Override
	public String getRecipeInfo() {
		return output.getDisplayName();
	}
}
