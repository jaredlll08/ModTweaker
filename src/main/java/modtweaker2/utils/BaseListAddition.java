package modtweaker2.utils;

import java.util.List;

import modtweaker2.helpers.LogHelper;

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
                if(this.list.add(recipe)) {
                    successful.add(recipe);
                } else {
                    LogHelper.logError(String.format("Error adding %s Recipe for %s", this.name, this.getRecipeInfo(recipe)));
                }
            } else {
                LogHelper.logError(String.format("Error adding %s Recipe: null object", this.name));
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
                if(!this.list.remove(recipe)) {
                    LogHelper.logError(String.format("Error removing %s Recipe for %s", this.name, this.getRecipeInfo(recipe)));
                }
            } else {
                LogHelper.logError(String.format("Error removing %s Recipe: null object", this.name));
            }
        }
    }

    @Override
    public String describe() {
        return String.format("[ModTweaker2] Adding %d %s Recipe(s) for %s", this.recipes.size(), this.name, this.getRecipeInfo());
    }

    @Override
    public String describeUndo() {
        return String.format("[ModTweaker2] Removing %d %s Recipe(s) for %s", this.recipes.size(), this.name, this.getRecipeInfo());
    }
}