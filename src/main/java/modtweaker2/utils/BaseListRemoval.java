package modtweaker2.utils;

import minetweaker.IUndoableAction;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.LinkedList;
import java.util.List;

public abstract class BaseListRemoval implements IUndoableAction {
	protected final String description;
	protected final List list;

	protected final FluidStack fluid;
	protected final ItemStack stack;
	protected final LinkedList recipes = new LinkedList();

	public BaseListRemoval(String description, List list, ItemStack stack, FluidStack fluid) {
		this.list = list;
		this.stack = stack;
		this.description = description;
		this.fluid = fluid;
	}

	public BaseListRemoval(String description, List list, ItemStack stack) {
		this(description, list, stack, null);
	}

	public BaseListRemoval(String description, List list, FluidStack fluid) {
		this(description, list, null, fluid);
	}

	public BaseListRemoval(List list, ItemStack stack) {
		this(null, list, stack);
	}

	public BaseListRemoval(List list, FluidStack stack) {
		this(null, list, stack);
	}

	public BaseListRemoval(String description, List list) {
		this(description, list, null, null);
	}
	
	@Override
	public void apply() {
	    for(Object recipe : recipes) {
	        list.remove(recipe);
	    }
	}

	@Override
	public boolean canUndo() {
		return list.size() > 0;
	}

	@Override
	public void undo() {
	    for(Object recipe : recipes) {
	        list.add(recipe);
	    }
	}

	public String getRecipeInfo() {
		return "Unknown Item";
	}

	@Override
	public String describe() {
		if (recipes.getFirst() instanceof ItemStack)
			return "Removing " + description + " Recipe(s) for :" + getItemStackNames(recipes);
		else if (recipes.getFirst() instanceof FluidStack)
			return "Removing " + description + " Recipe(s) for :" + getFluidStackNames(recipes);
		else return "Removing " + description + " Recipe for :" + getRecipeInfo();
	}

	@Override
	public String describeUndo() {
		if (recipes.getFirst() instanceof ItemStack)
			return "Restoring " + description + " Recipe(s) for :" + getItemStackNames(recipes);
		else if (recipes.getFirst() instanceof FluidStack)
			return "Restoring " + description + " Recipe(s) for :" + getFluidStackNames(recipes);
		else return "Restoring " + description + " Recipe for :" + getRecipeInfo();
	}

	@Override
	public Object getOverrideKey() {
		return null;
	}
	
    private String getItemStackNames(LinkedList list) {
        if(list.size() == 1)
            return ((ItemStack)list.getFirst()).getDisplayName();

        StringBuilder sb = new StringBuilder();
        
        for(Object entry : list) {
            sb.append(((ItemStack)entry).getDisplayName()).append(", ");
        }
        
        sb.setLength(sb.length() - 2);
        
        return sb.toString();
    }
    
    private String getFluidStackNames(LinkedList list) {
        if(list.size() == 1)
            return ((FluidStack)list.getFirst()).getFluid().getLocalizedName();

        StringBuilder sb = new StringBuilder();
        
        for(Object entry : list) {
            sb.append(((FluidStack)entry).getFluid().getLocalizedName()).append(", ");
        }
        
        sb.setLength(sb.length() - 2);
        
        return sb.toString();
    }
}
