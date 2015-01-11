package modtweaker.util;

import java.util.List;

import minetweaker.IUndoableAction;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public abstract class BaseListRemoval implements IUndoableAction {
    protected final String description;
    protected final List list;
    protected final FluidStack fluid;
    protected final ItemStack stack;
    protected Object recipe;

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
    public boolean canUndo() {
        return list != null;
    }

    @Override
    public void undo() {
        list.add(recipe);
    }

    public String getRecipeInfo() {
        return "Unknown Item";
    }

    @Override
    public String describe() {
        if (recipe instanceof ItemStack) return "Removing " + description + " Recipe for :" + ((ItemStack) recipe).getDisplayName();
        else if (recipe instanceof FluidStack) return "Removing " + description + " Recipe for :" + ((FluidStack) recipe).getFluid().getLocalizedName();
        else return "Removing " + description + " Recipe for :" + getRecipeInfo();
    }

    @Override
    public String describeUndo() {
        if (recipe instanceof ItemStack) return "Restoring " + description + " Recipe for :" + ((ItemStack) recipe).getDisplayName();
        else if (recipe instanceof FluidStack) return "Restoring " + description + " Recipe for :" + ((FluidStack) recipe).getFluid().getLocalizedName();
        else return "Restoring " + description + " Recipe for :" + getRecipeInfo();
    }

    @Override
    public Object getOverrideKey() {
        return null;
    }
}
