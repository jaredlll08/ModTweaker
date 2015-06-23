package modtweaker2.utils;

import java.util.Map;

import minetweaker.IUndoableAction;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public abstract class BaseMapRemoval implements IUndoableAction {
	protected final Map map;
	protected Object stack;
	protected String description;
	protected Object key;
	protected Object recipe;

	public BaseMapRemoval(Object stack, Map map, Object key) {
		this(null, map, key, stack);
	}

	public BaseMapRemoval(String description, Map map, Object stack) {
		this(description, map, null, stack);
	}

	public BaseMapRemoval(String description, Map map, Object key, Object stack) {
		this.stack = stack;
		this.map = map;
		this.key = key;
		this.description = description;
	}

	@Override
	public void apply() {
		recipe = map.get(key);
		map.remove(key);
	}

	@Override
	public boolean canUndo() {
		return map != null;
	}

	@Override
	public void undo() {
		map.put(key, recipe);
	}

	public String getRecipeInfo() {
		return "Unknown Item";
	}

	@Override
	public String describe() {
		if (recipe instanceof ItemStack)
			return "Removing " + description + " Recipe for : " + ((ItemStack) recipe).getDisplayName();
		else if (recipe instanceof FluidStack)
			return "Removing " + description + " Recipe for : " + ((FluidStack) recipe).getFluid().getLocalizedName();
		else return "Removing " + description + " Recipe for :" + getRecipeInfo();
	}

	@Override
	public String describeUndo() {
		if (recipe instanceof ItemStack)
			return "Restoring " + description + " Recipe for : " + ((ItemStack) recipe).getDisplayName();
		else if (recipe instanceof FluidStack)
			return "Restoring " + description + " Recipe for : " + ((FluidStack) recipe).getFluid().getLocalizedName();
		else return "Restoring " + description + " Recipe for : " + getRecipeInfo();
	}

	@Override
	public Object getOverrideKey() {
		return null;
	}
}
