package modtweaker2.utils;

import java.util.LinkedList;

import minetweaker.IUndoableAction;

public class BaseMultiModification extends BaseUndoable {

    protected final LinkedList<IUndoableAction> actions;
    
    protected BaseMultiModification(String name) {
        super(name);
        this.actions = new LinkedList<IUndoableAction>();
    }

    @Override
    public void apply() {
        for(IUndoableAction action : actions) {
            action.apply();
        }
    }

    @Override
    public boolean canUndo() {
        for(IUndoableAction action : actions) {
            if(!action.canUndo())
                return false;
        }

        return true;
    }

    @Override
    public String describe() {
        return String.format("Applying %d actions for %s Recipe change", this.name, this.actions.size());
    }

    @Override
    public String describeUndo() {
        return String.format("Reverting %d actions for %s Recipe change", this.name, this.actions.size());
    }

    @Override
    public void undo() {
        for(IUndoableAction action : actions) {
            action.undo();
        }
    }

}
