package modtweaker2.util;

import java.util.Map;

import minetweaker.IUndoableAction;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public abstract class BaseMapAddition implements IUndoableAction {
    protected String description;
    protected final Map map;
    protected Object key;
    protected Object recipe;

    public BaseMapAddition(Map map, Object key, Object recipe) {
        this(null, map, key, recipe);
    }

    public BaseMapAddition(String description, Map map, Object key, Object recipe) {
        this.map = map;
        this.key = key;
        this.recipe = recipe;
        this.description = description;
    }

    @Override
    public void apply() {
        map.put(key, recipe);
    }

    @Override
    public boolean canUndo() {
        return map != null;
    }

    @Override
    public void undo() {
        map.remove(key);
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
