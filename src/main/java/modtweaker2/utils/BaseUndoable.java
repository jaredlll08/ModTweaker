package modtweaker2.utils;

import minetweaker.IUndoableAction;

public abstract class BaseUndoable implements IUndoableAction {
	protected String description;
	protected boolean add;
	
	/**
	 * 
	 * @param description of the action (Mod + Machine/Recipe handler)
	 * @param add true = Adding recipe, false = Removing recipe. Used for description
	 */
	public BaseUndoable(String description, boolean add){
		this.description = description;
		this.add = add;
	}
	
	@Override
	public boolean canUndo() {
		return true;
	}

	@Override
	public Object getOverrideKey() {
		return null;
	}
	
	/**
	 * 
	 * @return the description of the recipe that is being added/Removed
	 * Good formats are: 
	 * (output)
	 *  -or-
	 * (output) from (input)
	 */ 
	public abstract String getRecipeInfo();
	
	@Override
	public String describe() {
		if(add)
			return "Added " + description + " recipe to get: " + getRecipeInfo();
		else
			return "Removed " + description + " recipe to get: " + getRecipeInfo();
	}

	@Override
	public String describeUndo() {
		if(add)
			return "Removed " + description + " recipe to get: " + getRecipeInfo();
		else
			return "Added " + description + " recipe to get: " + getRecipeInfo();
	}
}
