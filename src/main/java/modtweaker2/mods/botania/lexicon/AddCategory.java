package modtweaker2.mods.botania.lexicon;

import minetweaker.IUndoableAction;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.lexicon.LexiconCategory;

public class AddCategory implements IUndoableAction {
	
	LexiconCategory category;

    public AddCategory(LexiconCategory category) {
        this.category=category;
    }

    @Override
	public void apply() {
    	BotaniaAPI.addCategory(category);
	}

	@Override
	public boolean canUndo() {
        return category != null;
	}
	
	@Override
	public String describe() {
        return "Adding Lexicon Category: " + category.getUnlocalizedName();
	}
	
	@Override
	public String describeUndo() {
        return "Removing Lexicon Category: " + category.getUnlocalizedName();
	}
	
	@Override
	public void undo() {
		BotaniaAPI.getAllCategories().remove(category);
	}
	
	@Override
	public Object getOverrideKey() {
		return null;
	}
}
