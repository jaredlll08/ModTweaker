package modtweaker.util;

import minetweaker.IUndoableAction;

public abstract class BaseDescriptionAddition implements IUndoableAction {
    protected String description;

    public BaseDescriptionAddition(String description) {
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
        return "Adding " + description + " Recipe for :" + getRecipeInfo();
    }

    @Override
    public String describeUndo() {
        return "Removing " + description + " Recipe for :" + getRecipeInfo();
    }

    @Override
    public Object getOverrideKey() {
        return null;
    }
}
