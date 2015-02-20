package modtweaker2.util;

import java.util.List;

import minetweaker.IUndoableAction;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public abstract class BaseListAddition implements IUndoableAction {
    protected final List list;
    protected final Object recipe;
    protected String description;

    public BaseListAddition(String description, List list, Object recipe) {
        this(list, recipe);
        this.description = description;
    }

    public BaseListAddition(List list, Object recipe) {
        this.list = list;
        this.recipe = recipe;
    }

    @Override
    public void apply() {
        list.add(recipe);
    }

    @Override
    public boolean canUndo() {
        return list != null;
    }

    @Override
    public void undo() {
        list.remove(recipe);
    }

    public String getRecipeInfo() {
        return "Unknown Item";
    }

    @Override
    public String describe() {
        if (recipe instanceof ItemStack) return "Adding " + description + " Recipe for :" + ((ItemStack) recipe).getDisplayName();
        else if (recipe instanceof FluidStack) return "Adding " + description + " Recipe for :" + ((FluidStack) recipe).getFluid().getLocalizedName();
        else return "Adding " + description + " Recipe for :" + getRecipeInfo();
    }

    @Override
    public String describeUndo() {
        if (recipe instanceof ItemStack) return "Removing " + description + " Recipe for :" + ((ItemStack) recipe).getDisplayName();
        else if (recipe instanceof FluidStack) return "Removing " + description + " Recipe for :" + ((FluidStack) recipe).getFluid().getLocalizedName();
        else return "Removing " + description + " Recipe for :" + getRecipeInfo();
    }

    @Override
    public Object getOverrideKey() {
        return null;
    }
}
