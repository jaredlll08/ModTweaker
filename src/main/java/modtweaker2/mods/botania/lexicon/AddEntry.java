package modtweaker2.mods.botania.lexicon;

import minetweaker.IUndoableAction;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.lexicon.LexiconEntry;

public class AddEntry implements IUndoableAction {
	
	LexiconEntry Entry;

    public AddEntry(LexiconEntry Entry) {
        this.Entry=Entry;
    }

    @Override
	public void apply() {
    	BotaniaAPI.addEntry(Entry, Entry.category);
	}

	@Override
	public boolean canUndo() {
        return Entry != null;
	}
	
	@Override
	public String describe() {
        return "Adding Lexicon Entry: " + Entry.getUnlocalizedName();
	}
	
	@Override
	public String describeUndo() {
        return "Removing Lexicon Entry: " + Entry.getUnlocalizedName();
	}
	
	@Override
	public void undo() {
		Entry.category.entries.remove(Entry);
		BotaniaAPI.getAllEntries().remove(Entry);
	}
	
	@Override
	public Object getOverrideKey() {
		return null;
	}
}
