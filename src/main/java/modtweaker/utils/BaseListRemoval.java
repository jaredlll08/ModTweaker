package modtweaker.utils;

import modtweaker.helpers.LogHelper;

import java.util.List;

public abstract class BaseListRemoval<T> extends BaseListModification<T> {

    protected BaseListRemoval(String name, List<T> list) {
        super(name, list);
    }

    protected BaseListRemoval(String name, List<T> list, List<T> recipies) {
        this(name, list);

        if (recipes != null) {
            recipes.addAll(recipies);
        }
    }

    @Override
    public void apply() {
        if (recipes.isEmpty()) {
            return;
        }
        for (T recipe : this.recipes) {
            if (recipe != null) {
                if (this.list.remove(recipe)) {
                    successful.add(recipe);
                } else {
                    LogHelper.logError(String.format("Error removing %s Recipe for %s", name, getRecipeInfo(recipe)));
                }
            } else {
                LogHelper.logError(String.format("Error removing %s Recipe: null object", name));
            }
        }
    }

    @Override
    public void undo() {
        if (successful.isEmpty()) {
            return;
        }
        for (T recipe : successful) {
            if (recipe != null) {
                if (!list.add(recipe)) {
                    LogHelper.logError(String.format("Error restoring %s Recipe for %s", name, getRecipeInfo(recipe)));
                }
            } else {
                LogHelper.logError(String.format("Error restoring %s Recipe: null object", name));
            }
        }
    }

    @Override
    public String describe() {
        return String.format("[ModTweaker] Removing %d %s Recipe(s) for %s", this.recipes.size(), this.name, this.getRecipeInfo());
    }

    @Override
    public String describeUndo() {
        return String.format("[ModTweaker] Restoring %d %s Recipe(s) for %s", this.recipes.size(), this.name, this.getRecipeInfo());
    }
}
