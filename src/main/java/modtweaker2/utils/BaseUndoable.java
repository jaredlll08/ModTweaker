package modtweaker2.utils;

import minetweaker.IUndoableAction;

public abstract class BaseUndoable implements IUndoableAction {
	@Override
	public boolean canUndo() {
		return true;
	}

	@Override
	public Object getOverrideKey() {
		return null;
	}
}
