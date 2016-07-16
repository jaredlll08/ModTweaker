package modtweaker.utils;

import modtweaker.helpers.LogHelper;

import java.util.List;

public abstract class BaseListAddition<T> extends BaseListModification<T> {

    protected BaseListAddition(String name, List<T> list) {
        super(name, list);
    }
    
    protected BaseListAddition(String name, List<T> list, List<T> recipies) {
        this(name, list);
        if(recipes != null) {
            recipes.addAll(recipies);
        }
    }

    @Override
    public void apply() {
        if(recipes.isEmpty()) {
            return;
        }

        for(T recipe : recipes) {
            if(recipe != null) {
                if(list.add(recipe)) {
                    successful.add(recipe);
                } else {
                    LogHelper.logError(String.format("Error adding %s Recipe for %s", name, getRecipeInfo(recipe)));
                }
            } else {
                LogHelper.logError(String.format("Error adding %s Recipe: null object", name));
            }
        }
    }
    
    @Override
    public void undo() {
        if(this.successful.isEmpty()) {
            return;
        }
        
        for(T recipe : successful) {
            if(recipe != null) {
                if(!list.remove(recipe)) {
                    LogHelper.logError(String.format("Error removing %s Recipe for %s", name, this.getRecipeInfo(recipe)));
                }
            } else {
                LogHelper.logError(String.format("Error removing %s Recipe: null object", name));
            }
        }
    }

    @Override
    public String describe() {
        return String.format("[ModTweaker] Adding %d %s Recipe(s) for %s", recipes.size(), name, getRecipeInfo());
    }

    @Override
    public String describeUndo() {
        return String.format("[ModTweaker] Removing %d %s Recipe(s) for %s", recipes.size(), name, getRecipeInfo());
    }
}
