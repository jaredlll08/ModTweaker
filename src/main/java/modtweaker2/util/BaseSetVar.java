package modtweaker2.util;

import minetweaker.IUndoableAction;
import modtweaker2.helpers.ReflectionHelper;

public abstract class BaseSetVar implements IUndoableAction {
    protected Object object;
    protected final String description;
    protected final Class clazz;
    protected final String field;
    protected final int original;
    protected final int newValue;

    public BaseSetVar(String description, Class clazz, Object object, String field, int original, int newValue) {
        this(description, clazz, field, original, newValue);
        this.object = object;
    }

    public BaseSetVar(String description, Class clazz, String field, int original, int newValue) {
        this.description = description;
        this.clazz = clazz;
        this.field = field;
        this.original = original;
        this.newValue = newValue;
    }

    @Override
    public void apply() {
        ReflectionHelper.setPrivateValue(clazz, object, field, newValue);
    }

    @Override
    public boolean canUndo() {
        return true;
    }

    @Override
    public void undo() {
        ReflectionHelper.setPrivateValue(clazz, object, field, original);
    }

    @Override
    public String describe() {
        return "Setting " + description + " to " + newValue;
    }

    @Override
    public String describeUndo() {
        return "Setting " + description + " to the default value of " + original;
    }

    @Override
    public Object getOverrideKey() {
        return null;
    }
}
