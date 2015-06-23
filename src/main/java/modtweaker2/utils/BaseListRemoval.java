package modtweaker2.utils;

import minetweaker.IUndoableAction;
import minetweaker.MineTweakerAPI;
import modtweaker2.helpers.LogHelper;
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
	    if(recipes.size() == 0) {
	        MineTweakerAPI.logWarning(String.format("No %s Recipes to remove for: %s", this.description, getRecipeInfo()));
	        return;
	    }
	    
	    for(Object recipe : recipes) {
	        list.remove(recipe);
	    }
	}

	@Override
	public boolean canUndo() {
		return recipes.size() > 0;
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
	    if(recipes.size() > 0) {
            return String.format("Removing %d %s Recipe(s) for: %s", recipes.size(), this.description, getRecipeNames(recipes));
	    } else {
	        return String.format("Removing %s Recipe(s) for: %s", this.description, getRecipeInfo());
	    }
	}

	@Override
	public String describeUndo() {
        if(recipes.size() > 0) {
            return String.format("Restoring %d %s Recipe(s) for: %s", recipes.size(), this.description, getRecipeNames(recipes));
        } else {
            return String.format("No %s Recipes found to restore for: %s", this.description, getRecipeInfo());
        }
	}

	@Override
	public Object getOverrideKey() {
		return null;
	}
	
	/***
	 * Returns the names of the recipes inside the list
	 * @param list which holds the recipes
	 * @return name or list of names describing the recipe
	 */
	private String getRecipeNames(LinkedList list) {
	    if(list.size() == 0)
	        return getRecipeInfo();
	    
	    if(list.size() == 1) {
	        if(recipes.getFirst() instanceof ItemStack)
	            return ((ItemStack)list.getFirst()).getDisplayName();
	        else if (recipes.getFirst() instanceof FluidStack)
	            return ((FluidStack)list.getFirst()).getFluid().getLocalizedName();
	        else
	            return getRecipeInfo();
	    }
	    
	    StringBuilder sb = new StringBuilder();
	    
	    if(recipes.getFirst() instanceof ItemStack) {
	        for(ItemStack itemStack : (List<ItemStack>)list) {
	            sb.append(itemStack.getDisplayName()).append(", ");
	        }
	    } else if (recipes.getFirst() instanceof FluidStack) {
	        for(FluidStack fluidStack : (List<FluidStack>)list) {
	            sb.append(fluidStack.getFluid().getLocalizedName()).append(", ");
	        }
	    } else {
	        sb.append(getRecipeInfo()).append(", ");
	    }
	    
	    sb.setLength(sb.length() - 2);
	    return sb.toString();
	}
}
