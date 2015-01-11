package modtweaker.util;

import minetweaker.IUndoableAction;

public abstract class BaseDescriptionRemoval implements IUndoableAction {
    protected String description;

    public BaseDescriptionRemoval(String description) {
        this.description = description;
    }

    @Override
    public boolean canUndo() {
        return true;
    }

    public String getRecipeInfo() {
        return "Unknown Item";
    }

    @Override
    public String describe() {
        return "Removing " + description + " Recipe for :" + getRecipeInfo();
    }

    @Override
    public String describeUndo() {
        return "Restoring " + description + " Recipe for :" + getRecipeInfo();
    }

    @Override
    public Object getOverrideKey() {
        return null;
    }
}
