package modtweaker2.mods.botania.lexicon;

import minetweaker.IUndoableAction;
import vazkii.botania.api.lexicon.LexiconCategory;

public class SetCategoryPriority implements IUndoableAction {
	
	LexiconCategory category;
	int oldPriority;
	int newPriority;

    public SetCategoryPriority(LexiconCategory category, int priority) {
        this.category=category;
        this.newPriority=priority;
    }

    @Override
	public void apply() {
    	oldPriority=category.getSortingPriority();
    	category.setPriority(newPriority);
	}

	@Override
	public boolean canUndo() {
        return category != null;
	}
	
	@Override
	public String describe() {
        return "Setting Lexicon Category priority: " + category.getUnlocalizedName();
	}
	
	@Override
	public String describeUndo() {
        return "Unsetting Lexicon Category priority: " + category.getUnlocalizedName();
	}
	
	@Override
	public void undo() {
    	category.setPriority(oldPriority);
	}
	
	@Override
	public Object getOverrideKey() {
		return null;
	}
}
